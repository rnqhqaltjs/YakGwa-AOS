package com.prography.yakgwa.ui.invitation

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link
import com.prography.domain.model.response.MeetDetailResponseEntity.MeetInfo
import com.prography.domain.model.response.TimeCandidateResponseEntity
import com.prography.domain.model.response.VotePlaceResponseEntity
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentInvitationLeaderBinding
import com.prography.yakgwa.type.MeetType
import com.prography.yakgwa.ui.invitation.InvitationViewModel.Companion.INVITE_ID
import com.prography.yakgwa.util.OverlapDecoration
import com.prography.yakgwa.util.UiState
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
@SuppressLint("SetTextI18n")
class InvitationLeaderFragment :
    BaseFragment<FragmentInvitationLeaderBinding>(R.layout.fragment_invitation_leader) {
    private val viewModel: InvitationViewModel by viewModels()

    private lateinit var voteTimeListAdapter: VoteTimeListAdapter
    private lateinit var votePlaceListAdapter: VotePlaceListAdapter
    private lateinit var participantMemberListAdapter: ParticipantMemberListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observer()
        addListeners()
    }

    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.timeCandidateState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            handleTimeCandidate(it.data)
                            voteTimeListAdapter.submitList(
                                it.data.timeInfos?.sortedBy { timeInfo ->
                                    timeInfo.voteTime
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
                viewModel.votePlaceInfoState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            handleVotePlaceInfo(it.data)
                            votePlaceListAdapter.submitList(it.data.placeInfos)
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
                viewModel.placeCandidateState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> binding.tvPlaceVoteRange.text =
                            "총 ${it.data.size}개 후보 중 원하는 장소"

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
    }

    private fun setupRecyclerView() {
        voteTimeListAdapter = VoteTimeListAdapter()
        binding.rvVoteTime.adapter = voteTimeListAdapter

        votePlaceListAdapter = VotePlaceListAdapter()
        binding.rvVotePlace.adapter = votePlaceListAdapter

        participantMemberListAdapter = ParticipantMemberListAdapter()
        binding.rvParticipantMember.apply {
            addItemDecoration(OverlapDecoration(requireContext()))
            adapter = participantMemberListAdapter
        }
    }

    private fun addListeners() {
        binding.ivInvitationBtn.setOnClickListener {
            viewModel.meetInfoState.value?.let { meetInfo ->
                sendKakaoLink(meetInfo)
            }
        }

        binding.ivNavigateUpBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvShowEntire.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.invitationLeaderFragment) {
                navigateToParticipantMemberFragment()
            }
        }

        binding.btnVoteTime.setOnClickListener {
            navigateToVotePromiseTimeFragment()
        }

        binding.btnVotePlace.setOnClickListener {
            navigateToVotePromisePlaceFragment()
        }

        binding.btnVoteTimeAgain.setOnClickListener {
            navigateToVotePromiseTimeFragment()
        }

        binding.btnVotePlaceAgain.setOnClickListener {
            navigateToVotePromisePlaceFragment()
        }
    }

    private fun sendKakaoLink(meetInfo: MeetInfo) {
        val defaultFeed = createKakaoFeedTemplate(meetInfo)

        if (ShareClient.instance.isKakaoTalkSharingAvailable(requireActivity())) {
            shareViaKakaoTalk(defaultFeed)
        } else {
            shareViaWeb(defaultFeed)
        }
    }

    private fun createKakaoFeedTemplate(meetInfo: MeetInfo): FeedTemplate {
        val executionParams = mapOf(
            INVITE_ID to viewModel.meetId.toString()
        )
        val link = Link(
            androidExecutionParams = executionParams,
            iosExecutionParams = executionParams
        )
        return FeedTemplate(
            content = Content(
                title = meetInfo.meetTitle,
                description = meetInfo.description,
                imageUrl = "http://k.kakaocdn.net/dn/bp2Qmz/btsHbRn5Auu/I4MY1Ks8YoU2npkzSr7WT0/kakaolink40_original.png",
                link = link
            ),
            buttons = listOf(
                Button(
                    "앱으로 보기",
                    link
                )
            )
        )
    }

    private fun shareViaKakaoTalk(defaultFeed: FeedTemplate) {
        ShareClient.instance.shareDefault(
            requireActivity(),
            defaultFeed
        ) { sharingResult, error ->
            if (error != null) {
                Timber.e("카카오톡 공유 실패", error)
            } else if (sharingResult != null) {
                Timber.d("카카오톡 공유 성공 ${sharingResult.intent}")
                startActivity(sharingResult.intent)

                Timber.w("Warning Msg: ${sharingResult.warningMsg}")
                Timber.w("Argument Msg: ${sharingResult.argumentMsg}")
            }
        }
    }

    private fun shareViaWeb(defaultFeed: FeedTemplate) {
        val sharerUrl = WebSharerClient.instance.makeDefaultUrl(defaultFeed)

        try {
            KakaoCustomTabsClient.openWithDefault(requireActivity(), sharerUrl)
        } catch (e: UnsupportedOperationException) {
            Timber.e("CustomTabsServiceConnection 지원 브라우저가 없습니다: $e")
        }

        try {
            KakaoCustomTabsClient.open(requireActivity(), sharerUrl)
        } catch (e: ActivityNotFoundException) {
            Timber.e("디바이스에 설치된 인터넷 브라우저가 없습니다: $e")
        }
    }

    private fun showMeetDetails(meetInfo: MeetInfo) {
        with(binding) {
            tvTemaName.text = meetInfo.themeName
            tvInvitationTitle.text = meetInfo.meetTitle
            tvInvitationDescription.text = meetInfo.description
        }
    }

    private fun handleTimeCandidate(timeInfo: TimeCandidateResponseEntity) {
        when (timeInfo.meetStatus) {
            MeetType.BEFORE_VOTE.name -> {
                binding.cvTimeVote.visibility = View.VISIBLE
                binding.cvMyVoteTime.visibility = View.GONE
                binding.tvTimeVoteRange.text =
                    "${timeInfo.voteDate!!.startVoteDate} ~ ${timeInfo.voteDate!!.endVoteDate} 중 가능한 시간"
            }

            MeetType.VOTE.name -> {
                binding.cvTimeVote.visibility = View.GONE
                binding.cvMyVoteTime.visibility = View.VISIBLE
            }
        }
    }

    private fun handleVotePlaceInfo(voteInfo: VotePlaceResponseEntity) {
        when (voteInfo.meetStatus) {
            MeetType.BEFORE_VOTE.name -> {
                viewModel.getVotePlaceCandidate()
                binding.cvPlaceVote.visibility = View.VISIBLE
                binding.cvMyVotePlace.visibility = View.GONE
            }

            MeetType.VOTE.name -> {
                binding.cvPlaceVote.visibility = View.GONE
                binding.cvMyVotePlace.visibility = View.VISIBLE
            }
        }
    }

    private fun navigateToVotePromiseTimeFragment() {
        InvitationLeaderFragmentDirections.actionInvitationLeaderFragmentToVotePromiseTimeFragment(
            viewModel.meetId
        ).apply {
            findNavController().navigate(this)
        }
    }

    private fun navigateToVotePromisePlaceFragment() {
        InvitationLeaderFragmentDirections.actionInvitationLeaderFragmentToVotePromisePlaceFragment(
            viewModel.meetId
        ).apply {
            findNavController().navigate(this)
        }
    }

    private fun navigateToParticipantMemberFragment() {
        InvitationLeaderFragmentDirections.actionInvitationLeaderFragmentToParticipantMemberFragment(
            viewModel.participantInfo.value
        ).apply {
            findNavController().navigate(this)
        }
    }
}