package com.kelvin.bdaypost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kelvin.bdaypost.databinding.FragmentBdayMainBinding

/**
 * A simple [Fragment] subclass.
 * Use the [BirthdayMainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BirthdayMainFragment : Fragment() {

    private var _bdayMainBinding: FragmentBdayMainBinding? = null
    // only use this between onCreateView and onDestroyView
    private val bdayMainBind get() = _bdayMainBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bdayMainBinding = FragmentBdayMainBinding.inflate(inflater, container, false)
        val view = bdayMainBind.root

        bdayMainBind.btnAddBday.setOnClickListener { navToAddBday() }

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
        fun newInstance(param1: String, param2: String) = BirthdayMainFragment()
    }
}