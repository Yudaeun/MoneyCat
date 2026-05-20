package com.moneycat.domain.repository

import com.moneycat.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    fun get(): Flow<UserProfile?>
    suspend fun upsert(profile: UserProfile)
}
