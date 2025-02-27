package com.design_master1.isad.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.design_master1.isad.R
import com.design_master1.isad.databinding.FragmentAskQuestionBinding
import com.design_master1.isad.model.view_models.fragments.QuestionViewModel
import com.design_master1.isad.model.view_models.fragments.QuestionViewModel.AskQuestionState
import com.design_master1.isad.ui.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AskQuestionFragment : Fragment() {

    private lateinit var mBinding: FragmentAskQuestionBinding
    private lateinit var mMainActivity: MainActivity
    private val mViewModel: QuestionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAskQuestionBinding.inflate(layoutInflater)

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

        mViewModel.askQuestionState.observe(viewLifecycleOwner){
            mBinding.progress.visibility = if (it == AskQuestionState.ASKING) View.VISIBLE else View.GONE
            mBinding.submit.isClickable = it != AskQuestionState.ASKING

            if (it == AskQuestionState.FAILURE) mMainActivity.showToast(getString(R.string.something_went_wrong_try_again))

            if (it == AskQuestionState.ASKED) {
                mBinding.fullName.setText("")
                mBinding.fullName.requestFocus()
                mBinding.sessionName.setText("")
                mBinding.question.setText("")
                mMainActivity.showToast("Question Sent Successfully.")
            }
        }

        mBinding.submit.setOnClickListener {
            if (validation()){
                mViewModel.askQuestion(
                    mBinding.fullName.text.toString(),
                    mBinding.sessionName.text.toString(),
                    mBinding.question.text.toString()
                )
            }
        }

        mBinding.myQuestions.setOnClickListener{
            findNavController().navigate(R.id.action_askQuestionFragment_to_myQuestionsFragment)
        }

        return mBinding.root
    }
    private fun validation(): Boolean{
        var isAllOk = false
        if (mBinding.fullName.text.toString().isEmpty()){
            mBinding.fullName.error = getString(R.string.field_required)
            mBinding.fullName.requestFocus()
        } else if (mBinding.sessionName.text.toString().isEmpty()){
            mBinding.sessionName.error = getString(R.string.field_required)
            mBinding.sessionName.requestFocus()
        } else if (mBinding.question.text.toString().isEmpty()){
            mBinding.question.error = getString(R.string.field_required)
            mBinding.question.requestFocus()
        } else{
            isAllOk = true
        }
        return isAllOk
    }
    companion object {
        private const val TAG = "AskQuestionFragment"
    }
}