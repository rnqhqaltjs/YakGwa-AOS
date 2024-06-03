package com.prography.yakgwa.ui.invitation

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.ItemContent
import com.kakao.sdk.template.model.ItemInfo
import com.kakao.sdk.template.model.Link
import com.prography.yakgwa.R
import com.prography.yakgwa.databinding.FragmentInvitationLeaderBinding
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class InvitationLeaderFragment :
    BaseFragment<FragmentInvitationLeaderBinding>(R.layout.fragment_invitation_leader) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addListeners()
    }

    private fun addListeners() {
        binding.ivInvitationBtn.setOnClickListener {
            sendKakaoLink()
        }

        binding.ivNavigateUpBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun sendKakaoLink() {
        val defaultFeed = FeedTemplate(
            content = Content(
                title = "{모임 설명}",
                description = "{닉네임}님이 초대장을 보냈어요.",
                imageUrl = "http://k.kakaocdn.net/dn/bp2Qmz/btsHbRn5Auu/I4MY1Ks8YoU2npkzSr7WT0/kakaolink40_original.png",
                link = Link(
                    androidExecutionParams = mapOf("inviteId" to "okay"),
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
                        androidExecutionParams = mapOf("inviteId" to "okay"),
                        iosExecutionParams = mapOf("key1" to "value1")
                    )
                )
            )
        )

        if (ShareClient.instance.isKakaoTalkSharingAvailable(requireActivity())) {
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
        } else {
            val sharerUrl = WebSharerClient.instance.makeDefaultUrl(defaultFeed)

            try {
                KakaoCustomTabsClient.openWithDefault(requireActivity(), sharerUrl)
            } catch (e: UnsupportedOperationException) {
                // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
            }

            try {
                KakaoCustomTabsClient.open(requireActivity(), sharerUrl)
            } catch (e: ActivityNotFoundException) {
                // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
            }
        }
    }
}