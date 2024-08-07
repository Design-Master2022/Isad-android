package com.design_master.isad.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.design_master.isad.R
import com.design_master.isad.databinding.FragmentLoginWithOtpBinding
import com.design_master.isad.model.constants.Constants
import com.design_master.isad.model.view_models.activities.MainActivityViewModel
import com.design_master.isad.model.view_models.fragments.LoginWithOtpFragmentViewModel
import com.design_master.isad.model.view_models.fragments.LoginWithOtpFragmentViewModel.LoginWithOtpState
import com.design_master.isad.model.view_models.fragments.LoginWithOtpFragmentViewModel.VerifyEmailForQRCodeState
import com.design_master.isad.ui.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginWithOtpFragment : Fragment() {

    private lateinit var mBinding: FragmentLoginWithOtpBinding
    private val mViewModel: LoginWithOtpFragmentViewModel by viewModels()
    private val mMainActivityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var mMainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if (!mMainActivityViewModel.isForQrCodeFragment){
            if (mViewModel.mPrefsController.isEmailVerified()){
                mViewModel.mPrefsController.getCMEUrl()?.let { cmeUrl ->
                    mViewModel.mPrefsController.getVideoUrl()?.let { videoUrl ->
                        findNavController().navigate(LoginWithOtpFragmentDirections.actionLoginWithOtpFragmentToTabsFragment(
                            mViewModel.mMagician.decrypt(cmeUrl),
                            mViewModel.mMagician.decrypt(videoUrl)
                        ))
                    }
                }
            }
        }

        mBinding = FragmentLoginWithOtpBinding.inflate(layoutInflater)

        mMainActivity = requireActivity() as MainActivity

        mMainActivity.setActionBar(
            shouldShowDrawerMenuBtn = true,
            shouldShowNotificationBtn = true,
            shouldShowBottomNavigation = true,
            drawerMenuBtnClickListener = object: MainActivity.Companion.DrawerMenuClickListener{
                override fun onClick() {
                    mMainActivity.getMainActivityBinding().drawer.open()
                }
            },
            drawerItemsClickListener = object: MainActivity.Companion.DrawerItemsClickListener{
                override fun onHomeClick() {
                    findNavController().popBackStack(R.id.homeFragment,false)
                }
                override fun chairmanMessageClick() {
                    findNavController().navigate(WebViewFragmentDirections.actionGlobalWebViewFragment(
                        Constants.CHAIRMAN_MESSAGE_URL
                    ))
                }
                override fun onCommitteeClick() {
                    findNavController().navigate(WebViewFragmentDirections.actionGlobalWebViewFragment(
                        Constants.COMMITTEE_URL
                    ))
                }
                override fun onSpeakersClick() {
                    findNavController().navigate(R.id.action_global_speakersAndModeratorsFragment)
                }
                override fun onSponsorsClick() {
                    findNavController().navigate(WebViewFragmentDirections.actionGlobalWebViewFragment(
                        Constants.SPONSORS_URL
                    ))
                }
                override fun onEventLocationClick() {
                    findNavController().navigate(R.id.action_global_locationFragment)
                }
                override fun onHostelsAndVisaClick() {
                    findNavController().navigate(WebViewFragmentDirections.actionGlobalWebViewFragment(
                        Constants.HOTELS_AND_VISA
                    ))
                }
                override fun onAboutKuwaitClick() {
                    findNavController().navigate(WebViewFragmentDirections.actionGlobalWebViewFragment(
                        Constants.ABOUT_KUWAIT_URL
                    ))
                }
                override fun onPostersClick() {
                    findNavController().navigate(WebViewFragmentDirections.actionGlobalWebViewFragment(
                        Constants.POSTERS_URL
                    ))
                }
                override fun onFeedbackClick() {
                    findNavController().navigate(R.id.action_global_feedbackFragment)
                }
                override fun onAskQuestionClick() {
                    findNavController().navigate(R.id.action_global_askQuestionFragment)
                }
            },
            notificationBtnClickListener = object: MainActivity.Companion.NotificationClickListener{
                override fun onClick() {
                    findNavController().navigate(R.id.action_global_notificationsFragment)
                }
            }
        )

        mBinding.txtTitle.text =  if (mMainActivityViewModel.isForQrCodeFragment) getString(R.string.please_enter_you_registered_email) else getString(R.string.we_will_send_n_you_once_time_password_otp)

        mViewModel.loginWithOtpState.observe(viewLifecycleOwner){
            mBinding.txtSubmit.text = if (it == LoginWithOtpState.SENDING_OTP) getString(R.string.submitting) else getString(R.string.submit)

            if (it == LoginWithOtpState.FAILURE){
                mMainActivity.showToast(getString(R.string.failed_to_send_otp_try_again))
            }

            if (it == LoginWithOtpState.OTP_SENT){
               findNavController().navigate(LoginWithOtpFragmentDirections.actionLoginWithOtpFragmentToOtpVerifyFragment(mBinding.email.text.toString()))
            }
        }

        mViewModel.verifyEmailForQRCodeState.observe(viewLifecycleOwner){
            mBinding.txtSubmit.text = if (it == VerifyEmailForQRCodeState.VERIFYING) getString(R.string.submitting) else getString(R.string.submit)

            if (it == VerifyEmailForQRCodeState.FAILURE){
                mMainActivity.showToast(getString(R.string.failed_to_submit_try_again))
            }

            if (it == VerifyEmailForQRCodeState.VERIFIED){
                mMainActivityViewModel.isForQrCodeFragment = false
                mViewModel.mPrefsController.saveRegistrationCode(mViewModel.mMagician.encrypt(mViewModel.registrationCode))
                findNavController().navigate(LoginWithOtpFragmentDirections.actionLoginWithOtpFragmentToQrCodeFragment(mViewModel.registrationCode))
            }
        }

        mBinding.submit.setOnClickListener {
            if (validation()){
                if (mMainActivityViewModel.isForQrCodeFragment)
                    mViewModel.verifyEmailForQRCode(mBinding.email.text.toString())
                else
                    mViewModel.loginWithOtp(mBinding.email.text.toString())
            }
        }


        return mBinding.root
    }
    private fun validation(): Boolean{
        var isAllOk = false
        if (mBinding.email.text.toString().isEmpty()){
            mBinding.email.error = getString(R.string.enter_email)
            mBinding.email.requestFocus()
        }else if (!mViewModel.mHelper.isValidEmail(mBinding.email.text.toString())){
            mBinding.email.error = getString(R.string.enter_valid_email)
            mBinding.email.requestFocus()
        }else{
            isAllOk = true
        }
        return isAllOk
    }
}