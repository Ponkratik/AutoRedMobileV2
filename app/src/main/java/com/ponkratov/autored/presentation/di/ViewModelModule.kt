package com.ponkratov.autored.presentation.di

import com.ponkratov.autored.presentation.ui.home.tab.account.AccountViewModel
import com.ponkratov.autored.presentation.ui.home.tab.account.addadvertisement.AdvertisementAddViewModel
import com.ponkratov.autored.presentation.ui.home.tab.account.ridedetails.RideDetailsLessorViewModel
import com.ponkratov.autored.presentation.ui.home.tab.account.review.AddReviewLessorViewModel
import com.ponkratov.autored.presentation.ui.home.tab.account.ridelist.RideListLessorViewModel
import com.ponkratov.autored.presentation.ui.home.tab.account.settings.SettingsViewModel
import com.ponkratov.autored.presentation.ui.home.tab.account.support.SupportRequestViewModel
import com.ponkratov.autored.presentation.ui.home.tab.history.HistoryViewModel
import com.ponkratov.autored.presentation.ui.home.tab.history.details.RideDetailsLesseeViewModel
import com.ponkratov.autored.presentation.ui.home.tab.history.review.AddReviewLesseeViewModel
import com.ponkratov.autored.presentation.ui.home.tab.search.details.AdvertisementDetailsViewModel
import com.ponkratov.autored.presentation.ui.home.tab.search.list.SearchViewModel
import com.ponkratov.autored.presentation.ui.login.LoginViewModel
import com.ponkratov.autored.presentation.ui.registration.RegisterPhotoViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterPhotoViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::AdvertisementDetailsViewModel)
    viewModelOf(::AccountViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::SupportRequestViewModel)
    viewModelOf(::AdvertisementAddViewModel)
    viewModelOf(::HistoryViewModel)
    viewModelOf(::RideDetailsLessorViewModel)
    viewModelOf(::RideListLessorViewModel)
    viewModelOf(::RideDetailsLesseeViewModel)
    viewModelOf(::AddReviewLesseeViewModel)
    viewModelOf(::AddReviewLessorViewModel)
}