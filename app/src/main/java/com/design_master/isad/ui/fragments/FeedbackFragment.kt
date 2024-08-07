package com.design_master.isad.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.design_master.isad.R
import com.design_master.isad.databinding.FragmentFeedbackBinding
import com.design_master.isad.model.constants.Constants
import com.design_master.isad.model.view_models.fragments.FeedbackFragmentViewModel
import com.design_master.isad.model.view_models.fragments.FeedbackFragmentViewModel.AddFeedbackState
import com.design_master.isad.ui.activities.MainActivity
import com.design_master.isad.utils.helper.Helper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedbackFragment : Fragment() {

    private lateinit var mBinding: FragmentFeedbackBinding
    private lateinit var mMainActivity: MainActivity
    private val mViewModel: FeedbackFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentFeedbackBinding.inflate(layoutInflater)

        mMainActivity = requireActivity() as MainActivity

        mMainActivity.setActionBar(
            shouldShowDrawerMenuBtn = true,
            shouldShowNotificationBtn = true,
            drawerMenuBtnClickListener = object: MainActivity.Companion.DrawerMenuClickListener{
                override fun onClick() {
                    mMainActivity.getMainActivityBinding().drawer.open()
                }
            },
            notificationBtnClickListener = object: MainActivity.Companion.NotificationClickListener{
                override fun onClick() {
                    findNavController().navigate(R.id.action_global_notificationsFragment)
                }
            }
        )

        mViewModel.addFeedbackState.observe(viewLifecycleOwner){
            mBinding.progress.visibility = if (it == AddFeedbackState.ADDING) View.VISIBLE else View.GONE
            mBinding.submit.isClickable = it != AddFeedbackState.ADDING

            if (it == AddFeedbackState.FAILURE) mMainActivity.showToast(getString(R.string.something_went_wrong_try_again))

            if (it == AddFeedbackState.ADDED) {
                mBinding.fullName.setText("")
                mBinding.fullName.requestFocus()
                mBinding.mobileNumber.setText("")
                mBinding.email.setText("")
                mBinding.message.setText("")
                mMainActivity.showToast("Feedback Sent Successfully.")
            }
        }

        mBinding.submit.setOnClickListener {
            if (validation()){
                mViewModel.addFeedback(
                    mBinding.fullName.text.toString(),
                    mBinding.mobileNumber.text.toString(),
                    mBinding.email.text.toString(),
                    mBinding.message.text.toString()
                )
            }
        }

        return mBinding.root
    }

    private fun validation(): Boolean{
        var isAllOk = false
        if (mBinding.fullName.text.toString().isEmpty()){
            mBinding.fullName.error = getString(R.string.field_required)
            mBinding.fullName.requestFocus()
        } else if (mBinding.mobileNumber.text.toString().isEmpty()){
            mBinding.mobileNumber.error = getString(R.string.field_required)
            mBinding.mobileNumber.requestFocus()
        } else if (mBinding.email.text.toString().isEmpty()){
            mBinding.email.error = getString(R.string.field_required)
            mBinding.email.requestFocus()
        } else if (!Helper().isValidEmail(mBinding.email.text.toString())){
            mBinding.email.error = getString(R.string.enter_valid_email)
            mBinding.email.requestFocus()
        } else if (mBinding.message.text.toString().isEmpty()){
            mBinding.message.error = getString(R.string.field_required)
            mBinding.message.requestFocus()
        } else{
            isAllOk = true
        }
        return isAllOk
    }

    companion object {
        private const val TAG = "FeedbackFragment"
    }
}