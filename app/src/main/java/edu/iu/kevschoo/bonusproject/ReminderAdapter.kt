package edu.iu.kevschoo.bonusproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.iu.kevschoo.bonusproject.databinding.ItemReminderBinding

class ReminderAdapter(
    var reminders: List<Reminder>,
    private val onReminderClick: (Reminder) -> Unit,
    private val onDeleteClick: (String) -> Unit
) : RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {
    /**
     * ViewHolder for reminder items.
     */
    class ReminderViewHolder(private val binding: ItemReminderBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(reminder: Reminder, onReminderClick: (Reminder) -> Unit, onDeleteClick: (String) -> Unit)
        {
            binding.tvTitle.text = reminder.title
            binding.tvDescription.text = reminder.text
            binding.tvDateTime.text = "${reminder.date}\n${reminder.time}"
            binding.btnDelete.setOnClickListener { onDeleteClick(reminder.id) }
            itemView.setOnClickListener { onReminderClick(reminder) }
        }
    }
    /**
     * Creates new views (invoked by the layout manager).
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder
    {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReminderBinding.inflate(inflater, parent, false)
        return ReminderViewHolder(binding)
    }
    /**
     * Replaces the contents of a view (invoked by the layout manager).
     */
    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int)
    {
        val reminder = reminders[position]
        holder.bind(reminder, onReminderClick, onDeleteClick)
    }
    /**
     * Returns the size of the dataset (invoked by the layout manager).
     */
    override fun getItemCount(): Int = reminders.size
}