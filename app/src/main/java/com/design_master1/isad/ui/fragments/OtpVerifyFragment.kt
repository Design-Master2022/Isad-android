package com.design_master1.isad.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.design_master1.isad.R
import com.design_master1.isad.databinding.FragmentOtpVerifyBinding
import com.design_master1.isad.model.view_models.activities.MainActivityViewModel
import com.design_master1.isad.model.view_models.fragments.LoginWithOtpFragmentViewModel
import com.design_master1.isad.model.view_models.fragments.LoginWithOtpFragmentViewModel.LoginWithOtpState
import com.design_master1.isad.model.view_models.fragments.LoginWithOtpFragmentViewModel.OtpVerifyState
import com.design_master1.isad.ui.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpVerifyFragment : Fragment() {

    private lateinit var mBinding: FragmentOtpVerifyBinding
    private val mViewModel: LoginWithOtpFragmentViewModel by viewModels()
    private val mMainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val mArgs: OtpVerifyFragmentArgs by navArgs()
    private lateinit var mMainActivity: MainActivity
    private lateinit var mOtpBoxes: Array<EditText>
    private lateinit var mReversOtpBoxes: Array<EditText>
    private lateinit var mCountDownListener: CountDownListener
    private var isAnyBoxEmpty = true
    private var code = ""
    private var countdownTime = COUNTDOWN_TIME
    private var shouldListen = false
    private val mHandler = Handler()
    private lateinit var mRunnable: Runnable
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentOtpVerifyBinding.inflate(layoutInflater)

        mMainActivity = requireActivity() as MainActivity

        mMainActivity.setActionBar(
            shouldShowBackBtn = true,
            shouldShowBottomNavigation = false,
            backBtnClickListener = object: MainActivity.Companion.BackBtnClickListener{
                override fun onClick() {
                    findNavController().popBackStack()
                }
            }
        )

        mOtpBoxes = arrayOf(
            mBinding.otpCode1,
            mBinding.otpCode2,
            mBinding.otpCode3,
            mBinding.otpCode4
        )

        mReversOtpBoxes = arrayOf(
            mBinding.otpCode4,
            mBinding.otpCode3,
            mBinding.otpCode2,
            mBinding.otpCode1
        )

        mBinding.txtEmail.text = getString(R.string.we_have_sent_otp_at, mArgs.email.take(3), mArgs.email.takeLast(4))

        mBinding.otpCode1.addTextChangedListener { if (it.toString().isNotEmpty()) focusIfAnyEmpty() }
        mBinding.otpCode2.addTextChangedListener { if (it.toString().isNotEmpty()) focusIfAnyEmpty() }
        mBinding.otpCode3.addTextChangedListener { if (it.toString().isNotEmpty()) focusIfAnyEmpty() }
        mBinding.otpCode4.addTextChangedListener { if (it.toString().isNotEmpty()) {mViewModel.mHelper.hideKeyBoard(requireContext(),requireView().findFocus()); focusIfAnyEmpty()} }

        mBinding.otpCode2.setOnKeyListener { v, keyCode, event -> if (keyCode == KeyEvent.KEYCODE_DEL) if (mBinding.otpCode2.text.toString().trim().isEmpty()) controlBackPress(); return@setOnKeyListener false }
        mBinding.otpCode3.setOnKeyListener { v, keyCode, event -> if (keyCode == KeyEvent.KEYCODE_DEL) if (mBinding.otpCode3.text.toString().trim().isEmpty()) controlBackPress(); return@setOnKeyListener false }
        mBinding.otpCode4.setOnKeyListener { v, keyCode, event -> if (keyCode == KeyEvent.KEYCODE_DEL) if (mBinding.otpCode4.text.toString().trim().isEmpty()) controlBackPress(); return@setOnKeyListener false }

        mCountDownListener = object: CountDownListener{
            override fun onCountDownStart() {
                mBinding.resend.isEnabled = false
            }

            override fun onCountDownEnd() {
                shouldListen = false
                mBinding.resend.isEnabled = true
                mBinding.resend.text = getString(R.string.resend)
            }

            override fun onTick(currentTime: Int) {
                mBinding.resend.text = getString(R.string.resend_in,"00:${if (currentTime.toString().length == 2) currentTime else "0$currentTime"}")
            }
        }

        mViewModel.otpVerifyState.observe(viewLifecycleOwner){
            mBinding.txtSubmit.text = if (it == OtpVerifyState.VERIFYING) getString(R.string.submitting) else getString(R.string.submit)

            if (it == OtpVerifyState.FAILURE) mMainActivity.showToast(getString(R.string.failed_to_submit_try_again))

            if (it == OtpVerifyState.WRONG_OTP) mMainActivity.showToast(getString(R.string.invalid_otp))

            if (it == OtpVerifyState.VERIFIED) {
                mViewModel.mPrefsController.saveEmailVerified(isVerified = true)
                mViewModel.mPrefsController.saveVideoUrl(mViewModel.mMagician.encrypt(mViewModel.videoUrl))
                mViewModel.mPrefsController.saveCMEUrl(mViewModel.mMagician.encrypt(mViewModel.cmeUrl))

                mMainActivity.showToast(getString(R.string.otp_verified))
                findNavController().navigate(OtpVerifyFragmentDirections.actionOtpVerifyFragmentToTabsFragment(mViewModel.cmeUrl, mViewModel.videoUrl))

            }
        }

        mViewModel.loginWithOtpState.observe(viewLifecycleOwner){
            mBinding.resend.text = if (it == LoginWithOtpState.SENDING_OTP) getString(R.string.sending) else getString(R.string.resend)

            if (it == LoginWithOtpState.FAILURE){
                mMainActivity.showToast(getString(R.string.failed_to_send_otp_try_again))
            }

            if (it == LoginWithOtpState.OTP_SENT){
                startCountdown(mCountDownListener)
            }
        }

        startCountdown(mCountDownListener)

        mBinding.submit.setOnClickListener {
            mViewModel.verifyOtp(email = mArgs.email, otp = code)
        }

        mBinding.resend.setOnClickListener {
            mViewModel.loginWithOtp(mArgs.email)
        }

        return mBinding.root
    }
    private fun focusIfAnyEmpty(): Boolean{
        code = ""
        isAnyBoxEmpty = false
        for (otpBox in mOtpBoxes) {
            code += otpBox.text.toString().trim()
            if (otpBox.text.toString().trim().isEmpty()) {
                otpBox.requestFocus()
                isAnyBoxEmpty = true
                break
            }
            isAnyBoxEmpty = false
        }
        return isAnyBoxEmpty
    }
    private fun controlBackPress(): Boolean{
        var isAnyBoxNotEmpty = false
        for (otpBox in mReversOtpBoxes) {
            if (otpBox.text.toString().trim().isNotEmpty()) {
                otpBox.requestFocus()
                isAnyBoxNotEmpty = true
                break
            }
            isAnyBoxNotEmpty = false
        }
        return isAnyBoxNotEmpty
    }

    private fun startCountdown(listener: CountDownListener){
        shouldListen = true

        mRunnable = Runnable {
            if (shouldListen){
                if (countdownTime == COUNTDOWN_TIME) listener.onCountDownStart()
                listener.onTick(currentTime = countdownTime)

                if (countdownTime == 0) {
                    listener.onCountDownEnd()
                    countdownTime = COUNTDOWN_TIME
                } else {
                    countdownTime = countdownTime.minus(1)
                    mHandler.postDelayed(mRunnable,1000)
                }
            }
        }
        mHandler.postDelayed(mRunnable,1000)
    }
    interface CountDownListener{
        fun onCountDownStart()
        fun onCountDownEnd()
        fun onTick(currentTime: Int)
    }
    private fun enableSubmitBtn(enable: Boolean){
        mBinding.submit.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                if (enable) R.color.primary else R.color.disabledPrimaryColor
            )
        )

        mBinding.txtSubmit.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (enable) R.color.white else R.color.disabledWhiteColor
            )
        )
    }
    companion object{
        private const val TAG = "OtpVerifyFragment"
        private const val COUNTDOWN_TIME = 30
    }

}