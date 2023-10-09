package com.arianinstitute.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.arianinstitute.repository.ExamRepository
import com.arianinstitute.repository.LoginRepository
import com.arianinstitute.repository.SubmittestRepository
import com.arianinstitute.response.SubmittestResponse
import com.nctapplication.commons.ApiInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class SubmittestViewModel
@Inject
constructor(private val repository: ApiInterface): ViewModel() {
    var loginRepo: SubmittestRepository? = null
    private var mSubmittestResponse: MediatorLiveData<SubmittestResponse>? = null

    init {
        loginRepo = SubmittestRepository(repository)
        mSubmittestResponse = MediatorLiveData()
    }

    open fun submit_test(requestBody: RequestBody): LiveData<SubmittestResponse?>? {
        //to detect coroutine error
        val exceptionHandler = CoroutineExceptionHandler { _, ex ->
            Log.e("CoroutineScope", "Caught ${Log.getStackTraceString(ex)}")
        }
        viewModelScope.launch(exceptionHandler) {
            loginRepo?.let {
                mSubmittestResponse!!.addSource(
                    it.submit_test(requestBody),
                    Observer<SubmittestResponse?> { SubmittestResponse -> mSubmittestResponse!!.setValue(SubmittestResponse) })
            }
        }

        return mSubmittestResponse
    }
}