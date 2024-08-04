package com.prography.yakgwa.ui.voteResult

import android.content.ActivityNotFoundException
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.kakao.sdk.navi.Constants
import com.kakao.sdk.navi.NaviClient
import com.kakao.sdk.navi.model.CoordType
import com.kakao.sdk.navi.model.Location
import com.kakao.sdk.navi.model.NaviOption
import com.prography.domain.model.response.MeetDetailResponseEntity.MeetInfo
import com.prography.domain.model.response.TimeCandidateResponseEntity
import com.prography.domain.model.response.VotePlaceResponseEntity
import com.prography.yakgwa.BuildConfig.NAVER_CLIENT_ID
import com.prography.yakgwa.BuildConfig.NAVER_CLIENT_SECRET
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentVoteResultBinding
import com.prography.yakgwa.model.ConfirmPlaceModel
import com.prography.yakgwa.model.ConfirmTimeModel
import com.prography.yakgwa.model.NaviModel
import com.prography.yakgwa.type.MeetType
import com.prography.yakgwa.type.NaviType
import com.prography.yakgwa.ui.invitation.ParticipantMemberListAdapter
import com.prography.yakgwa.util.DateTimeUtils.formatDateTimeToKoreanDate
import com.prography.yakgwa.util.DateTimeUtils.formatDateTimeToKoreanTime
import com.prography.yakgwa.util.OverlapDecoration
import com.prography.yakgwa.util.UiState
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale


@AndroidEntryPoint
class VoteResultFragment :
    BaseFragment<FragmentVoteResultBinding>(R.layout.fragment_vote_result) {
    private val viewModel: VoteResultViewModel by viewModels()
    private lateinit var participantMemberListAdapter: ParticipantMemberListAdapter
    private lateinit var confirmPlaceListAdapter: ConfirmPlaceListAdapter
    private lateinit var confirmTimeListAdapter: ConfirmTimeListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observer()
        addListeners()
    }

    private fun observer() {
        lifecycleScope.launch {
            viewModel.selectedConfirmPlaceState.collectLatest { selectedPlace ->
                confirmPlaceListAdapter.submitList(selectedPlace)
            }
        }

        lifecycleScope.launch {
            viewModel.selectedConfirmTimeState.collectLatest { selectedTime ->
                confirmTimeListAdapter.submitList(selectedTime)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.timeCandidateState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            handleTimeCandidate(it.data)
                            confirmTimeListAdapter.submitList(
                                it.data.timeInfos?.map { timeInfo ->
                                    ConfirmTimeModel(timeInfo)
                                }
                            )
                        }

                        is UiState.Failure -> {
                            Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.votePlaceInfoState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            handleVotePlaceInfo(it.data)
                            confirmPlaceListAdapter.submitList(
                                it.data.placeInfos?.map { placeInfo ->
                                    ConfirmPlaceModel(placeInfo)
                                }
                            )
                        }

                        is UiState.Failure -> {
                            Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.detailMeetState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            showMeetDetails(it.data.meetInfo)
                            viewModel.setParticipantInfo(it.data.participantInfo)
                            participantMemberListAdapter.submitList(it.data.participantInfo.reversed())
                        }

                        is UiState.Failure -> {
                            Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.confirmPlaceState.collect {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            Snackbar.make(requireView(), "장소가 확정되었습니다.", Snackbar.LENGTH_SHORT)
                                .show()
                            viewModel.getUserVotePlace()
                        }

                        is UiState.Failure -> {
                            Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.confirmTimeState.collect {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            Snackbar.make(requireView(), "시간이 확정되었습니다.", Snackbar.LENGTH_SHORT)
                                .show()
                            viewModel.getVoteTimeCandidate()
                        }

                        is UiState.Failure -> {
                            Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.naviActionState.collect {
                when (it.first) {
                    NaviType.NAVER_MAP -> handleNaverMap(it.second)
                    NaviType.KAKAO_MAP -> handleKakaoMap(it.second)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        participantMemberListAdapter = ParticipantMemberListAdapter()
        binding.rvParticipantMember.apply {
            addItemDecoration(OverlapDecoration(requireContext()))
            adapter = participantMemberListAdapter
        }

        confirmPlaceListAdapter = ConfirmPlaceListAdapter().apply {
            setOnItemClickListener {
                viewModel.singleConfirmPlaceSelection(it)
            }
        }
        binding.rvVotePlaceResult.adapter = confirmPlaceListAdapter

        confirmTimeListAdapter = ConfirmTimeListAdapter().apply {
            setOnItemClickListener {
                viewModel.singleConfirmTimeSelection(it)
            }
        }
        binding.rvVoteTimeResult.adapter = confirmTimeListAdapter
    }

    private fun handleTimeCandidate(timeInfo: TimeCandidateResponseEntity) {
        when (timeInfo.meetStatus) {
            MeetType.BEFORE_CONFIRM.name -> {
                binding.cvPromiseTime.visibility = View.GONE
                binding.cvVoteTimeResult.visibility = View.VISIBLE
            }

            MeetType.CONFIRM.name -> {
                binding.cvPromiseTime.visibility = View.VISIBLE
                binding.cvVoteTimeResult.visibility = View.GONE
                binding.tvPromiseDate.text =
                    formatDateTimeToKoreanDate(timeInfo.timeInfos!!.first().voteTime)
                binding.tvPromiseTime.text =
                    formatDateTimeToKoreanTime(timeInfo.timeInfos!!.first().voteTime)
            }
        }
    }

    private fun handleVotePlaceInfo(voteInfo: VotePlaceResponseEntity) {
        when (voteInfo.meetStatus) {
            MeetType.BEFORE_CONFIRM.name -> {
                binding.cvPromisePlace.visibility = View.GONE
                binding.cvVotePlaceResult.visibility = View.VISIBLE
            }

            MeetType.CONFIRM.name -> {
                binding.cvPromisePlace.visibility = View.VISIBLE
                binding.cvVotePlaceResult.visibility = View.GONE

                val placeInfo = voteInfo.placeInfos!!.first()
                binding.tvTitle.text = placeInfo.title
                binding.tvAddress.text = placeInfo.roadAddress

                val geocoder = geoCoding(placeInfo.roadAddress)
                val mapUrl =
                    "https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?w=760&h=300" +
                            "&center=${geocoder.longitude},${geocoder.latitude}" +
                            "&level=17" +
                            "&markers=type:d|size:mid|pos:${geocoder.longitude}%20${geocoder.latitude}" +
                            "&X-NCP-APIGW-API-KEY-ID=${NAVER_CLIENT_ID}" +
                            "&X-NCP-APIGW-API-KEY=${NAVER_CLIENT_SECRET}"

                binding.ivPromisePlaceMap.load(mapUrl)
            }
        }
    }

    private fun handleNaverMap(naviModel: NaviModel) {
        try {
            val url =
                "nmap://navigation?dlat=${geoCoding(naviModel.address).latitude}&dlng=${
                    geoCoding(
                        naviModel.address
                    ).longitude
                }&dname=${naviModel.address}&appname=com.prography.yakgwa"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.nhn.android.nmap")
                )
            )
        }
    }

    private fun handleKakaoMap(naviModel: NaviModel) {
        if (NaviClient.instance.isKakaoNaviInstalled(requireContext())) {
            startActivity(
                NaviClient.instance.navigateIntent(
                    Location(
                        naviModel.address,
                        geoCoding(naviModel.address).longitude.toString(),
                        geoCoding(naviModel.address).latitude.toString()
                    ),
                    NaviOption(coordType = CoordType.WGS84)
                )
            )
        } else {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(Constants.WEB_NAVI_INSTALL)
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
    }

    private fun showMeetDetails(meetInfo: MeetInfo) {
        binding.tvTemaName.text = meetInfo.themeName
        binding.tvInvitationTitle.text = meetInfo.meetTitle
        binding.tvInvitationDescription.text = meetInfo.description
    }

    private fun addListeners() {
        binding.btnConfirmPlace.setOnClickListener {
            viewModel.confirmMeetPlace()
        }

        binding.btnConfirmTime.setOnClickListener {
            viewModel.confirmMeetTime()
        }

        binding.ivNaverMapBtn.setOnClickListener {
            viewModel.onNaviAction(NaviType.NAVER_MAP)
        }

        binding.ivKakoMapBtn.setOnClickListener {
            viewModel.onNaviAction(NaviType.KAKAO_MAP)
        }

        binding.ivNavigateUpBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvShowEntire.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.voteResultFragment) {
                navigateToParticipantMemberFragment()
            }
        }

        binding.ivSave.setOnClickListener {
        }
    }

    private fun geoCoding(address: String): android.location.Location {
        return try {
            Geocoder(requireContext(), Locale.KOREA).getFromLocationName(address, 1)?.let {
                android.location.Location("").apply {
                    latitude = it[0].latitude
                    longitude = it[0].longitude
                }
            } ?: android.location.Location("").apply {
                latitude = 0.0
                longitude = 0.0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            geoCoding(address)
        }
    }

    private fun navigateToParticipantMemberFragment() {
        VoteResultFragmentDirections.actionVoteResultFragmentToParticipantMemberFragment(
            viewModel.participantInfo.value
        ).apply {
            findNavController().navigate(this)
        }
    }
}