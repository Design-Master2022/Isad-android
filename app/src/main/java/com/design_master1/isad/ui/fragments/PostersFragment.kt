package com.design_master1.isad.ui.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.navigation.fragment.findNavController
import com.design_master1.isad.R
import com.design_master1.isad.databinding.FragmentPostersBinding
import com.design_master1.isad.extensions.hide
import com.design_master1.isad.extensions.show
import com.design_master1.isad.model.listeners.ActivityResultListener
import com.design_master1.isad.model.listeners.BackPressedListener
import com.design_master1.isad.ui.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import im.delight.android.webview.AdvancedWebView

@AndroidEntryPoint
class PostersFragment : Fragment() {

    private lateinit var mBinding: FragmentPostersBinding
    private lateinit var mMainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentPostersBinding.inflate(layoutInflater)

        mMainActivity = requireActivity() as MainActivity

        mMainActivity.setActionBar(
            shouldShowDrawerMenuBtn = true,
            shouldShowBottomNavigation = true,
            drawerMenuBtnClickListener = object: MainActivity.Companion.DrawerMenuClickListener{
                override fun onClick() {
                    mMainActivity.getMainActivityBinding().drawer.open()
                }
            }
        )

        mBinding.webview.setListener(requireActivity(), object: AdvancedWebView.Listener{
            override fun onPageStarted(url: String?, favicon: Bitmap?) {
                mBinding.progress.show()
            }
            override fun onPageFinished(url: String?) {
                mBinding.progress.hide()
            }
            override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {}
            override fun onDownloadRequested(
                url: String?,
                suggestedFilename: String?,
                mimeType: String?,
                contentLength: Long,
                contentDisposition: String?,
                userAgent: String?
            ) {}
            override fun onExternalPageRequest(url: String?) {}
        })
        mBinding.webview.setGeolocationEnabled(true)
        mBinding.webview.settings.javaScriptEnabled = true
        mBinding.webview.addHttpHeader("X-Requested-With", getString(R.string.app_name))
//        mBinding.webview.addPermittedHostname(mMainActivityViewModel.mElectroLibAccessProvider.getServerBaseUrl())
        mBinding.webview.setMixedContentAllowed(true)
        mBinding.webview.setDesktopMode(false)
        mBinding.webview.settings.setSupportMultipleWindows(true)
        mBinding.webview.settings.setSupportZoom(false)
        mBinding.webview.webChromeClient = object: WebChromeClient(){
            override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message?
            ): Boolean {
                val advancedWebView = AdvancedWebView(requireContext())
                val transport = resultMsg?.obj as WebView.WebViewTransport
                transport.webView = advancedWebView
                resultMsg.sendToTarget()
                return true
            }
        }

        mMainActivity.initializeActivityResultListener(object: ActivityResultListener {
            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                mBinding.webview.onActivityResult(requestCode, resultCode, data)
            }
        })
        mMainActivity.initializeBackPressedListener(object: BackPressedListener {
            override fun onBackPressed() {
                if (mBinding.webview.canGoBack())
                    mBinding.webview.goBack()
                else
                    findNavController().popBackStack()
            }
        })

        mBinding.webview.loadUrl("https://doha2024.isad.org/?get=app&page=posters")
        mBinding.progress.show()

        return mBinding.root
    }
    override fun onResume() {
        super.onResume()
        mBinding.webview.onResume()
    }
    override fun onPause() {
        mBinding.webview.onPause()
        super.onPause()
    }
    override fun onDestroy() {
        mBinding.webview.onDestroy()
        super.onDestroy()
    }
}