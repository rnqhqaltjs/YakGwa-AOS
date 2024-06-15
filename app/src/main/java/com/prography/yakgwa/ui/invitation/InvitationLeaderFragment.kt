package com.prography.yakgwa.ui.invitation

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.ItemContent
import com.kakao.sdk.template.model.ItemInfo
import com.kakao.sdk.template.model.Link
import com.prography.domain.model.response.MeetDetailResponseEntity.MeetInfo
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentInvitationLeaderBinding
import com.prography.yakgwa.util.UiState
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class InvitationLeaderFragment :
    BaseFragment<FragmentInvitationLeaderBinding>(R.layout.fragment_invitation_leader) {

    private val viewModel: InvitationViewModel by viewModels()
    private val args by navArgs<InvitationLeaderFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val meetId = args.meetId

        initView(meetId)
        observer()
        addListeners()
    }

    private fun initView(meetId: Int) {
        lifecycleScope.launch {
            viewModel.getMeetInformationDetail(viewModel.userId(), meetId)
        }
    }

    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.detailMeetState.collectLatest {
                    when (it) {
                        is UiState.Loading -> {}
                        is UiState.Success -> {
                            showMeetDetails(it.data.meetInfo)
                        }

                        is UiState.Failure -> {
                        }
                    }
                }
            }
        }
    }

    private fun addListeners() {
        binding.ivInvitationBtn.setOnClickListener {
            lifecycleScope.launch {
                sendKakaoLink(viewModel.userId(), viewModel.meetInfoState.value!!)
            }
        }

        binding.ivNavigateUpBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnTimePlaceVote.setOnClickListener {
            navigateToVotePromiseTimeFragment(viewModel.meetInfoState.value?.meetId!!)
        }
    }

    private fun sendKakaoLink(userId: Int, meetInfo: MeetInfo) {
        val defaultFeed = createKakaoFeedTemplate(userId, meetInfo)

        if (ShareClient.instance.isKakaoTalkSharingAvailable(requireActivity())) {
            shareViaKakaoTalk(defaultFeed)
        } else {
            shareViaWeb(defaultFeed)
        }
    }

    private fun createKakaoFeedTemplate(userId: Int, meetInfo: MeetInfo): FeedTemplate {
        return FeedTemplate(
            content = Content(
                title = meetInfo.meetName,
                description = meetInfo.meetDescription,
                imageUrl = "http://k.kakaocdn.net/dn/bp2Qmz/btsHbRn5Auu/I4MY1Ks8YoU2npkzSr7WT0/kakaolink40_original.png",
                link = Link(
                    androidExecutionParams = mapOf(
                        "userId" to userId.toString(),
                        "meetId" to meetInfo.meetId.toString()
                    ),
                    iosExecutionParams = mapOf("key1" to "value1")
                )
            ),
            itemContent = ItemContent(
                items = listOf(
                    ItemInfo(item = "약속 시간: ", itemOp = "MM월 DD일 HH:MM"),
                    ItemInfo(item = "약속 장소: ", itemOp = "{장소명}"),
                ),
            ),
            buttons = listOf(
                Button(
                    "앱으로 보기",
                    Link(
                        androidExecutionParams = mapOf(
                            "userId" to userId.toString(),
                            "meetId" to meetInfo.meetId.toString()
                        ),
                        iosExecutionParams = mapOf("key1" to "value1")
                    )
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

    @SuppressLint("SetTextI18n")
    private fun showMeetDetails(meetInfo: MeetInfo) {
        binding.apply {
            tvTemaName.text = meetInfo.meetThemeName
            tvInvitationTitle.text = meetInfo.meetName
            tvInvitationDescription.text = meetInfo.meetDescription

            val hours = parseHourFromTimeString(meetInfo.leftInviteTime)
            tvInvitationEnd.text = "${hours}시간 뒤 초대 마감"
        }
    }

    private fun parseHourFromTimeString(timeString: String): Int {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val localTime = LocalTime.parse(timeString, formatter)
        return localTime.hour
    }

    private fun navigateToVotePromiseTimeFragment(meetId: Int) {
        InvitationLeaderFragmentDirections.actionInvitationLeaderFragmentToVotePromiseTimeFragment(
            meetId
        )
            .apply {
                findNavController().navigate(this)
            }
    }
}