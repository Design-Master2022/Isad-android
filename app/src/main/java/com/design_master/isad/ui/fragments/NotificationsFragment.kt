package com.design_master.isad.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.design_master.isad.R
import com.design_master.isad.adapter.NotificationsAdapter
import com.design_master.isad.databinding.FragmentNotificationsBinding
import com.design_master.isad.model.constants.Constants
import com.design_master.isad.model.view_models.activities.MainActivityViewModel
import com.design_master.isad.model.view_models.fragments.NotificationsFragmentViewModel
import com.design_master.isad.model.view_models.fragments.NotificationsFragmentViewModel.GetAllNotificationsState
import com.design_master.isad.ui.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private lateinit var mBinding: FragmentNotificationsBinding
    private val mViewModel: NotificationsFragmentViewModel by viewModels()
    private val mMainActivityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var mNotificationsAdapter: NotificationsAdapter
    private lateinit var mMainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentNotificationsBinding.inflate(layoutInflater)

        mMainActivity = requireActivity() as MainActivity

        mMainActivity.setActionBar(
            shouldShowDrawerMenuBtn = true,
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
            }
        )

        mNotificationsAdapter = NotificationsAdapter()

        mBinding.recyclerNotifications.layoutManager = LinearLayoutManager(requireContext())
        mBinding.recyclerNotifications.adapter = mNotificationsAdapter

        mViewModel.getAllNotificationsState.observe(viewLifecycleOwner){
            mBinding.shimmerParent.visibility = if (it == GetAllNotificationsState.FETCHING) View.VISIBLE else View.GONE
            mBinding.recyclerNotifications.visibility = if (it == GetAllNotificationsState.FETCHING) View.GONE else View.VISIBLE

            mBinding.txtStatus.text = if (it == GetAllNotificationsState.FAILURE) getString(R.string.something_went_wrong_try_again) else ""

            if (it == GetAllNotificationsState.FETCHED){
                mBinding.txtStatus.text = if (mViewModel.notifications.isEmpty()) getString(R.string.no_notifications) else ""
                mNotificationsAdapter.setData(mViewModel.notifications)
            }
        }

        mViewModel.getAllNotifications(mViewModel.notifications.isEmpty())

        return mBinding.root
    }
}