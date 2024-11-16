package com.github.lexanovichok.workingshifts.schedule.address.model

import android.util.Log
import com.github.lexanovichok.workingshifts.schedule.userData.Address
import com.github.lexanovichok.workingshifts.schedule.userData.Worker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AddressRepository {

    private val database = FirebaseDatabase.getInstance()
    val addressesRef = database.getReference("address")

    suspend fun addAddress(address: Address): Unit = suspendCancellableCoroutine { continuation ->
        val addressId = addressesRef.push().key ?: run {
            continuation.resumeWithException(Exception("Error generating address ID"))
            return@suspendCancellableCoroutine
        }
        address.id = addressId

        addressesRef.child(addressId).setValue(address)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("SCHEDULE", "Data stored successfully")
                    continuation.resume(Unit)  // Возвращаем успешное завершение
                } else {
                    val exception = task.exception ?: Exception("Unknown error during setValue")
                    Log.d("SCHEDULE", "Error: ${exception.message}")
                    continuation.resumeWithException(exception)  // Возвращаем исключение
                }
            }
    }

    suspend fun getAddresses(): List<Address> {
        return suspendCancellableCoroutine { continuation ->
            addressesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val address = snapshot.children.mapNotNull { it.getValue(Address::class.java) }
                    continuation.resume(address)
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(Exception("Error getting workers: ${error.message}"))
                }
            })
        }
    }

    suspend fun getAddressById(addressId: String): Address? {
        return suspendCancellableCoroutine { continuation ->
            addressesRef.child(addressId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val address = snapshot.getValue(Address::class.java)
                    continuation.resume(address)
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(Exception("Error getting worker with ID $addressId: ${error.message}"))
                }
            })
        }
    }

    suspend fun updateAddress(address: Address) {
        addressesRef.child(address.id).setValue(address).await()
    }

    suspend fun deleteAddress(addressId: String) {
        addressesRef.child(addressId).removeValue().await()
    }

}