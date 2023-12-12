package edu.iu.kevschoo.bonusproject

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val storageService: StorageService = FirebaseStorageService()

    private val _reminders = MutableLiveData<List<Reminder>>()
    val reminders: LiveData<List<Reminder>> = _reminders

    private val _selectedReminder = MutableLiveData<Reminder?>()
    val selectedReminder: LiveData<Reminder?> = _selectedReminder
    /**
     * Initializes the ViewModel by loading reminders.
     */
    init { loadReminders() }
    /**
     * Loads reminders from the database into LiveData.
     */
    private fun loadReminders() { viewModelScope.launch { storageService.reminders().collect { remindersList -> _reminders.postValue(remindersList) } } }
    /**
     * Creates or updates a reminder in the database.
     * @param reminder Reminder to be created or updated.
     */
    fun createOrUpdateReminder(reminder: Reminder)
    {
        viewModelScope.launch {
            try
            {
                if (reminder.id.isEmpty())
                { storageService.createReminder(reminder) }
                else
                { storageService.updateReminder(reminder) }
                loadReminders()
            } catch (e: Exception)
            { Log.e("SharedViewModel", "Error saving note", e) }
        }
    }
    /**
     * Deletes a reminder from the database.
     * @param reminderId ID of the reminder to be deleted.
     */
    fun deleteReminder(reminderId: String)
    {
        viewModelScope.launch {
            storageService.deleteReminder(reminderId)
            loadReminders()
        }
    }
    /**
     * Sets the currently selected reminder.
     * @param reminder Reminder to be selected.
     */
    fun selectReminder(reminder: Reminder) { _selectedReminder.value = reminder }
    /**
     * Clears the currently selected reminder.
     */
    fun clearSelectedNote() { _selectedReminder.value = null }

}