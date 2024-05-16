package com.prography.yakgwa.ui.home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.prography.yakgwa.databinding.FragmentHomeBinding
import com.prography.yakgwa.util.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intent = activity?.intent
        if (Intent.ACTION_VIEW == intent?.action) {
            val uri = intent.data
            if (uri != null) {
                val abc = uri.getQueryParameter("inviteId")
                Log.d("url scheme parameter",abc!!)
            }
        }

        binding.textview.setOnClickListener {
            sendKakaoLink()
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
            // 카카오톡으로 카카오톡 공유 가능
            ShareClient.instance.shareDefault(requireActivity(), defaultFeed) { sharingResult, error ->
                if (error != null) {
                    Timber.e( "카카오톡 공유 실패", error)
                }
                else if (sharingResult != null) {
                    Timber.d( "카카오톡 공유 성공 ${sharingResult.intent}")
                    startActivity(sharingResult.intent)

                    // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                    Timber.w( "Warning Msg: ${sharingResult.warningMsg}")
                    Timber.w("Argument Msg: ${sharingResult.argumentMsg}")
                }
            }
        } else {
            // 카카오톡 미설치: 웹 공유 사용 권장
            // 웹 공유 예시 코드
            val sharerUrl = WebSharerClient.instance.makeDefaultUrl(defaultFeed)

            // CustomTabs으로 웹 브라우저 열기

            // 1. CustomTabsServiceConnection 지원 브라우저 열기
            // ex) Chrome, 삼성 인터넷, FireFox, 웨일 등
            try {
                KakaoCustomTabsClient.openWithDefault(requireActivity(), sharerUrl)
            } catch(e: UnsupportedOperationException) {
                // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
            }

            // 2. CustomTabsServiceConnection 미지원 브라우저 열기
            // ex) 다음, 네이버 등
            try {
                KakaoCustomTabsClient.open(requireActivity(), sharerUrl)
            } catch (e: ActivityNotFoundException) {
                // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
            }
        }
    }
}