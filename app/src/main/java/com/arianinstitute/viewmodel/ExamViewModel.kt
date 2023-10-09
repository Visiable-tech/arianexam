package com.arianinstitute.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.arianinstitute.repository.ExamRepository
import com.arianinstitute.repository.LoginRepository
import com.arianinstitute.repository.SaveQusRepository
import com.arianinstitute.response.ExamResponse
import com.nctapplication.commons.ApiInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ExamViewModel
@Inject
constructor(private val repository: ExamRepository): ViewModel() {
    //var loginRepo: ExamRepository? = null
    private var mExamResponse: MediatorLiveData<ExamResponse>? = null

    init {
        //loginRepo = ExamRepository(repository)
        mExamResponse = MediatorLiveData()
    }

    open fun examtest(requestBody: RequestBody): LiveData<ExamResponse?>? {
        //to detect coroutine error
        val exceptionHandler = CoroutineExceptionHandler { _, ex ->
            Log.e("CoroutineScope", "Caught ${Log.getStackTraceString(ex)}")
        }
        viewModelScope.launch(exceptionHandler) {
            repository?.let {
                mExamResponse!!.addSource(
                    it.examtest(requestBody),
                    Observer<ExamResponse?> { ExamResponse -> mExamResponse!!.setValue(ExamResponse) })
            }
        }

        return mExamResponse
    }
}