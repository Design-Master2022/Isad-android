package com.design_master1.isad.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.design_master1.isad.R
import com.design_master1.isad.adapter.SpeakersAdapter
import com.design_master1.isad.databinding.FragmentSpeakersAndModeratorsBinding
import com.design_master1.isad.model.view_models.fragments.SpeakersAndModeratorsFragmentViewModel
import com.design_master1.isad.model.view_models.fragments.SpeakersAndModeratorsFragmentViewModel.GetAllSpeakersState
import com.design_master1.isad.ui.activities.MainActivity
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