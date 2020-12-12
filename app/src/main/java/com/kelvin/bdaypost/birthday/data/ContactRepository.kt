package com.kelvin.bdaypost.birthday.data

import com.kelvin.bdaypost.birthday.data.model.Birthday
import com.kelvin.bdaypost.birthday.data.model.Birthday.Companion.getDayOfYear
import com.kelvin.bdaypost.data.Result
import com.kelvin.bdaypost.birthday.data.model.ContactInfo
import timber.log.Timber

class ContactRepository private constructor(
    private var contactNetwork: ContactNetwork = ContactNetwork()
) {
    /**
     * Builds Contact info out of name and address and then saves it to the network
     * The success contactInfo will include a uuidString generated when saving
     * @return The contact info (including contactId:uuidString) if properly saved, else null.
     */
    suspend fun addNewContactInfo(name: String, address: String): ContactInfo? {
        val contactInfo = ContactInfo(name, address)
        val contactNodeIdTask = contactNetwork.saveContactInfo(contactInfo)
        return if (contactNodeIdTask is Result.Success) {
            contactNodeIdTask.data
        } else {
            if (contactNodeIdTask is Result.Error) {
                Timber.e(contactNodeIdTask.exception)
            }
            null
        }
    }

    /**
     * Saves the Birthday of the given contact (identified by the contactId)
     * @return the Birthday of the contact
     */
    suspend fun addContactBirthday(contactId: String, birthMonth: Int, birthDate: Int): Birthday? {
        val birthday = Birthday(birthMonth, birthDate)
        val birthDateOfYear = birthday.getDayOfYear()
        val successTask = contactNetwork.recordContactBirthday(contactId, birthDateOfYear)
        return if (successTask is Result.Success) {
            Timber.d("Birthday recorded under nodeId: ${successTask.data}")
            birthday
        } else {
            Timber.e((successTask as Result.Error).exception)
            null
        }
    }

    companion object {
        @Volatile
        private var instance: ContactRepository? = null

        fun getInstance() = instance
            ?: synchronized(this) {
            instance
                ?: ContactRepository().also {
                instance = it
            }
        }
    }
}