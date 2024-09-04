package com.design_master1.isad.utils.helper

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.os.Handler
import android.util.Base64
import android.util.Base64OutputStream
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.design_master1.isad.R
import com.design_master1.isad.databinding.DialogHelperBinding
import com.design_master1.isad.databinding.DialogImagePickerBinding
import com.design_master1.isad.databinding.LayoutSnackbarBinding
import com.design_master1.isad.model.constants.Constants
import com.design_master1.isad.model.provider.ElectroLibAccessProvider
import com.design_master1.isad.model.listeners.ImagePickerListener
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToLong

class Helper (val application: Application? = null){

    interface LoadImageListener{
        fun onImageLoaded()
        fun onFailedToLoadImage()
    }

    interface SnackBarPositiveBtnListener{
        fun onPositiveBtnClick(snackbar: Snackbar)
    }

    interface DialogPositiveBtnListener{
        fun onPositiveBtnClick(dialog: Dialog, binding: DialogHelperBinding)
    }

    interface DialogNegativeBtnListener{
        fun onNegativeBtnClick(dialog: Dialog, binding: DialogHelperBinding)
    }

    fun dialogAttributes(dialog: Dialog, shouldHeightMatchParent: Boolean, shouldWidthMatchParent: Boolean ): WindowManager.LayoutParams{
        val mLayoutParams: WindowManager.LayoutParams = WindowManager.LayoutParams()
        dialog.window?.let { window ->
            mLayoutParams.copyFrom(window.attributes)
        }
        mLayoutParams.width = if (shouldWidthMatchParent) WindowManager.LayoutParams.MATCH_PARENT else WindowManager.LayoutParams.WRAP_CONTENT

        mLayoutParams.height = if (shouldHeightMatchParent) WindowManager.LayoutParams.MATCH_PARENT else WindowManager.LayoutParams.WRAP_CONTENT

        return mLayoutParams
    }

    fun dialogWindow(
        context: Context,
        isCancelable: Boolean = true
    ): Dialog{
        val dialog = Dialog(context)
        dialog.window?.addFlags(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        dialog.setCancelable(isCancelable)
        return dialog
    }

    fun showDialog(
        context: Context,
        layoutInflater: LayoutInflater,
        negativeBtnName: String = "",
        positiveBtnName: String = "",
        shouldShowCancelBtn: Boolean = false,
        title: String = "",
        description: String = "",
        shouldHeightMatchParent: Boolean = false,
        shouldWidthMatchParent: Boolean = true,
        negativeBtnListener: DialogNegativeBtnListener? = null,
        positiveBtnListener: DialogPositiveBtnListener? = null
    ){
        val dialog = Dialog(context)
        dialog.window?.addFlags(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        dialog.setCancelable(false)
        val binding = DialogHelperBinding.inflate(layoutInflater);
        dialog.setContentView(binding.root)

        binding.title.visibility = if (title.isEmpty()) View.GONE else View.VISIBLE
        binding.description.visibility = if (description.isEmpty()) View.GONE else View.VISIBLE
        binding.negativeBtn.visibility = if (negativeBtnName.isEmpty()) View.GONE else View.VISIBLE
        binding.positiveBtn.visibility = if (positiveBtnName.isEmpty()) View.GONE else View.VISIBLE
        binding.cancelBtn.visibility = if (shouldShowCancelBtn) View.VISIBLE else View.INVISIBLE

        binding.title.text = title
        binding.description.text = description
        binding.negativeBtn.text = negativeBtnName
        binding.positiveBtn.text = positiveBtnName

        binding.negativeBtn.setOnClickListener {
            negativeBtnListener?.let {
                negativeBtnListener.onNegativeBtnClick(dialog, binding)
            }
        }
        binding.positiveBtn.setOnClickListener {
            positiveBtnListener?.let {
                positiveBtnListener.onPositiveBtnClick(dialog, binding)
            }
        }
        binding.cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        if (!dialog.isShowing){
            dialog.show()
            dialog.window?.attributes = dialogAttributes(
                dialog = dialog,
                shouldWidthMatchParent = shouldWidthMatchParent,
                shouldHeightMatchParent = shouldHeightMatchParent
            )
        }
    }

    fun showImagePickerDialog(
        context: Context,
        layoutInflater: LayoutInflater,
        imagePickerListener: ImagePickerListener
    ){
        val dialog = Dialog(context)
        dialog.window?.addFlags(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        dialog.setCancelable(true)
        val binding = DialogImagePickerBinding.inflate(layoutInflater);
        dialog.setContentView(binding.root)

        binding.camera.setOnClickListener {
            imagePickerListener.openCamera()
            dialog.dismiss()
        }
        binding.gallery.setOnClickListener {
            imagePickerListener.openGallery()
            dialog.dismiss()
        }

        if (!dialog.isShowing){
            dialog.show()
            dialog.window?.attributes = dialogAttributes(
                dialog = dialog,
                shouldWidthMatchParent = true,
                shouldHeightMatchParent = false
            )
        }
    }


    fun slideUpAndShow(view: View, durationMillis: Long){
        view.visibility = View.VISIBLE
        val animate = TranslateAnimation(
            0f,               // fromXDelta
            0f,                 // toXDelta
            view.height.toFloat(),      // fromYDelta
            0f)                 // toYDelta
        animate.duration = durationMillis
        animate.fillAfter = true
        view.startAnimation(animate)
    }
    fun slideDownAndHide(view: View, durationMillis: Long){
        val animate = TranslateAnimation(
            0f,                 // fromXDelta
            0f,                   // toXDelta
            0f,                 // fromYDelta
            view.height.toFloat())        // toYDelta
        animate.duration = durationMillis
        animate.fillAfter = true
        view.startAnimation(animate)
        view.visibility = View.GONE
    }

    fun isValidEmail(email: String): Boolean{
        return email.contains(".") && email.contains("@") && email.length >= 5
    }
    fun getBaseUrl() = ElectroLibAccessProvider().getServerBaseUrl()
    fun getBaseUrlForImage() = "${ElectroLibAccessProvider().getServerBaseUrl()}${Constants.IMAGES_URL_EXTENSION}"
//    fun getPathUrlToShare(endPoint: String) = $endPoint"




    fun showSnackBar(
        layoutInflater: LayoutInflater,
        view: View,
        text: String,
        positiveBtnName: String = "",
        positiveBtnListener: SnackBarPositiveBtnListener? = null,
        shouldAutoDismiss: Boolean = true
    ){
        val binding = LayoutSnackbarBinding.inflate(layoutInflater)

        val mSnackBar = Snackbar.make(view,text,if (shouldAutoDismiss) Snackbar.LENGTH_LONG else Snackbar.LENGTH_INDEFINITE)
        mSnackBar.view.setBackgroundColor(Color.TRANSPARENT)
        mSnackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        val mSnackBarLayout = mSnackBar.view as Snackbar.SnackbarLayout
        mSnackBarLayout.setPadding(0, 0, 0, 0)

        val params = mSnackBar.view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        mSnackBar.view.layoutParams = params

        binding.positiveBtn.text = positiveBtnName

        binding.positiveBtn.visibility = if (positiveBtnName.isEmpty()) View.GONE else View.VISIBLE
        binding.text.text = text

        binding.cancel.setOnClickListener {
            mSnackBar.dismiss()
        }

        binding.positiveBtn.setOnClickListener {
            Log.d(TAG, "showSnackBar: ")
            positiveBtnListener?.onPositiveBtnClick(mSnackBar)
            mSnackBar.dismiss()
        }
        mSnackBarLayout.addView(binding.root,0)
        mSnackBar.show()
    }

    fun convertImageFileToBase64(imageFile: File): String {
        return ByteArrayOutputStream().use { outputStream ->
            Base64OutputStream(outputStream, Base64.DEFAULT).use { base64FilterStream ->
                imageFile.inputStream().use { inputStream ->
                    inputStream.copyTo(base64FilterStream)
                }
            }
            return@use outputStream.toString()
        }
    }
    fun showKeyBoard(context: Context){
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun hideKeyBoard(context: Context, currentFocus: View){
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }

    fun calculateValue(costOfSingle: Double, quantity: Int): Double{
        return round(costOfSingle.times(quantity))
    }
    fun round(value: Double): Double{
        val factor = 10.0.pow(1.0)
        return (value * factor).roundToLong() / factor
    }
    fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
        outputStream().use { out ->
            bitmap.compress(format, quality, out)
            out.flush()
        }
    }

    // To convertMillisToDaysHoursMinutesSeconds check  { https://github.com/electrodragon/MulmEndGameMainAccountant/blob/main/app/src/main/java/com/light_house/mulm_end_game_main_accountant/model/view_models/fragments/SellProductFragmentViewModel.kt }
    fun convertDateStringToMillis(inputFormat: String, dateString: String): Long{
        val formatter = SimpleDateFormat(inputFormat, Locale.US)
        val date = formatter.parse(dateString) as Date
        return date.time
    }
    fun convertMillisToDateString(outputFormat: String, millis: Long): String{
        val formatter = SimpleDateFormat(outputFormat, Locale.US)
        return formatter.format(Date(millis))
    }
    fun convert24HrFormatTo12hrFormat(time: String): String{
        val f1 = SimpleDateFormat("HH:mm:ss")
        val d = f1.parse(time)
        val f2 = SimpleDateFormat("h:mm a");

        Log.d(TAG, "convert24HrFormatTo12hrFormat: ")
        return f2.format(d)
    }

    fun convertFileToBase64(file: File): String {
        return Base64.encodeToString(file.readBytes(), Base64.NO_WRAP)
    }

    fun convertBase64ToBitmap(base64: String): Bitmap{
        val decodedString = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    fun copyToClipBoard(context: Context, text: String){
        val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("BASE64 ->", text))
    }
    fun isLocationProviderIsEnabled(context: Context): Boolean{
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gpsEnabled = false
        var networkEnabled = false
        var isEnabled = false

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }

        if (gpsEnabled && networkEnabled) {
            isEnabled = true
        }
        return isEnabled
    }

    private fun isPermissionGranted(permission: String, context: Context): Boolean{
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
    fun checkPermissions(permissions: Array<String>, context: Context): ArrayList<String>{
        val deniedPermissions = ArrayList<String>()
        for (permission in permissions){
            if (!isPermissionGranted(permission,context)){
                deniedPermissions.add(permission)
            }
        }
        return deniedPermissions
    }
    fun askPermissions(permissions: Array<String>, requestCode: Int, activity: Activity){
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }
    fun getPermissionName(context: Context, permission: String): String{
        return when(permission){
            Constants.COURSE_LOCATION_PERMISSION -> context.getString(R.string.location)
            Constants.FINE_LOCATION_PERMISSION -> context.getString(R.string.location)
            Constants.CAMERA_PERMISSION -> context.getString(R.string.camera)
            Constants.WRITE_EXTERNAL_STORAGE_PERMISSION -> context.getString(R.string.storage)
            Constants.READ_EXTERNAL_STORAGE_PERMISSION -> context.getString(R.string.storage)
            Constants.RECORD_AUDIO_PERMISSION -> context.getString(R.string.audio)
            else -> ""
        }
    }

    fun delayAndDo(timeInMillis: Long, needToDo: () -> Unit){
        val handler = Handler()
        handler.postDelayed({
            needToDo()
        }, timeInMillis)
    }

    companion object{
        private const val TAG = "Helper"
        const val INPUT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
        const val OUTPUT_DATE_FORMAT = "dd/MM/yyyy"
        const val ORDER_STATE_TIME_FORMAT = "hh:mm a dd MMM yyyy"
        const val DOWNLOAD_CONTENT_DIRECTORY = "downloads"


       // 9:10 PM 20 June 2020


//        EEE : Day ( Mon )
//        MMMM : Full month name ( December ) // MMMM February
//        MMM : Month in words ( Dec )
//        MM : Month ( 12 )
//        dd : Day in 2 chars ( 03 )
//        d: Day in 1 char (3)
//        HH : Hours ( 24 )
//        hh : Hours ( 12 )
//        mm : Minutes ( 50 )
//        ss : Seconds ( 34 )
//        yyyy: Year ( 2020 ) //both yyyy and YYYY are same
//        YYYY: Year ( 2020 )
//        zzz : GMT+05:30
//        a : ( AM / PM )
//        aa : ( AM / PM )
//        aaa : ( AM / PM )
//        aaaa : ( AM / PM )






        fun getCompleteUrl(endPoint: String) = "${ElectroLibAccessProvider().getServerBaseUrl()}${Constants.IMAGES_URL_EXTENSION}$endPoint"
        fun getDeviceHeight(activity: Activity): Int {
            val metrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(metrics)
            return metrics.heightPixels
        }
        fun getDeviceWidth(activity: Activity): Int {
            val metrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(metrics)
            return metrics.widthPixels
        }
        fun flip(image: File): File {
            val bitmap = BitmapFactory.decodeFile(image.absolutePath)
            val matrix = Matrix()
            matrix.preScale(-1.0f, 1.0f)
            return bitmapToFile(image.absolutePath, Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true))
        }
        fun bitmapToFile(path: String, bitmap: Bitmap): File{
            val file = File(path)
            val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.close()
            return file
        }
        fun loadImage(
            url: String?,
            imageView: ImageView,
            listener: LoadImageListener,
            isCompleteURL: Boolean = true
        ){

            Glide.with(imageView)
                .load(url?.let { if (isCompleteURL) url else "${ElectroLibAccessProvider().getServerBaseUrl()}${Constants.IMAGES_URL_EXTENSION}$url" }?:{R.color.black})
                .error(R.color.black)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d(TAG, "onLoadFailed: ${e?.message}  ${e?.printStackTrace()}")
                        listener.onFailedToLoadImage()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        listener.onImageLoaded()
                        return false
                    }
                })
                .into(imageView)
        }

    }
}