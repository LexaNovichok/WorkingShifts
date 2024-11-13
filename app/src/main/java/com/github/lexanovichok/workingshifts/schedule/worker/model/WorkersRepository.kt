package com.github.lexanovichok.workingshifts.schedule.worker.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.lexanovichok.workingshifts.schedule.userData.Worker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class WorkersRepository {

    private val database = FirebaseDatabase.getInstance()
    private val workersRef = database.getReference("worker")

    suspend fun addWorker(worker: Worker): Unit = suspendCancellableCoroutine { continuation ->
        val workerId = workersRef.push().key ?: run {
            continuation.resumeWithException(Exception("Error generating worker ID"))
            return@suspendCancellableCoroutine
        }
        worker.id = workerId
        Log.d("SCHEDULE", "WorkersRepository addWorker id: $workerId, name: ${worker.name}, contact: ${worker.contacts}")

        workersRef.child(workerId).setValue(worker)
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

    suspend fun getWorkers(): List<Worker> {
        return suspendCancellableCoroutine { continuation ->
            workersRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val workers = snapshot.children.mapNotNull { it.getValue(Worker::class.java) }
                    continuation.resume(workers)  // Возвращаем результат
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(Exception("Error getting workers: ${error.message}"))
                }
            })
        }
    }

    suspend fun updateWorker(worker: Worker) {
        workersRef.child(worker.id).setValue(worker).await()
    }

    suspend fun deleteWorker(workerId: String) {
        workersRef.child(workerId).removeValue().await()
    }
}