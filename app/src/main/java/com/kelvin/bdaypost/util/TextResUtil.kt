package com.kelvin.bdaypost.util

import android.content.Context
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.Toast

class TextResUtil {
    companion object {
        fun Context.getStringFromRes(stringRes: Int): String? = this.getString(stringRes)

        fun Context.showToast(message: String, duration: Int = Toast.LENGTH_LONG) =
            Toast.makeText(this, message, duration).show()

        fun Context.showToast(messageRes: Int, duration: Int = Toast.LENGTH_LONG) =
            Toast.makeText(this, messageRes, duration).show()

        fun createUnderlineText(text: String, fromIndex: Int = 0, toIndex: Int = text.length): SpannableString {
            val spannableString = SpannableString(text)
            spannableString.setSpan(UnderlineSpan(), fromIndex, toIndex, 0)
            return spannableString
        }
    }
}