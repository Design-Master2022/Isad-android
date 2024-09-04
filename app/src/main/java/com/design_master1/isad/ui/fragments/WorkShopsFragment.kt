package com.design_master1.isad.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.design_master1.isad.R
import com.design_master1.isad.adapter.DaysAdapter
import com.design_master1.isad.adapter.TracksAdapter
import com.design_master1.isad.adapter.WorkShopsAdapter
import com.design_master1.isad.databinding.FragmentWorkShopsBinding
import com.design_master1.isad.model.constants.Constants
import com.design_master1.isad.model.listeners.AddToWishlistListener
import com.design_master1.isad.model.listeners.DayListener
import com.design_master1.isad.model.listeners.DisableNotificationListener
import com.design_master1.isad.model.listeners.EnableNotificationListener
import com.design_master1.isad.model.listeners.RemoveFromWishlistListener
import com.design_master1.isad.model.listeners.TrackListener
import com.design_master1.isad.model.listeners.WorkShopListener
import com.design_master1.isad.model.network.response.EnableNotificationResponseClasses
import com.design_master1.isad.model.network.response.GetAllWorkShopsResponseClasses
import com.design_master1.isad.model.view_models.activities.MainActivityViewModel
import com.design_master1.isad.model.view_models.fragments.WorkShopsFragmentViewModel
import com.design_master1.isad.model.view_models.fragments.WorkShopsFragmentViewModel.GetAllWorkShopsState
import com.design_master1.isad.ui.activities.MainActivity
import com.design_master1.isad.ui.fragments.AddNoteFragment.Companion.FROM_WORKSHOPS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkShopsFragment : Fragment() {

    private lateinit var mBinding: FragmentWorkShopsBinding
    private lateinit var mMainActivity: MainActivity
    private lateinit var mWorkShopsAdapter: WorkShopsAdapter
    private val mViewModel: WorkShopsFragmentViewModel by viewModels()
    private val mMainActivityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var mDaysAdapter: DaysAdapter
    private lateinit var mTrackAdapter: TracksAdapter
    private var mCurrentDay = ProgramsFragment.DEFAULT_DAY
    private var mCurrentTrack = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentWorkShopsBinding.inflate(layoutInflater)

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
            notificationBtnClickListener = object: MainActivity.Companion.NotificationClickListener{
                override fun onClick() {
                    findNavController().navigate(R.id.action_global_notificationsFragment)
                }
            }
        )

        mTrackAdapter = TracksAdapter(
            context = requireContext(),
            listener = object: TrackListener {
                override fun onClick(track: String) {
                    setDayWiseWorkShops(mCurrentDay, track)
                }
            }
        )
        mBinding.recyclerTracks.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mBinding.recyclerTracks.adapter = mTrackAdapter

        mDaysAdapter = DaysAdapter(
            context = requireContext(),
            listener = object: DayListener{
                override fun onClick(day: Int) {
                    mCurrentDay = day
                    setDayWiseWorkShops(day, mCurrentTrack)
                }
            })
        mBinding.recyclerDays.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        mBinding.recyclerDays.adapter = mDaysAdapter

        mWorkShopsAdapter = WorkShopsAdapter(object: WorkShopListener{
            override fun onClick(workShop: GetAllWorkShopsResponseClasses.WorkShop) {
                mMainActivityViewModel.mSelectedWorkShop = workShop
                findNavController().navigate(WorkShopsFragmentDirections.actionWorkShopsFragmentToAddNoteFragment(FROM_WORKSHOPS))
            }
            override fun onEnableNotification(
                workShop: GetAllWorkShopsResponseClasses.WorkShop,
                listener: EnableNotificationListener
            ) {
                mMainActivityViewModel.enableNotification(
                    type = workShop.type,
                    dayWiseId = workShop.id2,
                    programOrWorkShopId = mMainActivityViewModel.programOrWorkShopId!!,
                    listener = object: EnableNotificationListener{
                        override fun onNotificationEnabled(data: EnableNotificationResponseClasses.Data) {
                            listener.onNotificationEnabled(data)
                        }
                        override fun onFailedToEnableNotification() {
                            listener.onFailedToEnableNotification()
                        }
                    }
                )
            }
            override fun onDisableNotification(
                workShop: GetAllWorkShopsResponseClasses.WorkShop,
                listener: DisableNotificationListener
            ) {
                mMainActivityViewModel.disableNotification(
                    dayWiseId = workShop.id2,
                    listener = object: DisableNotificationListener{
                        override fun onNotificationDisabled() {
                            listener.onNotificationDisabled()
                        }
                        override fun onFailedToDisableNotification() {
                            listener.onFailedToDisableNotification()
                        }
                    }
                )
            }
            override fun onAddToWishlist(
                workShop: GetAllWorkShopsResponseClasses.WorkShop,
                listener: AddToWishlistListener
            ) {
                mMainActivityViewModel.addToWishlist(
                    type = Constants.WORKSHOPS,
                    dayWiseId = workShop.id2,
                    programOrWorkShopId = mMainActivityViewModel.programOrWorkShopId!!,
                    listener = object: AddToWishlistListener{
                        override fun onAddedToWishlist() {
                            listener.onAddedToWishlist()
                        }

                        override fun onFailedToAdd() {
                            listener.onFailedToAdd()
                        }
                    }
                )
            }

            override fun onRemoveFromWishlist(
                workShop: GetAllWorkShopsResponseClasses.WorkShop,
                listener: RemoveFromWishlistListener
            ) {
                mMainActivityViewModel.removeFromWishlist(
                    dayWiseId = workShop.id2,
                    listener = object: RemoveFromWishlistListener{
                        override fun onRemovedFromWishlist() {
                            listener.onRemovedFromWishlist()
                        }
                        override fun onFailedToRemove() {
                            listener.onFailedToRemove()
                        }
                    }
                )
            }
        })
        mBinding.recyclerWorkshops.layoutManager = LinearLayoutManager(requireContext())
        mBinding.recyclerWorkshops.adapter = mWorkShopsAdapter

        mViewModel.getAllWorkShopsState.observe(viewLifecycleOwner){
            mBinding.normalParent.visibility = if (it == GetAllWorkShopsState.FETCHING) View.GONE else View.VISIBLE
            mBinding.shimmerParent.visibility = if (it == GetAllWorkShopsState.FETCHING) View.VISIBLE else View.GONE

            mBinding.txtStatus.text = if (it == GetAllWorkShopsState.FAILURE) getString(R.string.something_went_wrong_try_again) else ""

            if (it == GetAllWorkShopsState.FETCHED){
                mViewModel.workShopData?.let {
                    mMainActivityViewModel.programOrWorkShopId = it.id2
                    setDayWiseWorkShops(DEFAULT_DAY, mCurrentTrack)
                }
            }
        }

        mViewModel.getAllWorkShops(mViewModel.workShopData == null)

        return mBinding.root
    }
    private fun setDayWiseWorkShops(day: Int, track: String){
        mViewModel.workShopData?.let { data ->
            when(day){
                0 -> {
                    if (data.dayWise.day1Tracks.isEmpty()) {
                        mWorkShopsAdapter.setData(data.dayWise.day1WorkShops)
                        mTrackAdapter.setData(emptyList())
                    } else {
                        mCurrentTrack = data.dayWise.day1Tracks.first().track

                        mTrackAdapter.setData(data.dayWise.day1Tracks)
                        mWorkShopsAdapter.setData(getProgramsOfTrack(data.dayWise.day1WorkShops, track.ifEmpty { mCurrentTrack }))
                    }
                }
                1 -> {
                    if (data.dayWise.day2Tracks.isEmpty()) {
                        mWorkShopsAdapter.setData(data.dayWise.day2WorkShops)
                        mTrackAdapter.setData(emptyList())
                    } else {
                        mCurrentTrack = data.dayWise.day2Tracks.first().track
                        mTrackAdapter.setData(data.dayWise.day2Tracks)
                        mWorkShopsAdapter.setData(getProgramsOfTrack(data.dayWise.day2WorkShops, track.ifEmpty { mCurrentTrack }))
                    }
                }
                2 -> {
                    if (data.dayWise.day3Tracks.isEmpty()) {
                        mWorkShopsAdapter.setData(data.dayWise.day3WorkShops)
                        mTrackAdapter.setData(emptyList())
                    } else {
                        mCurrentTrack = data.dayWise.day3Tracks.first().track
                        mTrackAdapter.setData(data.dayWise.day3Tracks)
                        mWorkShopsAdapter.setData(getProgramsOfTrack(data.dayWise.day3WorkShops, track.ifEmpty { mCurrentTrack }))
                    }
                }
            }
        }
    }
    private fun getProgramsOfTrack(programs: List<GetAllWorkShopsResponseClasses.WorkShop>, track: String): List<GetAllWorkShopsResponseClasses.WorkShop>{
        val filteredPrograms = mutableListOf<GetAllWorkShopsResponseClasses.WorkShop>()
        for (program in programs){
            if (program.track == track){
                filteredPrograms.add(program)
            }
        }
        return filteredPrograms
    }
    companion object{
        private const val TAG = "WorkShopsFragment"
        const val DEFAULT_DAY = 0
    }
}