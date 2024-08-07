package com.design_master.isad.model.view_models.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.design_master.isad.model.network.client.ApiClient
import com.design_master.isad.model.network.response.SaveNotesResponse
import com.design_master.isad.model.network.response.SaveNotesResponseClasses
import com.design_master.isad.model.network.validator.SaveNotesValidator
import com.design_master.isad.model.network.validator.SaveNotesValidatorCallbacks
import com.design_master.isad.ui.fragments.AddNoteFragment.Companion.SaveNotesParams
import com.design_master.isad.utils.magician.Magician
import com.design_master.isad.utils.prefs_controller.PrefsController
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AddNoteFragmentViewModel @Inject constructor(): ViewModel(){
    @Inject lateinit var  mPrefsController: PrefsController
    @Inject lateinit var mApiClient: ApiClient
    @Inject lateinit var mMagician: Magician

    enum class SaveNotesState{
        IDLE, FAILURE, SAVING, SAVED, UNAUTHORIZED
    }
    val saveNotesState = MutableLiveData(SaveNotesState.IDLE)
    fun saveNotes(
        notes: String,
        programOrWorkshopId: String,
        type: String,
        dayWiseId: String
    ){
        saveNotesState.value = SaveNotesState.SAVING
        mApiClient.saveNotes.saveNotes(
            SaveNotesParams(
                deviceId = mPrefsController.getUserUid()!!,
                programOrWorkshopId = programOrWorkshopId,
                type = type,
                notes = notes,
                dayWiseId = dayWiseId
            )
        ).enqueue(object: Callback<SaveNotesResponse>{
                override fun onResponse(
                    call: Call<SaveNotesResponse>,
                    response: Response<SaveNotesResponse>
                ) {
                    SaveNotesValidator.validate(
                        response = response,
                        callbacks = object: SaveNotesValidatorCallbacks{
                            override fun onUnAuthorized() {
                                Log.d(TAG, "onUnAuthorized: ")
                                saveNotesState.value = SaveNotesState.UNAUTHORIZED
                            }

                            override fun onFailedToSaveNotes() {
                                Log.d(TAG, "onFailedToSaveNotes: ")
                                saveNotesState.value = SaveNotesState.FAILURE
                            }

                            override fun onNotesSaved(note: SaveNotesResponseClasses.Note) {
                                Log.d(TAG, "onNotesSaved: ")
                                saveNotesState.value = SaveNotesState.SAVED
                            }
                        }
                    )
                }

                override fun onFailure(call: Call<SaveNotesResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                    t.printStackTrace()
                    saveNotesState.value = SaveNotesState.FAILURE
                }
            })
    }
    companion object{
        private const val TAG = "AddNoteFragmentViewMode"
    }
}