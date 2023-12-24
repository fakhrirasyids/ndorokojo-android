package com.ndorokojo.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

object Constants {
    const val API_ENDPOINT = "HEHEHE"

    const val SPLASH_MAIN_TIMEOUT = 2000L

    const val IS_FROM_AUTH = "is_from_auth"

    const val USER_ACCESS_TOKEN = "user_access_token"

    enum class Ternak {
        DOMBA,
        KAMBING,
        SAPI_POTONG,
        SAPI_PERAH,
        KERBAU,
        KELINCI,
        ITIK,
        AYAM_PETELUR,
        ENTHOK,
        AYAM_POTONG,
        AYAM_KAMPUNG,
        BABI,
        PUYUH,
        BURUNG_KICAU,
        MERPATI,
        REPTILE
    }

    fun alertDialogMessage(context: Context, message: String, title: String? = null) {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)

        with(builder)
        {
            if (title != null) {
                setTitle(title)
            }
            setMessage(message)
            setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            show()
        }
    }
}