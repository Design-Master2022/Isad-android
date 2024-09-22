package com.design_master1.isad.model.network.service

import com.design_master1.isad.model.constants.Constants
import com.design_master1.isad.model.network.response.GetAllScientificProgramsResponse
import com.design_master1.isad.model.network.response.GetScientificProgramWebLinksResponse
import com.design_master1.isad.model.view_models.fragments.ProgramsFragmentViewModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GetScientificProgramsWebLinksService {
    @GET("${Constants.API_VERSION}program_link")
    fun get(
//        @Query("populate") populate: Boolean,
//        @Body params: ProgramsFragmentViewModel.ProgramParams
    ):Call<GetScientificProgramWebLinksResponse>
}