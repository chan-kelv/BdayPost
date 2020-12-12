package com.kelvin.bdaypost.birthday.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kelvin.bdaypost.data.Result
import com.kelvin.bdaypost.birthday.data.model.ContactInfo
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class ContactNetwork {
    private val fbAuth = FirebaseAuth.getInstance()
    private val fbDb = Firebase.database.reference

    /**
     * Saves the contact info to firebase
     * If success, uses the push() key as the generated contactId:uuidString
     */
    suspend fun saveContactInfo(contactInfo: ContactInfo): Result<ContactInfo> {
        fbAuth.currentUser?.uid?.let { uid ->
            return try {
                val contactNode = fbDb.child(uid).child("contact-info").push()
                val contactNodeKey = contactNode.key ?: ""
                contactInfo.uuid = contactNodeKey
                contactNode.setValue(contactInfo).await()
                Result.Success(contactInfo)
            } catch (e: Exception) {
                Timber.e(e)
                Result.Error(e)
            }
        } ?: run {
            Timber.e("Could not find auth uid")
            return Result.Error(Exception("Could not find auth uid"))
        }
    }

    /**
     * Saves the contactId date of the year birthday
     * @param contactId - generated when contact is saved (the firebase push id)
     * @param dayOfYear - 0..365 is the users birthday converted to a date range.
     *                    0 is assumed to be Feb 29, Jan 1 = 1..Dec 31 = 365
     * @return nodeKey - the id of the value being saved in firebase - not really useful atm
     */
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