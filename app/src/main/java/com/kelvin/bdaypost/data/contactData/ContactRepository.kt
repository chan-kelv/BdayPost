package com.kelvin.bdaypost.data.contactData

import com.kelvin.bdaypost.data.Result
import com.kelvin.bdaypost.data.model.ContactInfo
import com.kelvin.bdaypost.util.DateUtil
import timber.log.Timber

class ContactRepository private constructor(
    private var contactNetwork: ContactNetwork = ContactNetwork()
) {
    suspend fun addNewContactInfo(name: String, address: String): String? {
        val contactInfo = ContactInfo(name, address)
        val contactNodeIdTask = contactNetwork.saveContactInfo(contactInfo)
        return if (contactNodeIdTask is Result.Success && contactNodeIdTask.data.isNotBlank()) {
            contactNodeIdTask.data
        } else {
            if (contactNodeIdTask is Result.Error) {
                Timber.e(contactNodeIdTask.exception)
            }
            null
        }
    }

    suspend fun addContactBirthday(contactId: String, birthMonth: Int, birthDate: Int): Boolean {
        val birthDateOfYear = DateUtil.getDayOfYear(birthMonth, birthDate)
        val successTask = contactNetwork.recordContactBirthday(contactId, birthDateOfYear)
        return successTask is Result.Success
    }

    companion object {
        @Volatile
        private var instance: ContactRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ContactRepository().also {
                instance = it
            }
        }
    }
}