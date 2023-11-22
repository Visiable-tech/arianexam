
package com.onlinetalentsearchexam.maharaj

import com.onlinetalentsearchexam.maharaj.apiservice.ApiService
import com.onlinetalentsearchexam.maharaj.data.models.IntroResponse
import com.onlinetalentsearchexam.maharaj.retrofit.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class IntroUseCase @Inject constructor(val apiService: ApiService) {
    operator fun invoke() = flow<State<IntroResponse>> {
            apiService.getIntro().run {
                emit(State.DataState(this))
        }
    }.flowOn(Dispatchers.IO)
}
