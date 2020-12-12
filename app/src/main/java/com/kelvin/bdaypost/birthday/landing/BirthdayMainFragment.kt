package com.kelvin.bdaypost.birthday.landing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kelvin.bdaypost.BuildConfig
import com.kelvin.bdaypost.R
import com.kelvin.bdaypost.databinding.FragmentBdayMainBinding

/**
 * A simple [Fragment] subclass.
 * Use the [BirthdayMainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BirthdayMainFragment : Fragment() {

    private var _bdayMainBinding: FragmentBdayMainBinding? = null
    // only use this between onCreateView and onDestroyView
    private val bdayMainBind
        get() = _bdayMainBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bdayMainBinding = FragmentBdayMainBinding.inflate(inflater, container, false)
        val view = bdayMainBind.root

        bdayMainBind.btnAddBday.setOnClickListener { navToAddBday() }

        // HACK: For dev testing-switch between accounts by logging out...TODO
        if (BuildConfig.DEBUG) {
            bdayMainBind.btnLogoutDebug.visibility = View.VISIBLE
            bdayMainBind.btnLogoutDebug.setOnClickListener { logout() }
        }
        return view
    }

    private fun logout() {
        TODO("Not yet implemented")
    }

    private fun navToAddBday() {
        findNavController().navigate(R.id.action_birthdayLandingRoot_to_addBirthdayFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bdayMainBinding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BirthdayMainFragment()
    }
}