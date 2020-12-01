package com.kelvin.bdaypost.data.contactData

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kelvin.bdaypost.data.model.ContactInfo
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class ContactNetwork {
    private val fbAuth = FirebaseAuth.getInstance()
    private val fbDb = Firebase.database.reference

    suspend fun saveContactBirthday(contactInfo: ContactInfo) {
        fbAuth.currentUser?.uid?.let { uid ->
            fbDb.child(uid).child("contact").setValue(contactInfo)
        } ?: run {
            Timber.e("Could not find auth uid")
        }
    }
}