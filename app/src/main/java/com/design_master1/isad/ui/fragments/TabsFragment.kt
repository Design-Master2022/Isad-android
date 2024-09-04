package com.design_master1.isad.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayout
import com.design_master1.isad.R
import com.design_master1.isad.adapter.TabsPagerAdapter
import com.design_master1.isad.databinding.FragmentTabsBinding
import com.design_master1.isad.model.view_models.activities.MainActivityViewModel
import com.design_master1.isad.ui.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabsFragment : Fragment() {

    private lateinit var mBinding: FragmentTabsBinding
    private lateinit var mMainActivity: MainActivity
    private val mMainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val mArgs: TabsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentTabsBinding.inflate(layoutInflater)

        mMainActivity = requireActivity() as MainActivity

        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText(getString(R.string.cme)))
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText(getString(R.string.video)))

        mMainActivity.setActionBar(
            shouldShowActionBar = false
        )

        mMainActivityViewModel.cmeUrl = mArgs.cmeUrl
        mMainActivityViewModel.videoUrl = mArgs.videosUrl

        val pagerAdapter = TabsPagerAdapter(childFragmentManager)
        mBinding.viewPager.adapter = pagerAdapter

        mBinding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(mBinding.tabLayout))

        mBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { mBinding.viewPager.currentItem = it }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab?.position?.let { mBinding.viewPager.currentItem = it }
            }
        })


        return mBinding.root
    }
}