package com.ponkratov.autored.presentation.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.ponkratov.autored.databinding.FragmentRegisterBinding
import java.text.SimpleDateFormat
import java.util.*

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentRegisterBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            editTextBirthdate.setOnClickListener {
                val today = MaterialDatePicker.todayInUtcMilliseconds()
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = today
                calendar[Calendar.YEAR] = 1950
                val startDate = calendar.timeInMillis
                calendar.timeInMillis = today
                calendar[Calendar.YEAR] = calendar[Calendar.YEAR] - 18
                val endDate = calendar.timeInMillis

                val constraints: CalendarConstraints = CalendarConstraints.Builder()
                    .setOpenAt(endDate)
                    .setStart(startDate)
                    .setEnd(endDate)
                    .build()
                val datePicker = MaterialDatePicker
                    .Builder
                    .datePicker()
                    .setCalendarConstraints(constraints)
                    .setTitleText("Выберите дату рождения")
                    .build()

                datePicker.show(childFragmentManager, "date_picker")
                datePicker.addOnPositiveButtonClickListener {
                    calendar.timeInMillis = it
                    val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.US)
                    editTextBirthdate.setText(dateFormatter.format(calendar.time))
                }
            }

            buttonRegister.setOnClickListener {
                if (editTextFio.text.toString().isEmpty()) {
                    containerFio.error = "Поле пусто"
                    return@setOnClickListener
                }

                if (editTextEmail.text.toString().isEmpty()) {
                    containerEmail.error = "Поле пусто"
                    return@setOnClickListener
                }

                if (editTextPassword.text.toString().isEmpty()) {
                    containerPassword.error = "Поле пусто"
                    return@setOnClickListener
                }

                if (editTextPhone.text.toString().isEmpty()) {
                    containerPhone.error = "Поле пусто"
                    return@setOnClickListener
                }

                if (editTextBirthdate.text.toString().isEmpty()) {
                    containerBirthdate.error = "Поле пусто"
                    return@setOnClickListener
                }

                if (editTextPassportNumber.text.toString().isEmpty()) {
                    containerPassportNumber.error = "Поле пусто"
                    return@setOnClickListener
                }

                if (editTextDriverLicenseNumber.text.toString().isEmpty()) {
                    containerDriverLicenseNumber.error = "Поле пусто"
                    return@setOnClickListener
                }

                findNavController().navigate(
                    RegisterFragmentDirections.actionRegisterToRegisterPhoto(
                        birthdate = requireNotNull(
                            SimpleDateFormat("dd.MM.yyyy", Locale.US).parse(
                                editTextBirthdate.text.toString()
                            )
                        ),
                        fio = editTextFio.text.toString(),
                        email = editTextEmail.text.toString(),
                        rawPassword = editTextPassword.text.toString(),
                        phone = editTextPhone.text.toString(),
                        profileDescription = editTextProfileDescription.text.toString(),
                        passportNumber = editTextPassportNumber.text.toString(),
                        driverLicenseNumber = editTextDriverLicenseNumber.text.toString()
                    )
                )
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}