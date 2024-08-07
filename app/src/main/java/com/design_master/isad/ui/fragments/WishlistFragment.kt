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
import com.design_master.isad.adapter.WishlistAdapter
import com.design_master.isad.databinding.FragmentWishlistBinding
import com.design_master.isad.model.constants.Constants
import com.design_master.isad.model.listeners.DisableNotificationListener
import com.design_master.isad.model.listeners.EnableNotificationListener
import com.design_master.isad.model.listeners.RemoveFromWishlistListener
import com.design_master.isad.model.listeners.WishlistRequestListener
import com.design_master.isad.model.network.response.EnableNotificationResponseClasses
import com.design_master.isad.model.network.response.GetWishlistResponseClasses
import com.design_master.isad.model.view_models.activities.MainActivityViewModel
import com.design_master.isad.model.view_models.fragments.WishlistFragmentViewModel
import com.design_master.isad.model.view_models.fragments.WishlistFragmentViewModel.GetWishlistState
import com.design_master.isad.ui.activities.MainActivity
import com.design_master.isad.ui.fragments.AddNoteFragment.Companion.FROM_WISHLIST
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishlistFragment : Fragment() {

    private lateinit var mBinding: FragmentWishlistBinding
    private lateinit var mMainActivity: MainActivity
    private val mViewModel: WishlistFragmentViewModel by viewModels()
    private val mMainActivityViewModel: MainActivityViewModel by activityViewModels()
    private lateinit var mWishlistAdapter: WishlistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentWishlistBinding.inflate(layoutInflater)

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

       mWishlistAdapter = WishlistAdapter(object: WishlistRequestListener{
           override fun onClick(wishlist: GetWishlistResponseClasses.WishlistItem) {
               mMainActivityViewModel.mSelectedWishlistItem = wishlist
               findNavController().navigate(WishlistFragmentDirections.actionWishlistFragmentToAddNoteFragment(FROM_WISHLIST))
           }
           override fun onEnableNotification(
               wishlist: GetWishlistResponseClasses.WishlistItem,
               listener: EnableNotificationListener
           ) {
               mMainActivityViewModel.enableNotification(
                   type = wishlist.type,
                   dayWiseId = wishlist.dayWiseId,
                   programOrWorkShopId = wishlist.programOrWorkshopId,
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
               wishlist: GetWishlistResponseClasses.WishlistItem,
               listener: DisableNotificationListener
           ) {
               mMainActivityViewModel.disableNotification(
                   dayWiseId = wishlist.dayWiseId,
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
           override fun onRemoveFromWishlist(
               wishlist: GetWishlistResponseClasses.WishlistItem,
               listener: RemoveFromWishlistListener
           ) {
               mMainActivityViewModel.removeFromWishlist(
                   dayWiseId = wishlist.dayWise.id2,
                   listener = object: RemoveFromWishlistListener{
                       override fun onRemovedFromWishlist() {
                           mWishlistAdapter.selectedWishlistItem?.let {
                               mWishlistAdapter.wishlist.remove(it)
                               mBinding.txtStatus.text = if (mWishlistAdapter.wishlist.isEmpty()) getString(R.string.wishlist_is_empty) else ""
                               mWishlistAdapter.setData(mWishlistAdapter.wishlist)
                           }
                       }

                       override fun onFailedToRemove() {
                           mMainActivity.showToast(getString(R.string.failed_to_remove_from_wishlist))
                       }
                   }
               )
           }
       })
        mBinding.recyclerWishllist.layoutManager = LinearLayoutManager(requireContext())
        mBinding.recyclerWishllist.adapter = mWishlistAdapter

        mViewModel.getWishlistState.observe(viewLifecycleOwner){
            mBinding.shimmerParent.visibility = if (it == GetWishlistState.FETCHING) View.VISIBLE else View.GONE
            mBinding.recyclerWishllist.visibility = if (it == GetWishlistState.FETCHING) View.GONE else View.VISIBLE

            mBinding.txtStatus.text = if (it == GetWishlistState.FAILURE) getString(R.string.something_went_wrong_try_again) else ""

            if (it == GetWishlistState.FETCHED){
                mBinding.txtStatus.text = if (mViewModel.wishlist.isEmpty()) getString(R.string.wishlist_is_empty) else ""
                mWishlistAdapter.setData(mViewModel.wishlist)
            }
        }

        mViewModel.getWishlist(mViewModel.wishlist.isEmpty())

        return mBinding.root
    }
    companion object{
        private const val TAG = "WishlistFragment"
    }
}