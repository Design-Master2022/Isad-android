package com.design_master1.isad.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.design_master1.isad.R
import com.design_master1.isad.databinding.FragmentAddNoteBinding
import com.design_master1.isad.extensions.hide
import com.design_master1.isad.extensions.invisible
import com.design_master1.isad.extensions.show
import com.design_master1.isad.model.view_models.activities.MainActivityViewModel
import com.design_master1.isad.model.view_models.fragments.AddNoteFragmentViewModel
import com.design_master1.isad.model.view_models.fragments.AddNoteFragmentViewModel.SaveNotesState
import com.design_master1.isad.ui.activities.MainActivity
import com.design_master1.isad.utils.helper.Helper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNoteFragment : Fragment() {

    private lateinit var mBinding: FragmentAddNoteBinding
    private lateinit var mMainActivity: MainActivity
    private val mArgs: AddNoteFragmentArgs by navArgs()
    private val mMainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val mViewModel: AddNoteFragmentViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAddNoteBinding.inflate(layoutInflater)

        mMainActivity = requireActivity() as MainActivity

        mMainActivity.setActionBar(
            shouldShowActionBar = true,
            shouldShowLogoImage = false,
            shouldShowTickBtn = true,
            shouldShowBackBtn = true,
            tickBtnClickListener = object: MainActivity.Companion.TickClickListener{
                override fun onClick() {
                    saveNotes()
                }
            },
            backBtnClickListener = object: MainActivity.Companion.BackBtnClickListener{
                override fun onClick() {
                    findNavController().popBackStack()
                }
            }
        )

        mBinding.editor.setEditorHeight(200)
        mBinding.editor.setEditorFontSize(14)
//        mBinding.editor.setEditorFontColor(Color.RED)
        mBinding.editor.setPadding(10,10,10,10)
        mBinding.editor.setPlaceholder(getString(R.string.enter_the_title))

        mBinding.bold.setOnClickListener {
            mBinding.editor.setBold()
        }
        mBinding.italic.setOnClickListener {
            mBinding.editor.setItalic()
        }
        mBinding.underline.setOnClickListener {
            mBinding.editor.setUnderline()
        }
        mBinding.bullet.setOnClickListener {
            mBinding.editor.setBullets()
        }
        mBinding.quote.setOnClickListener {
            mBinding.editor.setBlockquote()
        }
        mBinding.bold.setOnClickListener {
            mBinding.editor.setBold()
        }
        mBinding.bold.setOnClickListener {
            mBinding.editor.setBold()
        }

        mBinding.btnSave.setOnClickListener {
            saveNotes()
        }

        if (mArgs.fromWhere == FROM_PROGRAMS) {
            mMainActivityViewModel.mSelectedProgram?.let { program ->
                mBinding.img.invisible()
                mBinding.shimmerImg.show()
                Helper.loadImage(
                    url = "speakers/${program.speaker.image}",
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

                mBinding.title.text = program.speaker.name
                program.userNotes?.let {
                    mBinding.editor.html = it.notes
                }
            }
        }

        if (mArgs.fromWhere == FROM_WORKSHOPS) {
            mMainActivityViewModel.mSelectedWorkShop?.let { workShop ->
                mBinding.img.invisible()
                mBinding.shimmerImg.show()
                Helper.loadImage(
                    url = "speakers/${workShop.speaker.image}",
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

                mBinding.title.text = workShop.title
                workShop.userNotes?.let {
                    mBinding.editor.html = it.notes
                }
            }
        }

        if (mArgs.fromWhere == FROM_WISHLIST) {
            mMainActivityViewModel.mSelectedWishlistItem?.let { wishlistItem ->
                mBinding.img.invisible()
                mBinding.shimmerImg.show()
                Helper.loadImage(
                    url = "speakers/${wishlistItem.dayWise.speaker.image}",
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

                mBinding.title.text = wishlistItem.dayWise.speaker.name
                wishlistItem.dayWise?.let {
                    mBinding.editor.html = it.notes
                }
            }
        }

        mViewModel.saveNotesState.observe(viewLifecycleOwner){
            mBinding.progress.visibility = if (it == SaveNotesState.SAVING) View.VISIBLE else View.GONE
            mBinding.editor.setInputEnabled(it != SaveNotesState.SAVING)

            if (it == SaveNotesState.FAILURE) Toast.makeText(
                requireContext(),
                "Failed to save note, Try again!",
                Toast.LENGTH_SHORT
            ).show()

            if (it == SaveNotesState.SAVED) Toast.makeText(
                requireContext(),
                "Saved Successfully.",
                Toast.LENGTH_SHORT
            ).show()
        }

        return mBinding.root
    }
    private fun saveNotes(){
        if (mMainActivityViewModel.mSelectedProgram == null &&
            mMainActivityViewModel.mSelectedWorkShop == null &&
            mMainActivityViewModel.mSelectedWishlistItem == null) return

        if (mBinding.editor.html == null){
            Toast.makeText(requireContext(), "Please add some note.", Toast.LENGTH_SHORT).show()
            return
        }

        mViewModel.saveNotes(
            notes = mBinding.editor.html,
            programOrWorkshopId = if (mArgs.fromWhere == FROM_PROGRAMS) mMainActivityViewModel.programOrWorkShopId!!
            else if (mArgs.fromWhere == FROM_WORKSHOPS) mMainActivityViewModel.programOrWorkShopId!!
            else mMainActivityViewModel.mSelectedWishlistItem!!.programOrWorkshopId,

            type = if (mArgs.fromWhere == FROM_PROGRAMS) mMainActivityViewModel.mSelectedProgram!!.type
            else if (mArgs.fromWhere == FROM_WORKSHOPS) mMainActivityViewModel.mSelectedWorkShop!!.type
            else mMainActivityViewModel.mSelectedWishlistItem!!.type,

            dayWiseId = if (mArgs.fromWhere == FROM_PROGRAMS) mMainActivityViewModel.mSelectedProgram?.id2!!.toString()
            else if (mArgs.fromWhere == FROM_WORKSHOPS) mMainActivityViewModel.mSelectedWorkShop?.id2!!.toString()
            else mMainActivityViewModel.mSelectedWishlistItem!!.dayWiseId
        )
    }
    companion object{
        private const val TAG = "AddNoteFragment"
        const val FROM_PROGRAMS = 1
        const val FROM_WORKSHOPS = 2
        const val FROM_NOTIFICATIONS = 3
        const val FROM_WISHLIST = 4
        data class SaveNotesParams(
            val deviceId: String,
            val programOrWorkshopId: String,
            val type: String,
            val notes: String,
            val dayWiseId: String
        )
    }
}