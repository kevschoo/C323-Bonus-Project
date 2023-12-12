package edu.iu.kevschoo.bonusproject


import kotlinx.coroutines.flow.Flow

interface StorageService {
    /**
     * Retrieves a flow of a list of reminders.
     *
     * @return Flow of a list of Reminder objects.
     */
    fun reminders(): Flow<List<Reminder>>
    /**
     * Creates a new reminder in the storage.
     *
     * @param reminder The Reminder object to be created.
     */
    suspend fun createReminder(reminder: Reminder)
    /**
     * Reads a specific reminder by its ID.
     *
     * @param reminderId The ID of the reminder to be read.
     * @return The Reminder object or null if not found.
     */
    suspend fun readReminder(reminderId: String): Reminder?
    /**
     * Updates an existing reminder.
     *
     * @param reminder The Reminder object to be updated.
     */
    suspend fun updateReminder(reminder: Reminder)
    /**
     * Deletes a reminder by its ID.
     *
     * @param reminderId The ID of the reminder to be deleted.
     */
    suspend fun deleteReminder(reminderId: String)
}