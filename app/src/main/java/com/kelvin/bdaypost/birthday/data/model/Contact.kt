package com.kelvin.bdaypost.birthday.data.model

import timber.log.Timber

data class Contact(var birthday: Birthday, var contactInfo: ContactInfo)

data class Birthday(val month: Int, val date: Int) {
    companion object {
        /**
         * If valid Month keys are given and date is an int, return a valid birthday
         */
        fun generateBirthday(month: String, date: String): Birthday? {
            try {
                val monthInt = Month.monthList.indexOf(month)
                val dateInt = date.toInt()
                if (monthInt > 0) // month will be -1 if invalid
                    return Birthday(
                        monthInt,
                        dateInt
                    ) else {
                    throw Exception("month invalid")
                }
            } catch (e: Exception) {
                Timber.e(e, "Could not generate Birthday")
            }
            return null
        }

        /**
         * Converts the month and day to a date of the year range of 0..365
         * ASSUMES that 0 == Feb 29
         * The remaining date ranges are Jan 1 == 1, Jan 2 == 2...Dec 31 == 365
         */
        fun Birthday.getDayOfYear(): Int {
            return if (month == 2 && date == 29) {
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
                return dateOfYear + month
            }
        }
    }
}

data class ContactInfo(
    val name: String,
    val address: String
) {
    var uuid: String = ""
}