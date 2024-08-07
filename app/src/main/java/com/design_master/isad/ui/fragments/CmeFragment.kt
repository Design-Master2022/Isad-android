package com.design_master.isad.ui.fragments

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
import androidx.fragment.app.activityViewModels
import com.design_master.isad.R
import com.design_master.isad.databinding.FragmentCmeBinding
import com.design_master.isad.model.listeners.ActivityResultListener
import com.design_master.isad.model.listeners.BackPressedListener
import com.design_master.isad.model.view_models.activities.MainActivityViewModel
import com.design_master.isad.ui.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import im.delight.android.webview.AdvancedWebView

@AndroidEntryPoint
class CmeFragment : Fragment() {

    private lateinit var mBinding: FragmentCmeBinding
    private lateinit var mMainActivity: MainActivity
    private val mMainActivityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentCmeBinding.inflate(layoutInflater)

        mMainActivity = requireActivity() as MainActivity

        mMainActivity.setActionBar(
            shouldShowActionBar = false
        )

        mBinding.webview.setListener(requireActivity(), object: AdvancedWebView.Listener{
            override fun onPageStarted(url: String?, favicon: Bitmap?) {

            }

            override fun onPageFinished(url: String?) {

            }

            override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {

            }

            override fun onDownloadRequested(
                url: String?,
                suggestedFilename: String?,
                mimeType: String?,
                contentLength: Long,
                contentDisposition: String?,
                userAgent: String?
            ) {
                if (AdvancedWebView.handleDownload(requireContext(),url,suggestedFilename)){}
                else {}
            }

            override fun onExternalPageRequest(url: String?) {
//                Log.d(TAG, "onExternalPageRequest: $url")
            }
        })
        mBinding.webview.setGeolocationEnabled(true)
        mBinding.webview.settings.javaScriptEnabled = true
        mBinding.webview.addHttpHeader("X-Requested-With", getString(R.string.app_name))
        mBinding.webview.addPermittedHostname(mMainActivityViewModel.mElectroLibAccessProvider.getServerBaseUrl())
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

            }
        })

        mBinding.webview.loadUrl(mMainActivityViewModel.cmeUrl)

        return mBinding.root
    }
}