package com.yomo.yakgwa.ui.voteResult

import android.content.ActivityNotFoundException
import android.content.Intent
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
import com.yomo.domain.model.response.MeetDetailResponseEntity.MeetInfo
import com.yomo.domain.model.response.TimeCandidateResponseEntity
import com.yomo.domain.model.response.VotePlaceResponseEntity
import com.yomo.yakgwa.R
import com.yomo.yakgwa.databinding.FragmentVoteResultBinding
import com.yomo.yakgwa.model.NaviModel
import com.yomo.yakgwa.type.MeetType
import com.yomo.yakgwa.type.NaviType
import com.yomo.yakgwa.ui.invitation.ParticipantMemberListAdapter
import com.yomo.yakgwa.util.DateTimeUtils.formatDateTimeToKoreanDate
import com.yomo.yakgwa.util.DateTimeUtils.formatDateTimeToKoreanTime
import com.yomo.yakgwa.util.OverlapDecoration
import com.yomo.yakgwa.util.UiState
import com.yomo.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


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
                            confirmTimeListAdapter.submitList(it.data.timeInfos)
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
                            confirmPlaceListAdapter.submitList(it.data.placeInfos)
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
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.confirmPlaceState.collect {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            Snackbar.make(requireView(), it.data, Snackbar.LENGTH_SHORT).show()
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
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.confirmTimeState.collect {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            Snackbar.make(requireView(), it.data, Snackbar.LENGTH_SHORT).show()
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

        lifecycleScope.launch {
            viewModel.staticMapState.collect {
                binding.ivPromisePlaceMap.load(it)
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

                timeInfo.timeInfos?.first()?.voteTime?.let { voteTime ->
                    binding.tvPromiseDate.text = formatDateTimeToKoreanDate(voteTime)
                    binding.tvPromiseTime.text = formatDateTimeToKoreanTime(voteTime)
                }
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

                voteInfo.placeInfos?.first()?.let { placeInfo ->
                    binding.tvTitle.text = placeInfo.title
                    binding.tvAddress.text = placeInfo.roadAddress

                    lifecycleScope.launch {
                        val location = viewModel.geoCoding(placeInfo.roadAddress)

                        viewModel.getStaticMap(
                            width = 600,
                            height = 240,
                            center = "${location.longitude},${location.latitude}",
                            level = 17,
                            markers = "type:d|size:mid|pos:${location.longitude}%20${location.latitude}"
                        )
                    }
                }
            }
        }
    }

    private fun handleNaverMap(naviModel: NaviModel) {
        try {
            lifecycleScope.launch {
                val location = viewModel.geoCoding(naviModel.address)
                val url =
                    "nmap://navigation?dlat=${location.latitude}" +
                            "&dlng=${location.longitude}" +
                            "&dname=${naviModel.address}" +
                            "&appname=com.prography.yakgwa"

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                startActivity(intent)
            }
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
            lifecycleScope.launch {
                val location = viewModel.geoCoding(naviModel.address)

                startActivity(
                    NaviClient.instance.navigateIntent(
                        Location(
                            naviModel.address,
                            location.longitude.toString(),
                            location.latitude.toString()
                        ),
                        NaviOption(coordType = CoordType.WGS84)
                    )
                )
            }
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

        binding.ivKakaoMapBtn.setOnClickListener {
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

    private fun navigateToParticipantMemberFragment() {
        VoteResultFragmentDirections.actionVoteResultFragmentToParticipantMemberFragment(
            viewModel.participantInfo.value
        ).apply {
            findNavController().navigate(this)
        }
    }
}