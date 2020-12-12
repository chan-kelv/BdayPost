package com.kelvin.bdaypost.birthday.add

import android.content.Context
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
import com.kelvin.bdaypost.birthday.data.model.Birthday
import com.kelvin.bdaypost.birthday.data.model.Month
import com.kelvin.bdaypost.databinding.FragmentAddBirthdayBinding
import com.kelvin.bdaypost.util.TextResUtil.showToast
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
        return addBdayBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // create VM
        addBdayVM = activity?.run {
            ViewModelProvider(this, AddBirthdayViewModel.AddBirthdayVMFactory())
                .get(AddBirthdayViewModel::class.java)
        }
        // Add VM observers
        addBdayVM?.run {
            this.validContactForm.observe(
                this@AddBirthdayFragment.viewLifecycleOwner,
                Observer { observeContactState(it) }
            )

            this.addContactResult.observe(
                this@AddBirthdayFragment.viewLifecycleOwner,
                Observer { observeAddContact(it) }
            )
        }


        // configure date picker for birthday spinner
        context?.let { addDatePicker(it) }
        // add btn listener for confirm saving birthday
        addBdayBinding.btnAddBdayAddContact.setOnClickListener { saveBday() }

        // debug address for easy testing
        if (BuildConfig.DEBUG) {
            addBdayBinding.inputAddBdayContactName.setText("Kelvin")
            addBdayBinding.inputAddBdayContactAddr.setText("742 evergreen terrace")
        }
    }

    /**
     * Populates the month birthday picker with array data onViewCreated
     * Adds listener to change the date to the appropriate range based on month selected
     */
    private fun addDatePicker(context: Context) {
        addBdayBinding.spinAddBdayMonth.adapter =
            ArrayAdapter(context, android.R.layout.simple_spinner_item, Month.monthList)

        addBdayBinding.spinAddBdayMonth.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Timber.d("Nothing selected")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                changeBdayDateRange(Month.monthList[position])
            }
        }
    }

    /**
     * Watches for add birthday saving results
     */
    private fun observeAddContact(contactAddedResult: AddBirthdayViewModel.ContactAddedResult?) {
        contactAddedResult?.let {
            it.success?.let { contact -> this.context?.showToast("${contact.contactInfo.name} saved!")}
            it.error?.let { errRes -> this.context?.showToast(errRes) }
        }
    }

    /**
     * Watches for valid add contact fields prior to saving
     */
    private fun observeContactState(contactState: AddBirthdayViewModel.ContactFormState?) {
        contactState?.let {
            contactState.nameError?.let {
                addBdayBinding.inputAddBdayContactName.error = this.getString(it) }
            contactState.addrError?.let {
                addBdayBinding.inputAddBdayContactAddr.error = this.getString(it) }
        }
    }

    /**
     * Validates the fields and then saves the contact
     */
    private fun saveBday() {
        val name = addBdayBinding.inputAddBdayContactName.text.toString()
        val addr = addBdayBinding.inputAddBdayContactAddr.text.toString()
        addBdayVM?.let { vm ->
            if (vm.validateContactFormState(name, addr)) {
                val bdayMonth = addBdayBinding.spinAddBdayMonth.selectedItem.toString()
                val bdayDate = addBdayBinding.spinAddBdayDate.selectedItem.toString()
                Birthday.generateBirthday(bdayMonth, bdayDate)?.let { birthday ->
                    vm.addContactBirthday(name, birthday, addr)
                }
            }
        }
    }

    /**
     * Depending on the month selected, populate the date picker with the correct date range
     */
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

    companion object {
        @JvmStatic
        fun newInstance() = AddBirthdayFragment()
    }
}