package com.github.lexanovichok.workingshifts.schedule.tasks.model

import android.util.Log
import com.github.lexanovichok.workingshifts.schedule.userData.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class TaskRepository {

    private val database = FirebaseDatabase.getInstance().reference.child("tasks")

    suspend fun createOrUpdateTask(task: Task) {
        database.child(task.id).setValue(task).await()
    }

    suspend fun getTasks(): List<Task> {
        return suspendCancellableCoroutine { continuation ->
            database.addValueEventListener(object : ValueEventListener {
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

    suspend fun deleteTask(taskId: String) {
        database.child(taskId).removeValue().await()
    }
}