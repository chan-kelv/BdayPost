package com.kelvin.bdaypost.ui.landing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kelvin.bdaypost.R
import com.kelvin.bdaypost.databinding.ActivityLandingBinding

class LandingActivity : AppCompatActivity() {

    private lateinit var landingBinding: ActivityLandingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        landingBinding = ActivityLandingBinding.inflate(layoutInflater)
        val landingView = landingBinding.root
        setContentView(landingView)
    }
}