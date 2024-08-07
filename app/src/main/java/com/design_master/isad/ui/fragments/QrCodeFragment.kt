package com.design_master.isad.ui.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.design_master.isad.databinding.FragmentQrCodeBinding
import com.design_master.isad.ui.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QrCodeFragment : Fragment() {

    private lateinit var mBinding: FragmentQrCodeBinding
    private lateinit var mMainActivity: MainActivity
    private val mArgs: QrCodeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentQrCodeBinding.inflate(layoutInflater)

        mMainActivity = requireActivity() as MainActivity

        mMainActivity.setActionBar(
            shouldShowBackBtn = true,
            backBtnClickListener = object: MainActivity.Companion.BackBtnClickListener{
                override fun onClick() {
                    findNavController().popBackStack()
                }
            }
        )

        generateQrCode()

        return mBinding.root
    }
    private fun generateQrCode(){
        val qrCodeEncoder = QRGEncoder(mArgs.registrationCode, null, QRGContents.Type.TEXT, 512)
        qrCodeEncoder.colorBlack = Color.BLACK
        qrCodeEncoder.colorWhite = Color.WHITE
        try {
            val bitmap = qrCodeEncoder.getBitmap(0)
            mBinding.qrCodeImg.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}