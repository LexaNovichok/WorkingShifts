package com.github.lexanovichok.workingshift.schedule.tasks.model

import android.util.Log
import com.github.lexanovichok.workingshift.schedule.userData.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class TaskRepository {

    private val database = FirebaseDatabase.getInstance()
    val tasksRef = database.getReference("tasks")


    suspend fun getTasks(): List<Task> {
        return suspendCancellableCoroutine { continuation ->
            tasksRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tasks = snapshot.children.mapNotNull { it.getValue(Task::class.java) }
                    continuation.resume(tasks)  // Возвращаем результат
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(Exception("Error getting tasks: ${error.message}"))
                }
            })
        }
    }

//    suspend fun getTasksWithCurrentDate() : List<Task> {
//
//    }

    suspend fun addTask(task : Task) : Unit = suspendCancellableCoroutine { continuation ->
        val taskId = tasksRef.push().key ?: run {
            continuation.resumeWithException(Exception("Error generating task ID"))
            return@suspendCancellableCoroutine
        }
        task.id = taskId

        tasksRef.child(taskId).setValue(task)
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

    suspend fun updateTask(task : Task) {
        tasksRef.child(task.id).setValue(task).await()
    }

    suspend fun deleteTask(taskId: String) {
        tasksRef.child(taskId).removeValue().await()
    }
}