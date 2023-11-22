package com.onlinetalentsearchexam.maharaj.retrofit

import com.onlinetalentsearchexam.commons.Constants
import com.onlinetalentsearchexam.maharaj.retrofit.exception.Failure


sealed class Result<out T: Any?>

class Success<out T: Any?>(val data: T): Result<T>()

class Error(
    val exception: Throwable? = null,
    val failure: Failure = Failure.UnKnownDataLoadException,
    val message: String = exception?.message ?: Constants.UnknownError
): Result<Nothing>()

class Progress(val isLoading: Boolean): Result<Nothing>()