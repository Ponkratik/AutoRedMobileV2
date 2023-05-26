package com.ponkratov.autored.presentation.ui.home.tab.account.addadvertisement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ponkratov.autored.domain.model.Advertisement
import com.ponkratov.autored.domain.model.Car
import com.ponkratov.autored.domain.model.CarFeatureList
import com.ponkratov.autored.domain.model.Lce
import com.ponkratov.autored.domain.usecase.AddAdvertisementUseCase
import com.ponkratov.autored.domain.usecase.GetJwtResponseUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import java.util.Date

class AdvertisementAddViewModel(
    private val addAdvertisementUseCase: AddAdvertisementUseCase,
    private val getJwtResponseUseCase: GetJwtResponseUseCase
) : ViewModel() {

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
            addAdvertisementUseCase(
                requireNotNull(advertisement),
                requireNotNull(car),
                requireNotNull(carFeatureList),
                files
            ).fold(
                onSuccess = {
                    lceFlow.tryEmit(Lce.Content(it))
                },
                onFailure = {
                    lceFlow.tryEmit(Lce.Error(it.message))
                }
            )
        }.launchIn(viewModelScope)

    private var advertisement: Advertisement? = null
    private var car: Car? = null
    private var carFeatureList: CarFeatureList? = null
    private var files: List<File> = emptyList()

    fun onSendButtonClicked(
        isConditioner: Boolean,
        isAllWheelDrive: Boolean,
        isLeatherSeats: Boolean,
        isChildSeats: Boolean,
        isHeatedSeats: Boolean,
        isCooledSeats: Boolean,
        isGps: Boolean,
        isSkiRack: Boolean,
        isAudioInput: Boolean,
        isUsbInput: Boolean,
        isBluetooth: Boolean,
        isAndroidAuto: Boolean,
        isAppleCarplay: Boolean,
        isSunRoof: Boolean,
        vin: String,
        licensePlate: String,
        make: String,
        model: String,
        manufacturedYear: Date,
        transmissionType: String,
        fuelType: String,
        doors: Int,
        seats: Int,
        carType: String,
        color: String,
        location: String,
        latitude: Double,
        longitude: Double,
        pricePerDay: Double,
        pricePerWeek: Double,
        pricePerMonth: Double,
        filesList: List<File>
    ) {
        carFeatureList = CarFeatureList(
            "",
            isConditioner,
            isAllWheelDrive,
            isLeatherSeats,
            isChildSeats,
            isHeatedSeats,
            isCooledSeats,
            isGps,
            isSkiRack,
            isAudioInput,
            isUsbInput,
            isBluetooth,
            isAndroidAuto,
            isAppleCarplay,
            isSunRoof
        )

        car = Car(
            vin = vin,
            licensePlate = licensePlate,
            make = make,
            model = model,
            manufacturedYear = manufacturedYear,
            transmissionType = transmissionType,
            fuelType = fuelType,
            doors = doors,
            seats = seats,
            carType = carType,
            color = color
        )

        advertisement = Advertisement(
            userId = getJwtResponseUseCase().id,
            location = location,
            latitude = latitude,
            longitude = longitude,
            pricePerDay = pricePerDay,
            pricePerWeek = pricePerWeek,
            pricePerMonth = pricePerMonth
        )

        files = filesList

        initFlow.tryEmit(Unit)
    }

}