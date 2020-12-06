package com.kelvin.bdaypost.util

object DateUtil {
    fun getDayOfYear(month: Int, dateOfMonth: Int): Int {
        return if (month == 2 && dateOfMonth == 29) {
            0
        } else {
            var dateOfYear = 0
            for (i in 1 until month) {
                if (i == 1 ||
                    i == 3 ||
                    i == 5 ||
                    i == 7 ||
                    i == 8 ||
                    i == 10 ||
                    i == 12
                ) {
                    dateOfYear += 31
                } else if (i == 2) {
                    dateOfYear += 28
                } else {
                    dateOfYear += 30
                }
            }
            return dateOfYear + dateOfMonth
        }
    }
}