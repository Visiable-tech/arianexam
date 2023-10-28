package com.onlinetalentsearchexam.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.onlinetalentsearchexam.repository.LoginRepository
import com.onlinetalentsearchexam.response.ApiResponse
import com.avision.commons.ApiInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject
constructor(private val repository: ApiInterface): ViewModel() {
    var loginRepo: LoginRepository? = null
    private var mApiResponse: MediatorLiveData<ApiResponse>? = null

    init {
        loginRepo = LoginRepository(repository)
        mApiResponse = MediatorLiveData()
    }

    open fun login(requestBody: RequestBody): LiveData<ApiResponse?>? {
        //to detect coroutine error
        val exceptionHandler = CoroutineExceptionHandler { _, ex ->
            Log.e("CoroutineScope", "Caught ${Log.getStackTraceString(ex)}")
        }
        viewModelScope.launch(exceptionHandler) {
            loginRepo?.let {
                mApiResponse!!.addSource(
                    it.login(requestBody),
                    Observer<ApiResponse?> { apiResponse -> mApiResponse!!.setValue(apiResponse) })
            }
        }

        return mApiResponse
    }
}