package com.arianinstitute.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.arianinstitute.repository.ExamRepository
import com.arianinstitute.repository.LoginRepository
import com.arianinstitute.repository.StartTestRepository
import com.arianinstitute.response.StartTestResponse
import com.nctapplication.commons.ApiInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class StartTestViewModel
@Inject
constructor(private val repository: StartTestRepository): ViewModel() {
    //var loginRepo: StartTestRepository? = null
    private var mStartTestResponse: MediatorLiveData<StartTestResponse>? = null

    init {
        //loginRepo = StartTestRepository(repository)
        mStartTestResponse = MediatorLiveData()
    }

    open fun start_test(requestBody: RequestBody): LiveData<StartTestResponse?>? {
        //to detect coroutine error
        val exceptionHandler = CoroutineExceptionHandler { _, ex ->
            Log.e("CoroutineScope", "Caught ${Log.getStackTraceString(ex)}")
        }
        viewModelScope.launch(exceptionHandler) {
            repository?.let {
                mStartTestResponse!!.addSource(
                    it.start_test(requestBody),
                    Observer<StartTestResponse?> { StartTestResponse -> mStartTestResponse!!.setValue(StartTestResponse) })
            }
        }

        return mStartTestResponse
    }
}
