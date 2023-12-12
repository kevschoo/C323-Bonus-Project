package edu.iu.kevschoo.bonusproject

import android.util.Log

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.*

class FirebaseStorageService : StorageService {

    private val firestore: FirebaseFirestore = Firebase.firestore
    /**
     * Retrieves a flow of a list of reminders.
     *
     * @return Flow of a list of Reminder objects.
     */
    override fun reminders(): Flow<List<Reminder>> = flow {
        val snapshot = firestore.collection("reminders").get().await()
        val remindersList = snapshot.toObjects(Reminder::class.java)
        emit(remindersList)
    }
    /**
     * Creates a new reminder in the storage.
     *
     * @param reminder The Reminder object to be created.
     */
    override suspend fun createReminder(reminder: Reminder)
    {
        try
        { firestore.collection("reminders").add(reminder).await() }
        catch (e: Exception)
        {
            Log.e("FirebaseStorageService", "Error creating reminder", e)
            throw e
        }
    }
    /**
     * Updates an existing reminder.
     *
     * @param reminder The Reminder object to be updated.
     */
    override suspend fun updateReminder(reminder: Reminder)
    {
        try
        {
            reminder.id.takeIf { it.isNotEmpty() }?.let { firestore.collection("reminders").document(it).set(reminder).await() }
        }
        catch (e: Exception)
        {
            Log.e("FirebaseStorageService", "Error updating reminder", e)
            throw e
        }
    }
    /**
     * Reads a specific reminder by its ID.
     *
     * @param reminderId The ID of the reminder to be read.
     * @return The Reminder object or null if not found.
     */
    override suspend fun readReminder(ReminderId: String): Reminder?
    {
        return firestore.collection("reminders").document(ReminderId).get().await().toObject(Reminder::class.java)
    }
    /**
     * Deletes a reminder by its ID.
     *
     * @param reminderId The ID of the reminder to be deleted.
     */
    override suspend fun deleteReminder(ReminderId: String)
    {
        firestore.collection("reminders").document(ReminderId).delete().await()
    }
}