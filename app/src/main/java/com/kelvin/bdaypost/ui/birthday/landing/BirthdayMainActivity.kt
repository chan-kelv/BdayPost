package com.kelvin.bdaypost.ui.birthday.landing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kelvin.bdaypost.databinding.ActivityBirthdayMainBinding

class BirthdayMainActivity : AppCompatActivity() {

    private lateinit var bdayMainBinding: ActivityBirthdayMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bdayMainBinding = ActivityBirthdayMainBinding.inflate(layoutInflater)
        val landingView = bdayMainBinding.root
        setContentView(landingView)
    }
}