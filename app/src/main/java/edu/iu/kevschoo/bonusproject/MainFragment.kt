package edu.iu.kevschoo.bonusproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import edu.iu.kevschoo.bonusproject.databinding.FragmentMainBinding
import androidx.navigation.fragment.findNavController
import androidx.appcompat.app.AlertDialog

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel
    private lateinit var adapter: ReminderAdapter
    /**
     * Inflates the layout for the fragment's view and initializes RecyclerView and Toolbar.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        setupRecyclerView()
        setupToolbar()
        return view
    }
    /**
     * Sets up the RecyclerView with reminders.
     */
    private fun setupRecyclerView()
    {
        adapter = ReminderAdapter(emptyList(),
            onReminderClick = { reminder ->
                viewModel.selectReminder(reminder)
                findNavController().navigate(R.id.reminderFragment)
            },
            onDeleteClick = { reminderId ->
                showDeleteConfirmationDialog(reminderId)
            }
        )
        viewModel.reminders.observe(viewLifecycleOwner) { reminders ->
            adapter.reminders = reminders
            adapter.notifyDataSetChanged()
        }
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = adapter
    }
    /**
     * Displays a dialog for confirmation before deleting a reminder.
     *
     * @param reminderId ID of the reminder to be deleted.
     */
    private fun showDeleteConfirmationDialog(reminderId: String)
    {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Reminder")
            .setMessage("Are you sure you want to delete this reminder?")
            .setPositiveButton("Delete") { dialog, which ->
                viewModel.deleteReminder(reminderId)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    /**
     * Sends an intent to open an email application for feedback.
     */
    private fun sendFeedbackEmail()
    {
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("kevschoo@iu.edu"))
            putExtra(Intent.EXTRA_SUBJECT, "Feedback")
            putExtra(Intent.EXTRA_TEXT, "Enter your feedback here...")
        }

        try
        { startActivity(Intent.createChooser(emailIntent, "Send email using...")) }
        catch (e: Exception)
        { Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show() }
    }
    /**
     * Opens the about page in a web browser.
     */
    private fun openAboutPage()
    {
        val aboutIntent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse("https://luddy.indiana.edu/") }
        startActivity(aboutIntent)
    }
    /**
     * Sets up the toolbar with menu options.
     */
    private fun setupToolbar()
    {
        binding.toolbar.inflateMenu(R.menu.toolbar_menu)
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_create_reminder -> {
                    viewModel.clearSelectedNote()
                    findNavController().navigate(R.id.reminderFragment)
                    true
                }
                R.id.action_feedback -> {
                    sendFeedbackEmail()
                    true
                }
                R.id.action_about -> {
                    openAboutPage()
                    true
                }
                else -> false
            }
        }
        binding.fab.setOnClickListener {
            viewModel.clearSelectedNote()
            findNavController().navigate(R.id.reminderFragment)
        }
    }
    /**
     * Observes changes in the list of reminders and updates the RecyclerView accordingly.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        viewModel.reminders.observe(viewLifecycleOwner) { reminders ->
            adapter.reminders = reminders
            adapter.notifyDataSetChanged()
        }
    }
    /**
     * Cleans up the binding when the view is destroyed.
     */
    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }
}