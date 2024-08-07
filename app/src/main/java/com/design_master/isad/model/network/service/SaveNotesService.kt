package com.design_master.isad.model.network.service

import com.design_master.isad.model.constants.Constants
import com.design_master.isad.model.network.response.SaveNotesResponse
import com.design_master.isad.ui.fragments.AddNoteFragment
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SaveNotesService {
    @POST("${Constants.API_VERSION}notes")
    fun saveNotes(
        @Body saveNotesParam: AddNoteFragment.Companion.SaveNotesParams
    ): Call<SaveNotesResponse>
}