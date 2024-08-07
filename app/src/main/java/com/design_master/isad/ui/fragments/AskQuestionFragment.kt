package com.design_master.isad.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.design_master.isad.R
import com.design_master.isad.databinding.FragmentAskQuestionBinding
import com.design_master.isad.model.constants.Constants
import com.design_master.isad.model.view_models.fragments.QuestionViewModel
import com.design_master.isad.model.view_models.fragments.QuestionViewModel.AskQuestionState
import com.design_master.isad.ui.activities.MainActivity
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
                    findNavController().popBackStack()
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