package edu.iu.kevschoo.bonusproject

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class Reminder(
    @DocumentId val id: String = "",
    val title: String = "",
    val text: String = "",
    val time: String = "",
    val date: String = "",
    @ServerTimestamp val noteSavedAt: Timestamp? = null

)
