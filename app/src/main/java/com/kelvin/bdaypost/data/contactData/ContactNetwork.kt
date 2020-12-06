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

    suspend fun saveContactInfo(contactInfo: ContactInfo): Result<String> {
        fbAuth.currentUser?.uid?.let { uid ->
            return try {
                val contactNode = fbDb.child(uid).child("contact-info").push()
                val contactNodeKey = contactNode.key ?: ""
                contactInfo.uuid = contactNodeKey
                contactNode.setValue(contactInfo).await()
                Result.Success(contactNodeKey)
            } catch (e: Exception) {
                Timber.e(e)
                Result.Error(e)
            }
        } ?: run {
            Timber.e("Could not find auth uid")
            return Result.Error(Exception("Could not find auth uid"))
        }
    }

    suspend fun recordContactBirthday(contactId: String, dayOfYear: Int): Result<String> {
        fbAuth.currentUser?.uid?.let {
            return try {
                val bdayUid = fbDb.child(it).child("contact-birthday").child(dayOfYear.toString()).push()
                bdayUid.setValue(contactId).await()
                Result.Success(bdayUid.key ?: "")
            } catch (e: Exception) {
                Timber.e(e)
                Result.Error(e)
            }
        } ?: run {
            Timber.e("Could not find auth uid")
            return Result.Error(Exception("Could not find auth uid"))
        }
    }
}