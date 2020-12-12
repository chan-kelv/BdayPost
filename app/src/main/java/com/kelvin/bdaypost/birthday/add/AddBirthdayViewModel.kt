package com.kelvin.bdaypost.birthday.add

import androidx.lifecycle.*
import com.kelvin.bdaypost.R
import com.kelvin.bdaypost.birthday.data.ContactRepository
import com.kelvin.bdaypost.birthday.data.model.Birthday
import com.kelvin.bdaypost.birthday.data.model.Contact
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class AddBirthdayViewModel(private val contactRepo: ContactRepository) : ViewModel() {
    private val _validContactForm = MutableLiveData<ContactFormState>()
    val validContactForm: LiveData<ContactFormState> = _validContactForm

    private val _addContactResult = MutableLiveData<ContactAddedResult>()
    val addContactResult: LiveData<ContactAddedResult> = _addContactResult

    /**
     * Validates the state of adding a new contact's bday
     * NameError - The name must not be blank
     * AddressError - The address must not be blank
     * Birthday - is a drop down and default to 01/01 and can not be invalid
     *
     * Will update the liveData for validContactForm
     * @return isValidForm - if the form is filled in correctly
     */
    fun validateContactFormState(name: String, address: String): Boolean {
        val nameError = if (name.isBlank()) {
            R.string.addBday_contactName_error
        } else null
        val addrError = if (address.isBlank()) {
            R.string.addBday_contactAddr_error
        } else null
        val currContactState = ContactFormState(
            nameError,
            addrError,
            nameError == null && addrError == null
        )
        _validContactForm.value = currContactState
        return currContactState.isValidForm
    }

    /**
     * Assumes params are all valididated
     * Will add a contact to the firebase database separated into contact info and birthday
     * - starts by adding name, address to contact info list -> generates uuid
     * - if successful, will add birthday in the range 0-365 using the generated uuid
     * - updates the live data for result
     */
    fun addContactBirthday(name: String, birthday: Birthday, address: String) {
        viewModelScope.launch {
            val contactId = contactRepo.addNewContactInfo(name, address)
            contactId?.let { contactInfo ->
                if (contactId.uuid.isNotBlank()) {
                    val recordedBday = contactRepo.addContactBirthday(
                        contactInfo.uuid,
                        birthday.month,
                        birthday.date
                    )
                    recordedBday?.let { bday ->
                        val contact = Contact(bday, contactInfo)
                        _addContactResult.value = ContactAddedResult(success = contact)
                    }?:run {
                        Timber.e("Error prevented birthday being added")
                        _addContactResult.value =
                            ContactAddedResult(error = R.string.addBday_bday_failed)
                    }
                } else {
                    Timber.e("UUID was blank! Bday not added")
                    _addContactResult.value =
                        ContactAddedResult(error = R.string.addBday_bday_failed)
                }
            }
        }
    }

    data class ContactFormState(
        val nameError: Int? = null,
        val addrError: Int? = null,
        val isValidForm: Boolean = false
    )

    data class ContactAddedResult(
        val success: Contact? = null,
        val error: Int? = null
    )

    @Suppress("UNCHECKED_CAST")
    class AddBirthdayVMFactory(private val contactRepo: ContactRepository = ContactRepository.getInstance()) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AddBirthdayViewModel(contactRepo) as T
        }
    }
}