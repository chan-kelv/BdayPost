package com.kelvin.bdaypost.ui.birthday.add

import androidx.lifecycle.*
import com.kelvin.bdaypost.R
import com.kelvin.bdaypost.data.contactData.ContactRepository
import kotlinx.coroutines.launch

class AddBirthdayViewModel(private val contactRepo: ContactRepository): ViewModel() {
    private val _validContactForm = MutableLiveData<ContactFormState>()
    val validContactForm: LiveData<ContactFormState> = _validContactForm

    /**
     * TODO needs to actually validate proper data after fb db set up
     */
    fun validateContactFormState(name: String, birthdate: String, address: String): Boolean {
        val nameError = if (name.isBlank()) { R.string.addBday_contactName_error } else null
        val dateError = if (birthdate.isBlank()) { R.string.addBday_contactDate_error } else null
        val addrError = if (address.isBlank()) { R.string.addBday_contactAddr_error} else null
        val currContactState = ContactFormState(
            nameError,
            dateError,
            addrError,
            nameError == null && dateError == null && addrError == null
        )
        _validContactForm.value = currContactState
        return currContactState.isValidForm
    }

    fun addContactBirthdate(name: String, birthdate: String, address: String) {
        viewModelScope.launch { contactRepo.addNewContactBirthdate(name, birthdate, address) }
    }

    data class ContactFormState (
        val nameError: Int? = null,
        val dateError: Int? = null,
        val addrError: Int? = null,
        val isValidForm: Boolean = false
    )

    @Suppress("UNCHECKED_CAST")
    class AddBirthdayVMFactory(private val contactRepo: ContactRepository = ContactRepository.getInstance()) :
            ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AddBirthdayViewModel(contactRepo) as T
        }
    }
}