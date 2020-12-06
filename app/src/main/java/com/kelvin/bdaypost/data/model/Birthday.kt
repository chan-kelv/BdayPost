package com.kelvin.bdaypost.data.model

data class Birthday(val month: Int, val date: Int) {
    companion object {
        fun generateBirthday(month: String, date: String): Birthday {
            val monthInt = Month.monthList.indexOf(month)
            val dateInt = date.toInt()
            return Birthday(monthInt, dateInt)
        }
    }
}