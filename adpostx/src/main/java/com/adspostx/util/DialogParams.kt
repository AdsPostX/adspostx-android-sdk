package com.adspostx.util

import java.io.Serializable

data class DialogParams(val title: String = "", val token: String = "android:sdk", val url: String = "https://demo.adpx.us/") :
    Serializable