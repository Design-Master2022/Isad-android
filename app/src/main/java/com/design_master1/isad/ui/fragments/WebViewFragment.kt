package com.design_master1.isad.ui.fragments

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.design_master1.isad.databinding.FragmentWebViewBinding
import com.design_master1.isad.extensions.hide
import com.design_master1.isad.extensions.show
import com.design_master1.isad.model.listeners.WebURLListener
import com.design_master1.isad.model.view_models.activities.MainActivityViewModel
import com.design_master1.isad.ui.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WebViewFragment : Fragment() {

    private lateinit var mBinding: FragmentWebViewBinding
    private lateinit var mMainActivity: MainActivity
    private val mMainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val mArgs: WebViewFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentWebViewBinding.inflate(layoutInflater)

        mMainActivity = requireActivity() as MainActivity

        mMainActivity.setActionBar(
            shouldShowDrawerMenuBtn = true,
            shouldShowBottomNavigation = true
        )

        mBinding.webview.settings.javaScriptEnabled = true
        mBinding.webview.settings.domStorageEnabled = true
        mBinding.webview.settings.allowFileAccess = true
        mBinding.webview.settings.setGeolocationEnabled(true)
        mBinding.webview.settings.allowContentAccess = true
        mBinding.webview.settings.mediaPlaybackRequiresUserGesture = false
        mBinding.webview.settings.setSupportZoom(false)
        mBinding.webview.setBackgroundColor(Color.TRANSPARENT)
        mBinding.webview.webViewClient = MyBrowser(object: WebURLListener{
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                Log.d(TAG, "shouldOverrideUrlLoading: ${request?.url}")
                request?.let {
                    it.url?.let {
                        if (it.toString().contains("whatsapp") ||
                            it.toString().contains("mailto")
//                            it.toString().contains("intent:")
                            ){
                            mMainActivity.openLink(it.toString())
                            return true
                        } else if (it.toString().contains("intent://maps.app.goo")){
                            mMainActivity.openLink(it.getQueryParameter("link")?:"")
                            return true
                        } else {
                            mBinding.progress.show()
                            return false
                        }
                    }
                }
                return false
            }
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                mBinding.progress.show()
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                mBinding.progress.hide()
            }
        })
        mBinding.webview.setWebChromeClient(object : WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(
                origin: String,
                callback: GeolocationPermissions.Callback
            ) {
                callback.invoke(origin, true, false)
            }
        })

        mMainActivity.onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (mBinding.webview.canGoBack())
                    mBinding.webview.goBack()
                else
                    findNavController().popBackStack()
            }
        })

        mBinding.progress.visibility = View.VISIBLE
        mBinding.webview.loadUrl(mArgs.url)

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
    companion object{
        private const val TAG = "WebViewFragment"
    }
}
private class MyBrowser(val listener: WebURLListener): WebViewClient(){
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return listener.shouldOverrideUrlLoading(view,request)
    }
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        listener.onPageStarted(view,url,favicon)
    }
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        listener.onPageFinished(view, url)
    }
}