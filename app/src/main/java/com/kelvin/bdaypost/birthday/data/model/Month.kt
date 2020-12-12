package com.kelvin.bdaypost.birthday.data.model

object Month {
    val monthList = listOf(
        "JAN",
        "FEB",
        "MAR",
        "APR",
        "MAY",
        "JUN",
        "JUL",
        "AUG",
        "SEP",
        "OCT",
        "NOV",
        "DEC"
    )

    fun has31Days(month: String): Boolean {
        return (
                month == "JAN" ||
                month == "MAR" ||
                month == "MAY" ||
                month == "JUL" ||
                month == "AUG" ||
                month == "OCT" ||
                month == "DEC")
    }
}