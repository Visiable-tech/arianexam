package com.arianinstitute.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.arianinstitute.repository.ExamRepository
import com.arianinstitute.repository.LoginRepository
import com.arianinstitute.repository.SaveQusRepository
import com.arianinstitute.response.SaveQusResponse
import com.nctapplication.commons.ApiInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class SavequsViewModel
@Inject
constructor(private val repository: SaveQusRepository): ViewModel() {
    //var loginRepo: SaveQusRepository? = null
    private var mSaveQusResponse: MediatorLiveData<SaveQusResponse>? = null

    init {
        //loginRepo = SaveQusRepository(repository)
        mSaveQusResponse = MediatorLiveData()
    }

    open fun save_question(requestBody: RequestBody): LiveData<SaveQusResponse?>? {
        //to detect coroutine error
        val exceptionHandler = CoroutineExceptionHandler { _, ex ->
            Log.e("CoroutineScope", "Caught ${Log.getStackTraceString(ex)}")
        }
        viewModelScope.launch(exceptionHandler) {
            repository?.let {
                mSaveQusResponse!!.addSource(
                    it.save_question(requestBody),
                    Observer<SaveQusResponse?> { SaveQusResponse -> mSaveQusResponse!!.setValue(SaveQusResponse) })
            }
        }

        return mSaveQusResponse
    }
}