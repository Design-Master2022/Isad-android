package com.design_master.isad.model.network.validator

import com.design_master.isad.model.constants.ApiResponseConstants
import com.design_master.isad.model.network.response.SaveNotesResponse
import com.design_master.isad.model.network.response.SaveNotesResponseClasses
import retrofit2.Response

interface SaveNotesValidatorCallbacks {
    fun onUnAuthorized()
    fun onFailedToSaveNotes()
    fun onNotesSaved(note: SaveNotesResponseClasses.Note)
}

class SaveNotesValidator {
    companion object {
        fun validate(
            response: Response<SaveNotesResponse>,
            callbacks: SaveNotesValidatorCallbacks
        ) {
            when {
                response.isSuccessful -> {
                    response.body()?.let { trendalResponse ->
                        trendalResponse.data?.let {
                            callbacks.onNotesSaved(it.note)
                        }
                    }
                }
                else -> {
                    when(response.code()){
                        ApiResponseConstants.UNAUTHORIZED -> callbacks.onUnAuthorized()
                        else -> callbacks.onFailedToSaveNotes()
                    }
                }
            }
        }
    }
}