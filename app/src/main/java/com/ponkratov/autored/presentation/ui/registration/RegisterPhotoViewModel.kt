package com.ponkratov.autored.presentation.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ponkratov.autored.domain.model.Lce
import com.ponkratov.autored.domain.model.request.RegisterRequest
import com.ponkratov.autored.domain.usecase.RegisterUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import java.util.Date

class RegisterPhotoViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private var registerRequest: RegisterRequest? = null
    private var avatarPhoto: File? = null
    private var passportPhoto: File? = null
    private var driverLicensePhoto: File? = null

    private val initFlow = MutableSharedFlow<Unit>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val lceFlow = MutableSharedFlow<Lce<String>>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val networkFlow = initFlow
        .onEach {
            lceFlow.tryEmit(Lce.Loading())
        }
        .onEach {
            registerUseCase(
                requireNotNull(registerRequest),
                requireNotNull(avatarPhoto),
                requireNotNull(passportPhoto),
                requireNotNull(driverLicensePhoto)
            ).fold(
                onSuccess = {
                    lceFlow.tryEmit(Lce.Content(it))
                },
                onFailure = {
                    lceFlow.tryEmit(Lce.Error(it.message))
                }
            )
        }.launchIn(viewModelScope)

    fun onRegisterButtonClicked(
        fio: String,
        email: String,
        rawPassword: String,
        phone: String,
        birthdate: Date,
        passportNumber: String,
        driverLicenseNumber: String,
        profileDescription: String,
        profilePhoto: File,
        passportPhoto: File,
        driverLicensePhoto: File
    ) {
        registerRequest = RegisterRequest(
            fio,
            email,
            rawPassword,
            phone,
            birthdate,
            passportNumber,
            driverLicenseNumber,
            profileDescription
        )
        avatarPhoto = profilePhoto
        this.passportPhoto = passportPhoto
        this.driverLicensePhoto = driverLicensePhoto

        initFlow.tryEmit(Unit)
    }
}