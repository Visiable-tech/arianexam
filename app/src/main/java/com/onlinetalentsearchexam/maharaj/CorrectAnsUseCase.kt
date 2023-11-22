
package com.onlinetalentsearchexam.maharaj

import com.onlinetalentsearchexam.maharaj.apiservice.ApiService
import com.onlinetalentsearchexam.maharaj.data.models.CorrectAnsResponse
import com.onlinetalentsearchexam.maharaj.retrofit.NetworkUtil
import com.onlinetalentsearchexam.maharaj.retrofit.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import okhttp3.RequestBody
import javax.inject.Inject


class CorrectAnsUseCase @Inject constructor(val apiService: ApiService) {

    operator fun invoke(partMap: MutableMap<String, RequestBody>) = flow<State<CorrectAnsResponse>> {
            apiService.getCorrectAns(partMap).run {
            emit(State.DataState(this))
        }
    }.catch {
        it.printStackTrace()
        emit(NetworkUtil.resolveError(it))
    }.onStart {
        emit(State.LoadingState(true))
    }.onCompletion {
        emit(State.LoadingState(false))
    }.flowOn(Dispatchers.IO)
}
