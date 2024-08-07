package com.design_master.isad.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.design_master.isad.R
import com.design_master.isad.adapter.SponsorAdapter
import com.design_master.isad.databinding.FragmentHomeBinding
import com.design_master.isad.extensions.hide
import com.design_master.isad.extensions.invisible
import com.design_master.isad.extensions.show
import com.design_master.isad.model.constants.Constants
import com.design_master.isad.model.listeners.SponsorListener
import com.design_master.isad.model.network.response.GetHomeDataResponseClasses
import com.design_master.isad.model.view_models.activities.MainActivityViewModel
import com.design_master.isad.model.view_models.fragments.HomeFragmentViewModel
import com.design_master.isad.model.view_models.fragments.HomeFragmentViewModel.FetchHomeDataState
import com.design_master.isad.ui.activities.MainActivity
import com.design_master.isad.utils.helper.Helper
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var mBinding: FragmentHomeBinding
    private val mViewModel: HomeFragmentViewModel by viewModels()
    private val mMainActivityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var mMainActivity: MainActivity
    private lateinit var mSponsorAdapter: SponsorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(layoutInflater)

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
            drawerItemsClickListener = object: MainActivity.Companion.DrawerItemsClickListener{
                override fun onHomeClick() {
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
            },
            notificationBtnClickListener = object: MainActivity.Companion.NotificationClickListener{
                override fun onClick() {
                    findNavController().navigate(R.id.action_global_notificationsFragment)
                }
            }
        )
        mSponsorAdapter = SponsorAdapter(
            listener = object: SponsorListener{
                override fun onClick(sponsor: GetHomeDataResponseClasses.Sponsor) {}
            }
        )
        mBinding.recyclerSponsors.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        mBinding.recyclerSponsors.adapter = mSponsorAdapter

        mViewModel.fetchHomeDataState.observe(viewLifecycleOwner){
            mBinding.normalParent.visibility = if (it == FetchHomeDataState.FETCHING) View.GONE else View.VISIBLE
            mBinding.shimmerParent.visibility = if (it == FetchHomeDataState.FETCHING) View.VISIBLE else View.GONE

            if (it == FetchHomeDataState.FAILURE){
                mBinding.normalParent.hide()
                mBinding.shimmerParent.hide()
                mBinding.txtStatus.text = getString(R.string.something_went_wrong_try_again)
            }

            if (it == FetchHomeDataState.FETCHED){
                mViewModel.homeData?.let {
                    loadData(it)
                }
            }

            mBinding.txtStatus.text = if (it == FetchHomeDataState.FAILURE) getString(R.string.something_went_wrong_try_again) else ""
        }

        mViewModel.fetchHomeData(mViewModel.homeData == null)

        mBinding.askQuestion.setOnClickListener {
            findNavController().navigate(R.id.action_global_askQuestionFragment)
        }
        mBinding.conferenceRecommendation.setOnClickListener {
            findNavController().navigate(R.id.action_global_feedbackFragment)
        }

        return mBinding.root
    }
    private fun loadData(data: GetHomeDataResponseClasses.Data){

        data.buttons1?.let {
            it.first()?.let { btn ->
                mBinding.txtRegister.text = btn.name1
                mBinding.txtSubmitAbstract.text = btn.name2

                mBinding.register.setOnClickListener {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWebViewFragment(btn.url1))
                }
                mBinding.submitAbstract.setOnClickListener {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWebViewFragment(btn.url2))
                }
            }
        }

        data.banners?.let {
            mBinding.layoutBannerSlider.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            if (it.isNotEmpty()){
                mBinding.layoutBannerSlider.layoutParams.height = (Helper.getDeviceWidth(requireActivity()) / 2)

                mBinding.bannerSlider.setImageListener { position, imageView ->
                    Helper.loadImage(
                        isCompleteURL = false,
                        url = "images/${it[position].image}",
                        imageView = imageView,
                        listener = object: Helper.LoadImageListener{
                            override fun onImageLoaded() {
                                mBinding.shimmerBannerSlider.visibility = View.GONE
                            }

                            override fun onFailedToLoadImage() {
                                mBinding.shimmerBannerSlider.visibility = View.GONE
                            }
                        }
                    )
                }
                mBinding.bannerSlider.pageCount = it.size
            }
        }

        mBinding.recyclerSponsors.visibility = if (data.sponsors.isNullOrEmpty()) View.GONE else View.VISIBLE
        data.sponsors?.let {
            mSponsorAdapter.setData(it)
        }

        data.aboutConference?.let {
            it.first()?.let {
                mBinding.conferenceTitle.text = it.title
                mBinding.conferenceMessage.text = it.message

                mBinding.conferenceImg.invisible()
                mBinding.shimmerConferenceImg.show()
                Helper.loadImage(
                    url = "settings/${it.image}",
                    isCompleteURL = false,
                    imageView = mBinding.conferenceImg,
                    listener = object: Helper.LoadImageListener{
                        override fun onImageLoaded() {
                            mBinding.conferenceImg.show()
                            mBinding.shimmerConferenceImg.invisible()
                        }
                        override fun onFailedToLoadImage() {
                            mBinding.conferenceImg.show()
                            mBinding.shimmerConferenceImg.invisible()
                        }
                    }
                )

                mBinding.conferenceReadMore.setOnClickListener {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWebViewFragment(
                        Constants.CONFERENCE_READ_MORE_URL
                    ))
                }
            }
        }

        data.about?.let {
            it.first()?.let {
                mBinding.descriptionAbout.text = it.message

                mBinding.imgAbout.invisible()
                mBinding.shimmerImgAbout.show()
                Helper.loadImage(
                    url = "settings/${it.image}",
                    isCompleteURL = false,
                    imageView = mBinding.imgAbout,
                    listener = object: Helper.LoadImageListener{
                        override fun onImageLoaded() {
                            mBinding.imgAbout.show()
                            mBinding.shimmerImgAbout.invisible()
                        }
                        override fun onFailedToLoadImage() {
                            mBinding.imgAbout.show()
                            mBinding.shimmerImgAbout.invisible()
                        }
                    }
                )
            }
        }

        data.aboutKuwait?.let {
            it.first()?.let {
                mBinding.titleAboutKuwait.text = it.title
                mBinding.descriptionAboutKuwait.text = it.message

                mBinding.imgAboutKuwait.invisible()
                mBinding.shimmerImgAboutKuwait.show()
                Helper.loadImage(
                    url = "settings/${it.image}",
                    isCompleteURL = false,
                    imageView = mBinding.imgAboutKuwait,
                    listener = object : Helper.LoadImageListener {
                        override fun onImageLoaded() {
                            mBinding.imgAboutKuwait.show()
                            mBinding.shimmerImgAboutKuwait.invisible()
                        }

                        override fun onFailedToLoadImage() {
                            mBinding.imgAboutKuwait.show()
                            mBinding.shimmerImgAboutKuwait.invisible()
                        }
                    }
                )
            }
        }

        data.info?.let {
            it.first()?.let {
                mBinding.name1.text = "${it.count1}\n${it.name1}"
                mBinding.name2.text = "${it.count2}\n${it.name2}"
                mBinding.name3.text = "${it.count3}\n${it.name3}"
                mBinding.name4.text = "${it.count4}\n${it.name4}"

                Helper.loadImage(
                    url = "info/${it.image1}",
                    imageView = mBinding.img1,
                    isCompleteURL = false,
                    listener = object: Helper.LoadImageListener{
                        override fun onImageLoaded() {}
                        override fun onFailedToLoadImage() {}
                    }
                )
                Helper.loadImage(
                    url = "info/${it.image2}",
                    imageView = mBinding.img2,
                    isCompleteURL = false,
                    listener = object: Helper.LoadImageListener{
                        override fun onImageLoaded() {}
                        override fun onFailedToLoadImage() {}
                    }
                )
                Helper.loadImage(
                    url = "info/${it.image3}",
                    imageView = mBinding.img3,
                    isCompleteURL = false,
                    listener = object: Helper.LoadImageListener{
                        override fun onImageLoaded() {}
                        override fun onFailedToLoadImage() {}
                    }
                )
                Helper.loadImage(
                    url = "info/${it.image4}",
                    imageView = mBinding.img4,
                    isCompleteURL = false,
                    listener = object: Helper.LoadImageListener{
                        override fun onImageLoaded() {}
                        override fun onFailedToLoadImage() {}
                    }
                )
            }
        }
    }
}