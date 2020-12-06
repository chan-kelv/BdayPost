package com.kelvin.bdaypost.birthday.add

import androidx.lifecycle.*
import com.kelvin.bdaypost.R
import com.kelvin.bdaypost.data.contactData.ContactRepository
import com.kelvin.bdaypost.data.model.Birthday
import kotlinx.coroutines.launch

class AddBirthdayViewModel(private val contactRepo: ContactRepository): ViewModel() {
    private val _validContactForm = MutableLiveData<ContactFormState>()
    val validContactForm: LiveData<ContactFormState> = _validContactForm

    /**
     * TODO needs to actually validate proper data after fb db set up
     */
    fun validateContactFormState(name: String, address: String): Boolean {
        val nameError = if (name.isBlank()) { R.string.addBday_contactName_error } else null
        val addrError = if (address.isBlank()) { R.string.addBday_contactAddr_error} else null
        val currContactState = ContactFormState(
            nameError,
            addrError,
            nameError == null && addrError == null
        )
        _validContactForm.value = currContactState
        return currContactState.isValidForm
    }

    fun addContactBirthday(name: String, birthday: Birthday, address: String) {
        viewModelScope.launch {
            val contactId = contactRepo.addNewContactInfo(name, address)
            if (!contactId.isNullOrBlank()) {
                contactRepo.addContactBirthday(contactId, birthday.month, birthday.date)
            }
        }
    }

    data class ContactFormState (
        val nameError: Int? = null,
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