package com.design_master1.isad.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.design_master1.isad.ui.fragments.CmeFragment
import com.design_master1.isad.ui.fragments.VideosFragment

class TabsPagerAdapter(fm: FragmentManager?): FragmentStatePagerAdapter(fm!!) {

    override fun getItem(position: Int): Fragment {
        return if(position == 0) CmeFragment() else VideosFragment()
    }

    override fun getCount(): Int {
        return NUM_PAGES
    }
    companion object{
        private const val NUM_PAGES = 2
    }
}