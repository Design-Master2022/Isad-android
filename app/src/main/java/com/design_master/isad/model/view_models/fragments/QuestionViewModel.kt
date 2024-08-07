package com.design_master.isad.model.view_models.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.design_master.isad.model.network.client.ApiClient
import com.design_master.isad.model.network.response.AskQuestionResponse
import com.design_master.isad.model.network.response.FetchQuestionsResponse
import com.design_master.isad.model.network.response.FetchQuestionsResponseClasses
import com.design_master.isad.model.network.service.AskQuestionParams
import com.design_master.isad.model.network.service.FetchQuestionsParams
import com.design_master.isad.model.network.validator.AskQuestionValidator
import com.design_master.isad.model.network.validator.AskQuestionValidatorCallbacks
import com.design_master.isad.model.network.validator.FetchQuestionsValidator
import com.design_master.isad.model.network.validator.FetchQuestionsValidatorCallbacks
import com.design_master.isad.utils.magician.Magician
import com.design_master.isad.utils.prefs_controller.PrefsController
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(): ViewModel(){
    @Inject lateinit var  mPrefsController: PrefsController
    @Inject lateinit var mApiClient: ApiClient
    @Inject lateinit var mMagician: Magician

    enum class FetchQuestionsState{
        IDLE, FAILURE, UNAUTHORIZED, FETCHING, FETCHED
    }
    val fetchQuestionsState = MutableLiveData(FetchQuestionsState.IDLE)
    var questions = mutableListOf<FetchQuestionsResponseClasses.Question>()
    fun fetchQuestions(){
        fetchQuestionsState.value = FetchQuestionsState.FETCHING
        mApiClient.fetchQuestionsService.fetch(
            params = FetchQuestionsParams(
                deviceId = mPrefsController.getUserUid()!!
            )
        ).enqueue(object: Callback<FetchQuestionsResponse>{
                override fun onResponse(
                    call: Call<FetchQuestionsResponse>,
                    response: Response<FetchQuestionsResponse>
                ) {
                    FetchQuestionsValidator.validate(
                        response = response,
                        callbacks = object: FetchQuestionsValidatorCallbacks{
                            override fun onUnAuthorized() {
                                Log.d(TAG, "onUnAuthorized: ")
                                fetchQuestionsState.value = FetchQuestionsState.UNAUTHORIZED
                            }
                            override fun onFailedToFetchQuestions() {
                                Log.d(TAG, "onFailedToFetchSpeakers: ")
                                fetchQuestionsState.value = FetchQuestionsState.FAILURE
                            }
                            override fun onQuestionsFetched(questions: List<FetchQuestionsResponseClasses.Question>) {
                                Log.d(TAG, "onSpeakersFetched: ")
                                this@QuestionViewModel.questions = questions.toMutableList()
                                fetchQuestionsState.value = FetchQuestionsState.FETCHED
                            }
                        }
                    )
                }
                override fun onFailure(call: Call<FetchQuestionsResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                    t.printStackTrace()
                    fetchQuestionsState.value = FetchQuestionsState.FAILURE
                }
            })
    }

    enum class AskQuestionState{
        IDLE, FAILURE, UNAUTHORIZED, ASKING, ASKED
    }
    val askQuestionState = MutableLiveData(AskQuestionState.IDLE)
    fun askQuestion(
        name: String,
        sessionName: String,
        question: String
    ){
        askQuestionState.value = AskQuestionState.ASKING
        mApiClient.askQuestionService.ask(
            params = AskQuestionParams(
                deviceId = mPrefsController.getUserUid()!!,
                name = name,
                sessionName = sessionName,
                question = question
            )
        ).enqueue(object: Callback<AskQuestionResponse>{
            override fun onResponse(
                call: Call<AskQuestionResponse>,
                response: Response<AskQuestionResponse>
            ) {
                AskQuestionValidator.validate(
                    response = response,
                    callbacks = object: AskQuestionValidatorCallbacks{
                        override fun onUnAuthorized() {
                            Log.d(TAG, "onUnAuthorized: ")
                            askQuestionState.value = AskQuestionState.UNAUTHORIZED
                        }
                        override fun onFailedToAskQuestion() {
                            Log.d(TAG, "onFailedToAskQuestion: ")
                            askQuestionState.value = AskQuestionState.FAILURE
                        }
                        override fun onQuestionAsked() {
                            Log.d(TAG, "onQuestionAsked: ")
                            askQuestionState.value = AskQuestionState.ASKED
                        }
                    }
                )
            }
            override fun onFailure(call: Call<AskQuestionResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
                t.printStackTrace()
                askQuestionState.value = AskQuestionState.FAILURE
            }
        })
    }

    companion object{
        private const val TAG = "FeedbackFragmentViewMod"
    }
}