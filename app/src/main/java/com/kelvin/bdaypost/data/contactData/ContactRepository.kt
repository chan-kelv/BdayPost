package com.kelvin.bdaypost.data.contactData

import com.kelvin.bdaypost.data.model.ContactInfo

class ContactRepository private constructor(
    private var contactNetwork: ContactNetwork = ContactNetwork()
) {
    suspend fun addNewContactBirthdate(name: String, birthdate: String, address: String) {
        val contactInfo = ContactInfo(name, birthdate, address)
        contactNetwork.saveContactBirthday(contactInfo)
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