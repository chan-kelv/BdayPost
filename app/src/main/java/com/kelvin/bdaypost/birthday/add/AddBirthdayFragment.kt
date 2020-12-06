package com.kelvin.bdaypost.birthday.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kelvin.bdaypost.BuildConfig
import com.kelvin.bdaypost.databinding.FragmentAddBirthdayBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AddBirthdayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddBirthdayFragment : Fragment() {
    private var _addBdayBinding: FragmentAddBirthdayBinding? = null
    // only use this between onCreateView and onDestroyView
    private val addBdayBinding
        get() = _addBdayBinding!!

    private var addBdayVM: AddBirthdayViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _addBdayBinding = FragmentAddBirthdayBinding.inflate(inflater, container, false)
        val view = addBdayBinding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addBdayVM = activity?.run {
            ViewModelProvider(this, AddBirthdayViewModel.AddBirthdayVMFactory())
                .get(AddBirthdayViewModel::class.java)
        }

        addBdayVM?.run {
            this.validContactForm.observe(
                this@AddBirthdayFragment.viewLifecycleOwner,
                Observer { observeContactState(it) }
            )
        }

        addBdayBinding.btnAddBdayAddContact.setOnClickListener { saveBday() }

        if (BuildConfig.DEBUG) {
            addBdayBinding.inputAddBdayContactName.setText("Kelvin")
            addBdayBinding.inputAddBdayContactAddr.setText("123 fake street")
        }
    }

    private fun observeContactState(contactState: AddBirthdayViewModel.ContactFormState?) {
        contactState?.let {
            contactState.nameError?.let {
                addBdayBinding.inputAddBdayContactName.error = this.getString(it) }
            contactState.dateError?.let {
                addBdayBinding.inputAddBdayContactDate.error = this.getString(it) }
            contactState.addrError?.let {
                addBdayBinding.inputAddBdayContactAddr.error = this.getString(it) }
        }
    }

    private fun saveBday() {
        val name = addBdayBinding.inputAddBdayContactName.text.toString()
        val birthdate = addBdayBinding.inputAddBdayContactDate.text.toString()
        val addr = addBdayBinding.inputAddBdayContactAddr.text.toString()
        addBdayVM?.let { vm ->
            if (vm.validateContactFormState(name, birthdate, addr)) {
                vm.addContactBirthdate(name, birthdate, addr)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddBirthdayFragment()
    }
}