package com.design_master.isad.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.design_master.isad.R
import com.design_master.isad.databinding.FragmentHomeBinding
import com.design_master.isad.extensions.hide
import com.design_master.isad.extensions.invisible
import com.design_master.isad.extensions.show
import com.design_master.isad.model.constants.Constants
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
            notificationBtnClickListener = object: MainActivity.Companion.NotificationClickListener{
                override fun onClick() {
                    findNavController().navigate(R.id.action_global_notificationsFragment)
                }
            }
        )

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

//        mBinding.askQuestion.setOnClickListener {
//            findNavController().navigate(R.id.action_global_askQuestionFragment)
//        }
//        mBinding.conferenceRecommendation.setOnClickListener {
//            findNavController().navigate(R.id.action_global_feedbackFragment)
//        }

        return mBinding.root
    }
    private fun loadData(data: GetHomeDataResponseClasses.Data){

        data.banners?.let {
            mBinding.layoutBannerSlider.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            if (it.isNotEmpty()){
                mBinding.layoutBannerSlider.layoutParams.height = (Helper.getDeviceWidth(requireActivity()) / 2)

                mBinding.bannerSlider.setImageListener { position, imageView ->
                    Helper.loadImage(
                        isCompleteURL = false,
                        url = "home_banner/${it[position].logo}",
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

        data.buttons?.let {
            it.first()?.let { btn ->
                mBinding.txtRegister.text = btn.name

                mBinding.register.setOnClickListener {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWebViewFragment(btn.url))
                }
            }

            it[1]?.let { btn ->
                mBinding.txtSubmitAbstract.text = btn.name

                mBinding.submitAbstract.setOnClickListener {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWebViewFragment(btn.url))
                }
            }
        }

        data.heading?.let {
            mBinding.txt1.text = it.first().info
        }

        data.personInfo1.first()?.let {
            mBinding.titlePerson.text = it.title
            mBinding.infoPerson.text = it.info

            mBinding.imgPerson.invisible()
            mBinding.shimmerImgPerson.show()
            Helper.loadImage(
                url = "settings/${it.img}",
                isCompleteURL = false,
                imageView = mBinding.imgPerson,
                listener = object: Helper.LoadImageListener{
                    override fun onImageLoaded() {
                        mBinding.imgPerson.show()
                        mBinding.shimmerImgPerson.invisible()
                    }
                    override fun onFailedToLoadImage() {
                        mBinding.imgPerson.show()
                        mBinding.shimmerImgPerson.invisible()
                    }
                }
            )
        }

        data.moreInfo.first()?.let { moreInfo ->
            mBinding.txtMoreInfo1.text = moreInfo.info
            mBinding.txtMoreInfo2.text = moreInfo.info1

            mBinding.imgMoreInfo.invisible()
            mBinding.shimmerImgMoreInfo.show()
            Helper.loadImage(
                url = "settings/${moreInfo.img}",
                isCompleteURL = false,
                imageView = mBinding.imgMoreInfo,
                listener = object: Helper.LoadImageListener{
                    override fun onImageLoaded() {
                        mBinding.imgMoreInfo.show()
                        mBinding.shimmerImgMoreInfo.invisible()
                    }
                    override fun onFailedToLoadImage() {
                        mBinding.imgMoreInfo.show()
                        mBinding.shimmerImgMoreInfo.invisible()
                    }
                }
            )

            mBinding.txtMoreInfoReadMore.setOnClickListener {
                findNavController().navigate(WebViewFragmentDirections.actionGlobalWebViewFragment(moreInfo.readMore))
            }
        }

        data.footer.first()?.let {
            mBinding.titleFooter1.text = it.title
            mBinding.titleFooter2.text = it.message

            mBinding.imgFooter.invisible()
            mBinding.shimmerImgFooter.show()
            Helper.loadImage(
                url = "settings/${it.image}",
                isCompleteURL = false,
                imageView = mBinding.imgFooter,
                listener = object: Helper.LoadImageListener{
                    override fun onImageLoaded() {
                        mBinding.imgFooter.show()
                        mBinding.shimmerImgFooter.invisible()
                    }
                    override fun onFailedToLoadImage() {
                        mBinding.imgFooter.show()
                        mBinding.shimmerImgFooter.invisible()
                    }
                }
            )
        }

        data.information?.let {
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