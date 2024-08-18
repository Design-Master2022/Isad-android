package com.design_master.isad.ui.activities

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.design_master.isad.R
import com.design_master.isad.adapter.MenuAdapter
import com.design_master.isad.databinding.ActivityMainBinding
import com.design_master.isad.model.constants.Constants
import com.design_master.isad.model.listeners.ActivityResultListener
import com.design_master.isad.model.listeners.BackPressedListener
import com.design_master.isad.model.listeners.MenuListener
import com.design_master.isad.model.network.response.FetchMenuResponseClasses
import com.design_master.isad.model.view_models.activities.MainActivityViewModel
import com.design_master.isad.service.LocationUpdateService
import com.design_master.isad.ui.fragments.QrCodeFragmentDirections
import com.design_master.isad.ui.fragments.WebViewFragmentDirections
import com.design_master.isad.utils.helper.PermissionHelper
import com.design_master.isad.utils.worker.MyWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private val mViewModel: MainActivityViewModel by viewModels()
    private lateinit var mNavController: NavController
    private lateinit var mActivityResultListener: ActivityResultListener
    private lateinit var mBackPressedListener: BackPressedListener
    private lateinit var mMenuAdapter: MenuAdapter
    private lateinit var context: Context

    private var INSTAGRAM_URL = ""
    private var LINKED_IN_URL = ""
    private var YOUTUBE_URL = ""
    private var FACEBOOK_URL = ""
    private var TWITTER_URL = ""
    private var WHATSAPP_URL = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        context = this

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        mNavController = navHostFragment.navController
        val navGraph = mNavController.navInflater.inflate(R.navigation.main_nav_graph)
        mNavController.graph = navGraph
        NavigationUI.setupWithNavController(mBinding.bottomNavigation, mNavController)

        mNavController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (mBinding.bottomNavigation.selectedItemId == R.id.homeFragment) updateCustomNavigationLayout(
                getString(R.string.home)
            )
            else if (mBinding.bottomNavigation.selectedItemId == R.id.programsFragment) updateCustomNavigationLayout(
                getString(R.string.scientific_programs)
            )
            else if (mBinding.bottomNavigation.selectedItemId == R.id.workShopsFragment) updateCustomNavigationLayout(
                getString(R.string.workshop)
            )
            else if (mBinding.bottomNavigation.selectedItemId == R.id.wishlistFragment) updateCustomNavigationLayout(
                getString(R.string.wishlist)
            )
            else if (mBinding.bottomNavigation.selectedItemId == R.id.loginWithOtpFragment) updateCustomNavigationLayout(
                getString(R.string.post_conference)
            )
            mBinding.actionBar.title.text = destination.label
        }

        mBinding.customBottomNavigation.layoutHome.setOnClickListener {
            mBinding.bottomNavigation.selectedItemId = R.id.homeFragment
            updateCustomNavigationLayout(getString(R.string.home))
        }
        mBinding.customBottomNavigation.layoutProgram.setOnClickListener {
            mBinding.bottomNavigation.selectedItemId = R.id.programsFragment
            updateCustomNavigationLayout(getString(R.string.scientific_programs))
        }
        mBinding.customBottomNavigation.layoutWorkshop.setOnClickListener {
            mBinding.bottomNavigation.selectedItemId = R.id.workShopsFragment
            updateCustomNavigationLayout(getString(R.string.workshop))
        }
        mBinding.customBottomNavigation.layoutWishlist.setOnClickListener {
            mBinding.bottomNavigation.selectedItemId = R.id.wishlistFragment
            updateCustomNavigationLayout(getString(R.string.wishlist))
        }
        mBinding.customBottomNavigation.layoutPost.setOnClickListener {
            mViewModel.isForQrCodeFragment = false
            mBinding.bottomNavigation.selectedItemId = R.id.loginWithOtpFragment
            updateCustomNavigationLayout(getString(R.string.post_conference))
        }


        Log.d(TAG, "WORKER: IsWelcomeMessageSent = ${mViewModel.mPrefsController.isWelcomeNotificationSent()}")

        if (!mViewModel.mPrefsController.isWelcomeNotificationSent()){
            Log.d(TAG, "WORKER: IsWorkScheduled = ${isWorkScheduled(WorkManager.getInstance(this).getWorkInfosByTag(WORKER_TAG).get())}")
            checkLocationPermissions()
        }else{
            if (isWorkScheduled(WorkManager.getInstance(this).getWorkInfosByTag(WORKER_TAG).get()))
                WorkManager.getInstance().cancelAllWorkByTag(WORKER_TAG)

            if (isMyServiceRunning(LocationUpdateService::class.java)){
                val intent = Intent(this, LocationUpdateService::class.java)
                this.stopService(intent)
            }
        }

        mMenuAdapter = MenuAdapter(object: MenuListener{
            override fun onClick(menuItem: FetchMenuResponseClasses.MenuItem) {
                mBinding.drawer.close()
                mNavController.navigate(WebViewFragmentDirections.actionGlobalWebViewFragment(menuItem.redirectUrl))
            }
        })
        mBinding.drawerLayout.recycler.layoutManager = LinearLayoutManager(context)
        mBinding.drawerLayout.recycler.adapter = mMenuAdapter

        mViewModel.menuData.observe(this){
            it?.let {
                mMenuAdapter.setData(it)
            }
        }
        mViewModel.socialUrls.observe(this){
            it?.let {
                FACEBOOK_URL = it.facebook
                INSTAGRAM_URL = it.instagram
                YOUTUBE_URL = it.youtube
                WHATSAPP_URL = it.whatsapp
                TWITTER_URL = it.twitter
                LINKED_IN_URL = it.linkedin

                mBinding.drawerLayout.facebook.visibility = if (it.facebook.isNullOrEmpty()) View.GONE else View.VISIBLE
                mBinding.drawerLayout.youtube.visibility = if (it.youtube.isNullOrEmpty()) View.GONE else View.VISIBLE
                mBinding.drawerLayout.instagram.visibility = if (it.instagram.isNullOrEmpty()) View.GONE else View.VISIBLE
                mBinding.drawerLayout.linkedIn.visibility = if (it.linkedin.isNullOrEmpty()) View.GONE else View.VISIBLE
                mBinding.drawerLayout.whatsapp.visibility = if (it.whatsapp.isNullOrEmpty()) View.GONE else View.VISIBLE
                mBinding.drawerLayout.twitter.visibility = if (it.twitter.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
        }

        mViewModel.fetchMenu()

        mViewModel.fetchSocial()
    }
    fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
    fun initializeActivityResultListener(listener: ActivityResultListener){
        mActivityResultListener = listener
    }
    fun initializeBackPressedListener(listener: BackPressedListener){
        mBackPressedListener = listener
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (this::mActivityResultListener.isInitialized) mActivityResultListener.onActivityResult(requestCode, resultCode, data)
    }
    fun getMainActivityBinding(): ActivityMainBinding{
        return mBinding
    }
    private fun updateCustomNavigationLayout(label: String){
        setCustomBottomNavigationHome(label == getString(R.string.home))
        setCustomBottomNavigationPrograms(label == getString(R.string.scientific_programs))
        setCustomActionBarWorkShop(label == getString(R.string.workshop))
        setCustomActionBarWishlist(label == getString(R.string.wishlist))
        setCustomActionBarPost(label == getString(R.string.post_conference))
    }
    private fun setCustomBottomNavigationHome(isSelected: Boolean){
        mBinding.customBottomNavigation.imageHomeActivated.visibility = if (isSelected) View.VISIBLE else View.GONE
        mBinding.customBottomNavigation.imageHome.visibility = if (isSelected) View.GONE else View.VISIBLE
        mBinding.customBottomNavigation.nameHome.visibility = if (isSelected) View.VISIBLE else View.GONE
    }
    private fun setCustomBottomNavigationPrograms(isSelected: Boolean){
        mBinding.customBottomNavigation.imageProgramActivated.visibility = if (isSelected) View.VISIBLE else View.GONE
        mBinding.customBottomNavigation.imageProgram.visibility = if (isSelected) View.GONE else View.VISIBLE
        mBinding.customBottomNavigation.nameProgram.visibility = if (isSelected) View.VISIBLE else View.GONE
    }
    private fun setCustomActionBarWorkShop(isSelected: Boolean){
        mBinding.customBottomNavigation.imageWorkshopActivated.visibility = if (isSelected) View.VISIBLE else View.GONE
        mBinding.customBottomNavigation.imageWorkshop.visibility = if (isSelected) View.GONE else View.VISIBLE
        mBinding.customBottomNavigation.nameWorkshop.visibility = if (isSelected) View.VISIBLE else View.GONE
    }
    private fun setCustomActionBarWishlist(isSelected: Boolean){
        mBinding.customBottomNavigation.imageWishlistActivated.visibility = if (isSelected) View.VISIBLE else View.GONE
        mBinding.customBottomNavigation.imageWishlist.visibility = if (isSelected) View.GONE else View.VISIBLE
        mBinding.customBottomNavigation.nameWishlist.visibility = if (isSelected) View.VISIBLE else View.GONE
    }
    private fun setCustomActionBarPost(isSelected: Boolean){
        mBinding.customBottomNavigation.imagePostActivated.visibility = if (isSelected) View.VISIBLE else View.GONE
        mBinding.customBottomNavigation.imagePost.visibility = if (isSelected) View.GONE else View.VISIBLE
        mBinding.customBottomNavigation.namePost.visibility = if (isSelected) View.VISIBLE else View.GONE
    }
    fun setActionBar(
        shouldShowStatusBar: Boolean = true,
        shouldShowActionBar: Boolean = true,
        shouldShowBackBtn: Boolean = false,
        shouldShowNotificationBtn: Boolean = false,
        shouldShowTickBtn: Boolean = false,
        shouldShowDrawerMenuBtn: Boolean = false,
        shouldShowTitle: Boolean = true,
        setActionBarTitle: String = "",
        shouldShowLogoImage: Boolean = true,
        shouldShowBottomNavigation: Boolean = false,
        backBtnClickListener: BackBtnClickListener? = null,
        notificationBtnClickListener: NotificationClickListener? = null,
        tickBtnClickListener: TickClickListener? = null,
        drawerMenuBtnClickListener: DrawerMenuClickListener? = null
    ){

        mBinding.actionBarView.visibility = if (shouldShowActionBar) View.VISIBLE else View.GONE
        mBinding.actionBar.actionBar.visibility = if (shouldShowActionBar) View.VISIBLE else View.GONE

        mBinding.actionBar.btnBack.visibility = if (shouldShowBackBtn) View.VISIBLE else View.GONE
        mBinding.actionBar.btnDrawer.visibility = if (shouldShowDrawerMenuBtn) View.VISIBLE else View.GONE
        mBinding.actionBar.title.visibility = if (shouldShowTitle) View.VISIBLE else View.GONE
        mBinding.actionBar.logoImage.visibility = if (shouldShowLogoImage) View.VISIBLE else View.GONE
        mBinding.actionBar.btnNotification.visibility = if (shouldShowNotificationBtn) View.VISIBLE else View.GONE
        mBinding.actionBar.btnTick.visibility = if (shouldShowTickBtn) View.VISIBLE else View.GONE

        if (!mBinding.actionBar.btnTick.isVisible && !mBinding.actionBar.btnNotification.isVisible) mBinding.actionBar.btnTick.visibility = View.INVISIBLE
        if (!mBinding.actionBar.btnDrawer.isVisible && !mBinding.actionBar.btnBack.isVisible) mBinding.actionBar.btnBack.visibility = View.INVISIBLE

        if (setActionBarTitle.isNotEmpty()) mBinding.actionBar.title.text = setActionBarTitle


        mBinding.customBottomNavigation.parent.visibility = if (shouldShowBottomNavigation) View.VISIBLE else View.GONE
        mBinding.bottomView.visibility = if (shouldShowBottomNavigation) View.VISIBLE else View.GONE

//
//        if (shouldShowStatusBar)
//            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        else
//            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        mBinding.actionBar.btnBack.setOnClickListener {
            mNavController.popBackStack()
        }
        mBinding.actionBar.btnDrawer.setOnClickListener {
            mBinding.drawer.open()
        }
        mBinding.actionBar.btnNotification.setOnClickListener {
            mNavController.navigate(R.id.action_global_notificationsFragment)
        }
        mBinding.actionBar.btnTick.setOnClickListener {
            tickBtnClickListener?.onClick()
        }
        mBinding.drawerLayout.facebook.setOnClickListener {
            mBinding.drawer.close()
            openLink(FACEBOOK_URL)
        }
        mBinding.drawerLayout.instagram.setOnClickListener {
            mBinding.drawer.close()
            openLink(INSTAGRAM_URL)
        }
        mBinding.drawerLayout.twitter.setOnClickListener {
            mBinding.drawer.close()
            openLink(TWITTER_URL)
        }
        mBinding.drawerLayout.linkedIn.setOnClickListener {
            mBinding.drawer.close()
            openLink(LINKED_IN_URL)
        }
        mBinding.drawerLayout.youtube.setOnClickListener {
            mBinding.drawer.close()
            openLink(YOUTUBE_URL)
        }
        mBinding.drawerLayout.whatsapp.setOnClickListener {
            mBinding.drawer.close()
            openLink(WHATSAPP_URL)
        }
    }
    fun openLink(link: String){
        startActivity(
            Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse(link))
        )
    }
    fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    override fun onBackPressed() {
        if (this::mBackPressedListener.isInitialized) mBackPressedListener.onBackPressed()
        super.onBackPressed()
    }
    companion object{
        private const val TAG = "MainActivity"
        private const val WORKER_TAG = "welcome_notification_work"
        interface BackBtnClickListener{
            fun onClick()
        }
        interface DrawerMenuClickListener{
            fun onClick()
        }
        interface NotificationClickListener{
            fun onClick()
        }
        interface TickClickListener{
            fun onClick()
        }
    }
    private fun startWork(){
        val periodicWork = PeriodicWorkRequest.Builder(MyWorker::class.java, 60, TimeUnit.MINUTES)
            .addTag(WORKER_TAG)
            .build()
        WorkManager.getInstance().enqueueUniquePeriodicWork("welcome_notification_work", ExistingPeriodicWorkPolicy.KEEP, periodicWork)
    }
    private fun checkLocationPermissions(){
        val permissions = listOf(
            Constants.COURSE_LOCATION_PERMISSION,
            Constants.FINE_LOCATION_PERMISSION
        )
        val deniedPermissions = mViewModel.mHelper.checkPermissions(
            permissions.toTypedArray(),
            this
        )
        if(deniedPermissions.isNotEmpty()){
            mViewModel.mPermissionHelper.askForPermission(
                context = this,
                helper = mViewModel.mHelper,
                layoutInflater = layoutInflater,
                permissionToBeAsked = deniedPermissions,
                listener = object: PermissionHelper.PermissionListener{
                    override fun onAllPermissionsGranted() {
                        Log.d(TAG, "onAllPermissionsGranted: ")
                        askNotificationPermission()
                        if (!mViewModel.mHelper.isLocationProviderIsEnabled(this@MainActivity))
                            startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        startWork()
                    }

                    override fun onPermissionRefusedByDialog() {
                        Log.d(TAG, "onPermissionRefusedByDialog: ")
                    }
                }
            )
        }else{
            startWork()
            if (!mViewModel.mHelper.isLocationProviderIsEnabled(this@MainActivity))
                startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }
    fun askNotificationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val deniedPermissions = mViewModel.mHelper.checkPermissions(
                listOf(Constants.POST_NOTIFICATION_PERMISSION).toTypedArray(),
                this
            )
            if(deniedPermissions.isNotEmpty()){
                mViewModel.mPermissionHelper.askForPermission(
                    context = this,
                    helper = mViewModel.mHelper,
                    layoutInflater = layoutInflater,
                    permissionToBeAsked = deniedPermissions,
                    listener = object: PermissionHelper.PermissionListener{
                        override fun onAllPermissionsGranted() {
                            Log.d(TAG, "onAllPermissionsGranted: ")
                        }

                        override fun onPermissionRefusedByDialog() {
                            Log.d(TAG, "onPermissionRefusedByDialog: ")
                        }
                    }
                )
            }
        }
    }
    private fun isWorkScheduled(workInfos: List<WorkInfo>?): Boolean {
        var running = false
        if (workInfos.isNullOrEmpty()) return false
        for (workStatus in workInfos) {
            running =
                (workStatus.state == WorkInfo.State.RUNNING) or (workStatus.state == WorkInfo.State.ENQUEUED)
        }
        return running
    }
}