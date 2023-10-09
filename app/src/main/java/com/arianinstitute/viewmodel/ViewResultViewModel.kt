package com.arianinstitute.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.arianinstitute.repository.ExamRepository
import com.arianinstitute.repository.LoginRepository
import com.arianinstitute.repository.SaveQusRepository
import com.arianinstitute.repository.ViewResultRepository
import com.arianinstitute.response.ViewResultResponse
import com.nctapplication.commons.ApiInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ViewResultViewModel
@Inject
constructor(private val repository: ViewResultRepository): ViewModel() {

    private var mViewResultResponse: MediatorLiveData<ViewResultResponse>? = null

    init {
        mViewResultResponse = MediatorLiveData()
    }

    open fun view_result(requestBody: RequestBody): LiveData<ViewResultResponse?>? {
        //to detect coroutine error
        val exceptionHandler = CoroutineExceptionHandler { _, ex ->
            Log.e("CoroutineScope", "Caught ${Log.getStackTraceString(ex)}")
        }
        viewModelScope.launch(exceptionHandler) {
            repository?.let {
                mViewResultResponse!!.addSource(
                    it.view_result(requestBody),
                    Observer<ViewResultResponse?> { ViewResultResponse -> mViewResultResponse!!.setValue(ViewResultResponse) })
            }
        }

        return mViewResultResponse
    }
}