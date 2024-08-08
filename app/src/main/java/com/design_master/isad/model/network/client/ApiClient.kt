package com.design_master.isad.model.network.client

import com.design_master.isad.model.network.service.AddFeedbackService
import com.design_master.isad.model.network.service.AddToWishlistService
import com.design_master.isad.model.network.service.AskQuestionService
import com.design_master.isad.model.network.service.DisableNotificationService
import com.design_master.isad.model.network.service.EnableNotificationService
import com.design_master.isad.model.network.service.FetchAllSpeakersService
import com.design_master.isad.model.network.service.FetchLocationService
import com.design_master.isad.model.network.service.FetchMenuService
import com.design_master.isad.model.network.service.FetchQuestionsService
import com.design_master.isad.model.network.service.GetNotificationsService
import com.design_master.isad.model.network.service.GetAllScientificProgramsService
import com.design_master.isad.model.network.service.GetAllWorkShopsService
import com.design_master.isad.model.network.service.GetHomeDataService
import com.design_master.isad.model.network.service.GetWishlistService
import com.design_master.isad.model.network.service.LoginService
import com.design_master.isad.model.network.service.LoginWithOtpService
import com.design_master.isad.model.network.service.RemoveFromWishlistService
import com.design_master.isad.model.network.service.SaveNotesService
import com.design_master.isad.model.network.service.SendWelcomeNotificationService
import com.design_master.isad.model.network.service.VerifyOtpService


class ApiClient(
    val getAllScientificProgramsService: GetAllScientificProgramsService,
    val getHomeDataService: GetHomeDataService,
    val getAllWorkShopsService: GetAllWorkShopsService,
    val getWishlistService: GetWishlistService,
    val loginService: LoginService,
    val saveNotes: SaveNotesService,
    val addToWishlistService: AddToWishlistService,
    val removeFromWishlistService: RemoveFromWishlistService,
    val enableNotificationService: EnableNotificationService,
    val disableNotificationService: DisableNotificationService,
    val getNotificationsService: GetNotificationsService,
    val fetchAllSpeakersService: FetchAllSpeakersService,
    val sendWelcomeNotificationService: SendWelcomeNotificationService,
    val loginWithOtpService: LoginWithOtpService,
    val verifyOtpService: VerifyOtpService,
    val fetchLocationService: FetchLocationService,
    val addFeedbackService: AddFeedbackService,
    val fetchQuestionsService: FetchQuestionsService,
    val askQuestionService: AskQuestionService,
    val fetchMenuService: FetchMenuService
)