package com.day.moneycat.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moneycat.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StartupViewModel @Inject constructor(
    userProfileRepository: UserProfileRepository,
) : ViewModel() {
    val isOnboardingCompleted = userProfileRepository.get()
        .map { it?.onboardingCompleted }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}
