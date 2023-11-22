package com.onlinetalentsearchexam.maharaj.retrofit

import android.content.Context
import com.visiabletech.avision.maharaj.core.extension.networkInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
open class NetworkHandler @Inject constructor(@ApplicationContext  val context: Context) {
    open val isConnected get() = context.networkInfo?.isConnectedOrConnecting
}