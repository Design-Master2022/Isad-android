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
import com.design_master.isad.adapter.SpeakersAdapter
import com.design_master.isad.databinding.FragmentSpeakersAndModeratorsBinding
import com.design_master.isad.model.constants.Constants
import com.design_master.isad.model.view_models.fragments.SpeakersAndModeratorsFragmentViewModel
import com.design_master.isad.model.view_models.fragments.SpeakersAndModeratorsFragmentViewModel.GetAllSpeakersState
import com.design_master.isad.ui.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpeakersAndModeratorsFragment : Fragment() {

    private lateinit var mBinding: FragmentSpeakersAndModeratorsBinding
    private val mViewModel: SpeakersAndModeratorsFragmentViewModel by viewModels()
    private lateinit var mMainActivity: MainActivity
    private val mSpeakersAdapter = SpeakersAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSpeakersAndModeratorsBinding.inflate(layoutInflater)

        mMainActivity = requireActivity() as MainActivity

        mMainActivity.setActionBar(
            shouldShowDrawerMenuBtn = true,
            shouldShowNotificationBtn = true,
            shouldShowBottomNavigation = false,
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

        mBinding.recyclerSpeakers.layoutManager = LinearLayoutManager(requireContext())
        mBinding.recyclerSpeakers.adapter = mSpeakersAdapter

        mViewModel.getAllSpeakersState.observe(viewLifecycleOwner){
            mBinding.shimmerParent.visibility = if (it == GetAllSpeakersState.FETCHING) View.VISIBLE else View.GONE
            mBinding.normalParent.visibility = if (it == GetAllSpeakersState.FETCHING) View.GONE else View.VISIBLE

            if (it == GetAllSpeakersState.FAILURE) mBinding.txtStatus.text = getString(R.string.something_went_wrong_try_again)

            if (it == GetAllSpeakersState.UNAUTHORIZED) {
                // TODO CONTROL EXCEPTION
             }

            if (it == GetAllSpeakersState.FETCHED){
                mBinding.txtStatus.text = if (mViewModel.speakers.isEmpty()) getString(R.string.no_speakers_found) else ""
                mSpeakersAdapter.setData(mViewModel.speakers)
            }
        }

        mViewModel.getAllSpeakers(mViewModel.speakers.isEmpty())

        return mBinding.root
    }
}