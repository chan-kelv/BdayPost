package com.kelvin.bdaypost.birthday.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kelvin.bdaypost.BuildConfig
import com.kelvin.bdaypost.data.model.Birthday
import com.kelvin.bdaypost.data.model.Month
import com.kelvin.bdaypost.databinding.FragmentAddBirthdayBinding
import timber.log.Timber

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

        context?.let {
            addBdayBinding.spinAddBdayMonth.adapter =
                ArrayAdapter(it, android.R.layout.simple_spinner_item, Month.monthList)

            addBdayBinding.spinAddBdayMonth.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Timber.d("Nothing selected")
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    changeBdayDateRange(Month.monthList[position])
                }

            }
        }

        return view
    }

    private fun changeBdayDateRange(monthCode: String) {
        val dayList =
            when {
                Month.has31Days(monthCode) -> {
                    (1..31).toList()
                }
                monthCode == "FEB" -> {
                    (1..29).toList()
                }
                else -> {
                    (1..30).toList()
                }
            }
        context?.let {
            addBdayBinding.spinAddBdayDate.adapter =
                ArrayAdapter(it, android.R.layout.simple_spinner_item, dayList)
        }
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
            contactState.addrError?.let {
                addBdayBinding.inputAddBdayContactAddr.error = this.getString(it) }
        }
    }

    private fun saveBday() {
        val name = addBdayBinding.inputAddBdayContactName.text.toString()
        val addr = addBdayBinding.inputAddBdayContactAddr.text.toString()
        addBdayVM?.let { vm ->
            if (vm.validateContactFormState(name, addr)) {
                val bdayMonth = addBdayBinding.spinAddBdayMonth.selectedItem.toString()
                val bdayDate = addBdayBinding.spinAddBdayDate.selectedItem.toString()
                val birthday = Birthday.generateBirthday(bdayMonth, bdayDate)
                vm.addContactBirthday(name, birthday, addr)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddBirthdayFragment()
    }
}