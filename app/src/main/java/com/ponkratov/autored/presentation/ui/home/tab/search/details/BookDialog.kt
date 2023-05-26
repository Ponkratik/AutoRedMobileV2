package com.ponkratov.autored.presentation.ui.home.tab.search.details

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.ponkratov.autored.databinding.FragmentDialogBookBinding
import com.ponkratov.autored.domain.model.response.AdvertisementResponse
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class BookDialog(private val advertisementResponse: AdvertisementResponse) : DialogFragment() {
    private var _binding: FragmentDialogBookBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel by viewModel<AdvertisementDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDialogBookBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = false

        with(binding) {
            editTextDateStart.setOnClickListener {
                val today = MaterialDatePicker.todayInUtcMilliseconds()
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = today
                val startDate = calendar.timeInMillis
                calendar[Calendar.YEAR] = calendar[Calendar.YEAR] + 1
                val endDate = calendar.timeInMillis

                val constraints: CalendarConstraints = CalendarConstraints.Builder()
                    .setOpenAt(startDate)
                    .setStart(startDate)
                    .setEnd(endDate)
                    .build()
                val datePicker = MaterialDatePicker
                    .Builder
                    .datePicker()
                    .setCalendarConstraints(constraints)
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setTitleText("Выберите дату начала")
                    .build()

                datePicker.show(childFragmentManager, "date_picker")
                datePicker.addOnPositiveButtonClickListener {
                    calendar.timeInMillis = it
                    val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.US)
                    editTextDateStart.setText(dateFormatter.format(calendar.time))
                }
            }

            editTextDateEnd.setOnClickListener {
                val today = MaterialDatePicker.todayInUtcMilliseconds()
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = today
                val startDate = calendar.timeInMillis
                calendar[Calendar.YEAR] = calendar[Calendar.YEAR] + 1
                val endDate = calendar.timeInMillis

                val constraints: CalendarConstraints = CalendarConstraints.Builder()
                    .setOpenAt(startDate)
                    .setStart(startDate)
                    .setEnd(endDate)
                    .build()
                val datePicker = MaterialDatePicker
                    .Builder
                    .datePicker()
                    .setCalendarConstraints(constraints)
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setTitleText("Выберите дату окончания")
                    .build()

                datePicker.show(childFragmentManager, "date_picker")
                datePicker.addOnPositiveButtonClickListener {
                    calendar.timeInMillis = it
                    val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.US)
                    editTextDateEnd.setText(dateFormatter.format(calendar.time))
                }
            }

            buttonSave.setOnClickListener {
                viewModel.onBookButtonClicked(
                    advertisementResponse.advertisement.id,
                    requireNotNull(
                        SimpleDateFormat("dd.MM.yyyy", Locale.US).parse(
                            editTextDateStart.text.toString()
                        )
                    ),
                    requireNotNull(
                        SimpleDateFormat("dd.MM.yyyy", Locale.US).parse(
                            editTextDateEnd.text.toString()
                        )
                    )
                )
                dismiss()
            }

            buttonCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}