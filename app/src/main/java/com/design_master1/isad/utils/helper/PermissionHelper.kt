package com.design_master1.isad.utils.helper

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.design_master1.isad.R
import com.design_master1.isad.databinding.DialogHelperBinding


class PermissionHelper {

    private var index: Int = 0

    interface PermissionListener{
        fun onAllPermissionsGranted()
        fun onPermissionRefusedByDialog()
    }

    fun askForPermission(
        context: Context,
        helper: Helper,
        layoutInflater: LayoutInflater,
        permissionToBeAsked: List<String>,
        listener: PermissionListener
    ){
        index = 0
        if (permissionToBeAsked.isNotEmpty()) {
            askNext(context, helper, layoutInflater, permissionToBeAsked, listener)
        }
    }

    private fun askNext(
        context: Context,
        helper: Helper,
        layoutInflater: LayoutInflater,
        permissionToBeAsked: List<String>,
        listener: PermissionListener
    ){
//        Log.d(TAG, "askNext: $index")
        Dexter.withContext(context)
            .withPermission(permissionToBeAsked[index])
            .withListener(object: com.karumi.dexter.listener.single.PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    index += 1
                    if (index < permissionToBeAsked.size) askNext(context, helper, layoutInflater, permissionToBeAsked, listener)
                    else p0?.let {
                        listener.onAllPermissionsGranted()
                    }
                }
                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    p0?.let {
                        showPermissionDialog(context, helper, layoutInflater, permissionToBeAsked, helper.getPermissionName(context, p0.permissionName), p0.isPermanentlyDenied, listener)
                    }
                }
                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            }).onSameThread().check()
    }

    fun goToPermissionSetting(context: Context){
        context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .setData(Uri.fromParts("package", context.packageName,null)))
    }

    private fun showPermissionDialog(
        context: Context,
        helper: Helper,
        layoutInflater: LayoutInflater,
        permissionToBeAsked: List<String>,
        permissionName: String,
        isPermanentlyDenied: Boolean,
        listener: PermissionListener
    ){
        helper.showDialog(
            context = context,
            layoutInflater = layoutInflater,
            title = context.getString(R.string.permission_required),
            description = "$permissionName ${if (isPermanentlyDenied) context.getString(R.string.permission_permanently_denied_message) else context.getString(R.string.permission_required_description)}",
            negativeBtnName = context.getString(R.string.cancel),
            positiveBtnName = if (isPermanentlyDenied) context.getString(R.string.go_to_settings) else context.getString(R.string.ok),
            negativeBtnListener = object: Helper.DialogNegativeBtnListener{
                override fun onNegativeBtnClick(dialog: Dialog, binding: DialogHelperBinding) {
                    dialog.dismiss()
                    listener.onPermissionRefusedByDialog()
                }
            },
            positiveBtnListener = object: Helper.DialogPositiveBtnListener{
                override fun onPositiveBtnClick(dialog: Dialog, binding: DialogHelperBinding) {
                    dialog.dismiss()
                    if (isPermanentlyDenied) goToPermissionSetting(context)
                    else askNext(context, helper, layoutInflater, permissionToBeAsked, listener)
                }
            }
        )
    }

    companion object{
        private const val TAG = "PermissionHelper"
    }
}