package com.design_master.isad.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.design_master.isad.R
import com.design_master.isad.adapter.QuestionsAdapter
import com.design_master.isad.databinding.FragmentMyQuestionsBinding
import com.design_master.isad.model.constants.Constants
import com.design_master.isad.model.view_models.fragments.QuestionViewModel
import com.design_master.isad.model.view_models.fragments.QuestionViewModel.FetchQuestionsState
import com.design_master.isad.ui.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyQuestionsFragment : Fragment() {

    private lateinit var mBinding: FragmentMyQuestionsBinding
    private lateinit var mMainActivity: MainActivity
    private lateinit var mQuestionsAdapter: QuestionsAdapter
    private val mViewModel: QuestionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMyQuestionsBinding.inflate(layoutInflater)

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

        mQuestionsAdapter = QuestionsAdapter()
        mBinding.recyclerQuestions.layoutManager = LinearLayoutManager(requireContext())
        mBinding.recyclerQuestions.adapter = mQuestionsAdapter

        mViewModel.fetchQuestionsState.observe(viewLifecycleOwner){
            mBinding.recyclerQuestions.visibility = if (it == FetchQuestionsState.FETCHING) View.GONE else View.VISIBLE
            mBinding.shimmerParent.visibility = if (it == FetchQuestionsState.FETCHING) View.VISIBLE else View.GONE

            mBinding.txtStatus.text = if (it == FetchQuestionsState.FAILURE) getString(R.string.failed_to_fetch_questions) else ""


            if (it == FetchQuestionsState.FETCHED) {
                mBinding.txtStatus.text = if (mViewModel.questions.isEmpty()) getString(R.string.no_questions_found) else ""
                mQuestionsAdapter.setData(mViewModel.questions)
            }
        }

        if (mViewModel.questions.isEmpty()) mViewModel.fetchQuestions()
        else mQuestionsAdapter.setData(mViewModel.questions)

        return mBinding.root
    }
    companion object {
        private const val TAG = "MyQuestionsFragment"
    }
}