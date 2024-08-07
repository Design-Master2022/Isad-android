package com.design_master.isad.model.network.service

import com.design_master.isad.model.constants.Constants
import com.design_master.isad.model.network.response.GetAllScientificProgramsResponse
import com.design_master.isad.model.view_models.fragments.ProgramsFragmentViewModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GetAllScientificProgramsService {
    @POST("${Constants.API_VERSION}scientific_programs")
    fun getAllScientificPrograms(
        @Query("populate") populate: Boolean,
        @Body params: ProgramsFragmentViewModel.ProgramParams
    ):Call<GetAllScientificProgramsResponse>
}