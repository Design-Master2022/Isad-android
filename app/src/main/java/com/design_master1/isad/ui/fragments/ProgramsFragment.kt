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
import com.design_master1.isad.adapter.ProgramsAdapter
import com.design_master1.isad.adapter.TracksAdapter
import com.design_master1.isad.databinding.FragmentProgramsBinding
import com.design_master1.isad.model.constants.Constants
import com.design_master1.isad.model.listeners.AddToWishlistListener
import com.design_master1.isad.model.listeners.DayListener
import com.design_master1.isad.model.listeners.DisableNotificationListener
import com.design_master1.isad.model.listeners.EnableNotificationListener
import com.design_master1.isad.model.listeners.RemoveFromWishlistListener
import com.design_master1.isad.model.listeners.ScientificProgramListener
import com.design_master1.isad.model.listeners.TrackListener
import com.design_master1.isad.model.network.response.EnableNotificationResponseClasses
import com.design_master1.isad.model.network.response.GetAllScientificProgramsResponseClasses
import com.design_master1.isad.model.view_models.activities.MainActivityViewModel
import com.design_master1.isad.model.view_models.fragments.ProgramsFragmentViewModel
import com.design_master1.isad.model.view_models.fragments.ProgramsFragmentViewModel.GetAllScientificProgramsState
import com.design_master1.isad.ui.activities.MainActivity
import com.design_master1.isad.ui.fragments.AddNoteFragment.Companion.FROM_PROGRAMS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProgramsFragment : Fragment() {

    private lateinit var mBinding: FragmentProgramsBinding
    private lateinit var mMainActivity: MainActivity
    private val mMainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val mViewModel: ProgramsFragmentViewModel by viewModels()
    private lateinit var mProgramsAdapter: ProgramsAdapter
    private lateinit var mDaysAdapter: DaysAdapter
    private lateinit var mTrackAdapter: TracksAdapter
    private var mCurrentDay = DEFAULT_DAY
    private var mCurrentTrack = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentProgramsBinding.inflate(layoutInflater)

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
            listener = object: TrackListener{
                override fun onClick(track: String) {
                    setDayWisePrograms(mCurrentDay, track)
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
                    setDayWisePrograms(day, mCurrentTrack)
                }
            }
        )
        mBinding.recyclerDays.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        mBinding.recyclerDays.adapter = mDaysAdapter

        mProgramsAdapter = ProgramsAdapter(object: ScientificProgramListener{
            override fun onClick(program: GetAllScientificProgramsResponseClasses.ScientificProgram) {
                mMainActivityViewModel.mSelectedProgram = program
                findNavController().navigate(ProgramsFragmentDirections.actionProgramsFragmentToAddNoteFragment(FROM_PROGRAMS))
            }

            override fun onEnableNotification(
                program: GetAllScientificProgramsResponseClasses.ScientificProgram,
                listener: EnableNotificationListener
            ) {
                mMainActivityViewModel.enableNotification(
                    type = program.type,
                    dayWiseId = program.id2,
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
                program: GetAllScientificProgramsResponseClasses.ScientificProgram,
                listener: DisableNotificationListener
            ) {
                mMainActivityViewModel.disableNotification(
                    dayWiseId = program.id2,
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
                program: GetAllScientificProgramsResponseClasses.ScientificProgram,
                listener: AddToWishlistListener
            ) {
                mMainActivityViewModel.addToWishlist(
                    type = Constants.SCIENTIFIC_PROGRAMS,
                    dayWiseId = program.id2,
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
                program: GetAllScientificProgramsResponseClasses.ScientificProgram,
                listener: RemoveFromWishlistListener
            ) {
                mMainActivityViewModel.removeFromWishlist(
                    dayWiseId = program.id2,
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
        mBinding.recyclerPrograms.layoutManager = LinearLayoutManager(requireContext())
        mBinding.recyclerPrograms.adapter = mProgramsAdapter

        mViewModel.getAllScientificProgramsState.observe(viewLifecycleOwner){
            mBinding.shimmerParent.visibility = if (it == GetAllScientificProgramsState.FETCHING) View.VISIBLE else View.GONE
            mBinding.normalParent.visibility = if (it == GetAllScientificProgramsState.FETCHING) View.GONE else View.VISIBLE

            mBinding.txtStatus.text = if (it == GetAllScientificProgramsState.FAILURE) getString(R.string.something_went_wrong_try_again) else ""

            if (it == GetAllScientificProgramsState.UNAUTHORIZED) {
                // TODO CONTROL UNAUTHORIZED
            }

            if (it == GetAllScientificProgramsState.FETCHED){
                mViewModel.programsData?.let {
                    mMainActivityViewModel.programOrWorkShopId = it.id2
                    setDayWisePrograms(DEFAULT_DAY, mCurrentTrack)
                }
            }
        }

        mViewModel.getAllPrograms(mViewModel.programsData == null)

        return mBinding.root
    }
    private fun setDayWisePrograms(day: Int, track: String){
        mViewModel.programsData?.let { data ->
            when(day){
                0 -> {
                    if (data.dayWise.day1Tracks.isEmpty()) {
                        mProgramsAdapter.setData(data.dayWise.day1Programs)
                        mTrackAdapter.setData(emptyList())
                    } else {
                        mCurrentTrack = data.dayWise.day1Tracks.first().track
                        mTrackAdapter.setData(data.dayWise.day1Tracks)
                        mProgramsAdapter.setData(getProgramsOfTrack(data.dayWise.day1Programs, track.ifEmpty { mCurrentTrack }))
                    }
                }
                1 -> {
                    if (data.dayWise.day2Tracks.isEmpty()) {
                        mProgramsAdapter.setData(data.dayWise.day2Programs)
                        mTrackAdapter.setData(emptyList())
                    } else {
                        mCurrentTrack = data.dayWise.day2Tracks.first().track
                        mTrackAdapter.setData(data.dayWise.day2Tracks)
                        mProgramsAdapter.setData(getProgramsOfTrack(data.dayWise.day2Programs, track.ifEmpty { mCurrentTrack }))
                    }
                }
                2 -> {
                    if (data.dayWise.day3Tracks.isEmpty()) {
                        mProgramsAdapter.setData(data.dayWise.day3Programs)
                        mTrackAdapter.setData(emptyList())
                    } else {
                        mCurrentTrack = data.dayWise.day3Tracks.first().track
                        mTrackAdapter.setData(data.dayWise.day3Tracks)
                        mProgramsAdapter.setData(getProgramsOfTrack(data.dayWise.day3Programs, track.ifEmpty { mCurrentTrack }))
                    }
                }
            }
        }
    }
    private fun getProgramsOfTrack(programs: List<GetAllScientificProgramsResponseClasses.ScientificProgram>, track: String): List<GetAllScientificProgramsResponseClasses.ScientificProgram>{
        val filteredPrograms = mutableListOf<GetAllScientificProgramsResponseClasses.ScientificProgram>()
        for (program in programs){
            if (program.track == track){
                filteredPrograms.add(program)
            }
        }
        return filteredPrograms
    }
    companion object{
        const val DEFAULT_DAY = 0
    }
//                        val programs = mutableListOf<GetAllScientificProgramsResponseClasses.ScientificProgram>()
//                        val trackWithPrograms = mutableListOf<TrackWithPrograms>()
//                        for (track in data.dayWise.day1Tracks){
//                            programs.clear()
//                            for (program in data.dayWise.day1Programs){
//                                if (if(program.track == null) "1" == track.track else program.track == track.track){
//                                    programs.add(program)
//                                }
//                            }
//                            trackWithPrograms.add(
//                                TrackWithPrograms(
//                                    track = track,
//                                    programs = programs
//                                )
//                            )
//                        }
//                        mDaysAdapter.setTracks(data.dayWise.day1Tracks)
//                        mProgramsAdapter.setData(trackWithPrograms.first().programs)
}