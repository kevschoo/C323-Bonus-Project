package edu.iu.kevschoo.bonusproject

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import edu.iu.kevschoo.bonusproject.databinding.FragmentReminderBinding
import androidx.navigation.fragment.findNavController

import androidx.appcompat.app.AlertDialog

class ReminderFragment : Fragment() {

    private var _binding: FragmentReminderBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel

    /**
     * Inflates the layout for this fragment.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentReminderBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        return view
    }
    /**
     * Sets up the UI components and observes ViewModel data.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedReminder.observe(viewLifecycleOwner) { selectedReminder ->
            if (selectedReminder != null && selectedReminder.id.isNotEmpty())
            {
                binding.editTextTitle.setText(selectedReminder.title)
                binding.editTextDescription.setText(selectedReminder.text)
                binding.btnPickTime.text = selectedReminder.time
                binding.btnPickDate.text = selectedReminder.date
            }
            else
            {

            }
        }
        val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            saveReminderIfNeeded()
            findNavController().navigate(R.id.mainFragment)
        }
    }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        setupToolbar()
        setupTimeAndDatePickers()
    }
    /**
     * Sets up time and date pickers.
     */
    private fun setupTimeAndDatePickers()
    {
        binding.btnPickTime.setOnClickListener {
            val timePickerDialog = TimePickerDialog(context, { _, hourOfDay, minute ->
                binding.btnPickTime.text = String.format("%02d:%02d", hourOfDay, minute)
            }, 12, 0, false)
            timePickerDialog.show()
        }

        binding.btnPickDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                binding.btnPickDate.text = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            }, 2023, 0, 1)
            datePickerDialog.show()
        }
    }
    /**
     * Configures the toolbar menu actions.
     */
    private fun setupToolbar()
    {
        binding.toolbar.inflateMenu(R.menu.toolbar_note_edit_menu)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId)
            {
                R.id.action_delete_note -> {
                    showDeleteConfirmationDialog()
                    true
                }
                else -> false
            }
        }
    }
    /**
     * Displays a confirmation dialog for deleting a reminder.
     */
    private fun showDeleteConfirmationDialog()
    {
        val selectedReminder = viewModel.selectedReminder.value

        AlertDialog.Builder(requireContext())
            .setTitle("Delete Reminder")
            .setMessage("Are you sure you want to delete this reminder?")
            .setPositiveButton("Delete") { dialog, which ->
                if (selectedReminder != null && selectedReminder.id.isNotEmpty())
                { viewModel.deleteReminder(selectedReminder.id) }
                viewModel.clearSelectedNote()
                navigateBack()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    /**
     * Saves the reminder if needed based on user input.
     */
    private fun saveReminderIfNeeded()
    {
        val currentReminder = viewModel.selectedReminder.value
        val title = binding.editTextTitle.text.toString()
        val text = binding.editTextDescription.text.toString()
        val time = binding.btnPickTime.text.toString()
        val date = binding.btnPickDate.text.toString()

        val isTimeSet = time != "Pick Time"
        val isDateSet = date != "Pick Date"

        val isBlankReminder = title.isBlank() && text.isBlank() && !isTimeSet && !isDateSet

        if (isBlankReminder)
        {
            if (currentReminder != null && currentReminder.id.isNotEmpty())
            { viewModel.deleteReminder(currentReminder.id) }
        }
        else
        {
            val timeToSave = if (isTimeSet) time else ""
            val dateToSave = if (isDateSet) date else ""
            val newOrUpdateReminder = Reminder(id = currentReminder?.id ?: "", title = title, text = text, time = timeToSave, date = dateToSave)
            viewModel.createOrUpdateReminder(newOrUpdateReminder)
        }
    }
    /**
     * Handles navigation back to the main fragment.
     */
    private fun navigateBack() { findNavController().navigate(R.id.mainFragment) }
    /**
     * Cleans up resources when the view is destroyed.
     */
    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }
}