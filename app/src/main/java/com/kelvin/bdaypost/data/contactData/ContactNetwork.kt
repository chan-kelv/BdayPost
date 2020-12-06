package com.kelvin.bdaypost.data.contactData

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kelvin.bdaypost.data.Result
import com.kelvin.bdaypost.data.model.ContactInfo
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class ContactNetwork {
    private val fbAuth = FirebaseAuth.getInstance()
    private val fbDb = Firebase.database.reference

    suspend fun saveContactBirthday(contactInfo: ContactInfo): Result<Void> {
        fbAuth.currentUser?.uid?.let { uid ->
            val contactRes = fbDb.child(uid).child("contact-info")
                .push().setValue(contactInfo).await()
            return Result.Success(contactRes)
        } ?: run {
            Timber.e("Could not find auth uid")
            return Result.Error(Exception("Could not find auth uid"))
        }
    }
}