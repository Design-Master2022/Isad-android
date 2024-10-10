package com.design_master1.isad.ui.fragments

import android.os.Build
import android.os.Bundle
import android.text.Html
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
import com.design_master1.isad.extensions.hide
import com.design_master1.isad.extensions.invisible
import com.design_master1.isad.extensions.show
import com.design_master1.isad.model.view_models.fragments.SpeakersAndModeratorsFragmentViewModel
import com.design_master1.isad.model.view_models.fragments.SpeakersAndModeratorsFragmentViewModel.Companion.CHAIRMAN
import com.design_master1.isad.model.view_models.fragments.SpeakersAndModeratorsFragmentViewModel.Companion.SPEAKER_TYPE_INTERNATIONAL
import com.design_master1.isad.model.view_models.fragments.SpeakersAndModeratorsFragmentViewModel.Companion.SPEAKER_TYPE_LOCAL
import com.design_master1.isad.model.view_models.fragments.SpeakersAndModeratorsFragmentViewModel.GetAllSpeakersState
import com.design_master1.isad.ui.activities.MainActivity
import com.design_master1.isad.utils.helper.Helper
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

        mBinding.recyclerSpeakers.layoutManager = LinearLayoutManager(requireContext())
        mBinding.recyclerSpeakers.adapter = mSpeakersAdapter

        mViewModel.getAllSpeakersState.observe(viewLifecycleOwner){
            mBinding.shimmerParent.visibility = if (it == GetAllSpeakersState.FETCHING) View.VISIBLE else View.GONE
            mBinding.normalParent.visibility = if (it == GetAllSpeakersState.FETCHING) View.GONE else View.VISIBLE

            if (it == GetAllSpeakersState.FAILURE) mBinding.txtStatus.text = getString(R.string.something_went_wrong_try_again)

            if (it == GetAllSpeakersState.FETCHED){
                if (mViewModel.type == SPEAKER_TYPE_LOCAL){
                    mBinding.txtStatus.text = if (mViewModel.localSpeakers.isEmpty()) getString(R.string.no_speakers_found) else ""
                    mSpeakersAdapter.setData(mViewModel.localSpeakers)
                }else if (mViewModel.type == SPEAKER_TYPE_INTERNATIONAL){
                    mBinding.txtStatus.text = if (mViewModel.internationalSpeakers.isEmpty()) getString(R.string.no_speakers_found) else ""
                    mSpeakersAdapter.setData(mViewModel.internationalSpeakers)
                }else{
                    setChairmanData()
                }
            }
        }

        mViewModel.getAllSpeakers(mViewModel.localSpeakers.isEmpty(), SPEAKER_TYPE_LOCAL)
        mViewModel.getAllSpeakers(mViewModel.chairman.isEmpty(), CHAIRMAN)
        handleBtnUI(true)

        mBinding.localBtn.setOnClickListener {
            handleBtnUI(true)
            if (mViewModel.localSpeakers.isEmpty())
                mViewModel.getAllSpeakers(false, SPEAKER_TYPE_LOCAL)
            else
                mSpeakersAdapter.setData(mViewModel.localSpeakers)
        }

        mBinding.internationalBtn.setOnClickListener {
            handleBtnUI(false)
            if (mViewModel.internationalSpeakers.isEmpty())
                mViewModel.getAllSpeakers(false, SPEAKER_TYPE_INTERNATIONAL)
            else
                mSpeakersAdapter.setData(mViewModel.internationalSpeakers)
        }

        return mBinding.root
    }

    private fun handleBtnUI(isLocalSelected: Boolean){
        mBinding.localBtn.setBackgroundResource(if (isLocalSelected) R.drawable.speaker_btn_back_selected else R.drawable.speaker_btn_back_normal)
        mBinding.internationalBtn.setBackgroundResource(if (isLocalSelected) R.drawable.speaker_btn_back_normal else R.drawable.speaker_btn_back_selected)

        mBinding.localBtn.setTextColor(if (isLocalSelected) resources.getColor(R.color.whiteTextColor) else resources.getColor(R.color.primaryTextColor))
        mBinding.internationalBtn.setTextColor(if (isLocalSelected) resources.getColor(R.color.primaryTextColor) else resources.getColor(R.color.whiteTextColor))
    }

    private fun setChairmanData(){
        mBinding.name.text = mViewModel.chairman.first().name

        mBinding.img.invisible()
        mBinding.shimmerImg.show()
        Helper.loadImage(
            url = "committe/${mViewModel.chairman.first().image}",
            imageView = mBinding.img,
            isCompleteURL = false,
            listener = object: Helper.LoadImageListener{
                override fun onImageLoaded() {
                    mBinding.img.show()
                    mBinding.shimmerImg.hide()
                }

                override fun onFailedToLoadImage() {
                    mBinding.img.show()
                    mBinding.shimmerImg.hide()
                }
            }
        )
        if (mViewModel.chairman.isNotEmpty()){
            mBinding.imgFlag.invisible()
            mBinding.shimmerImgFlag.show()
            Helper.loadImage(
                url = "https://flagcdn.com/48x36/${mViewModel.chairman.first().country.first().countryCode.toLowerCase()}.png",
                imageView = mBinding.imgFlag,
                isCompleteURL = true,
                listener = object: Helper.LoadImageListener{
                    override fun onImageLoaded() {
                        mBinding.imgFlag.show()
                        mBinding.shimmerImgFlag.hide()
                    }
                    override fun onFailedToLoadImage() {
                        mBinding.imgFlag.show()
                        mBinding.shimmerImgFlag.hide()
                    }
                }
            )
        }
    }
}