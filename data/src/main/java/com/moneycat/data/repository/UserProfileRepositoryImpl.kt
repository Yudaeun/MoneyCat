package com.moneycat.data.repository

import com.moneycat.data.local.db.dao.UserProfileDao
import com.moneycat.data.local.db.mapper.toDomain
import com.moneycat.data.local.db.mapper.toEntity
import com.moneycat.domain.model.UserProfile
import com.moneycat.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileRepositoryImpl @Inject constructor(
    private val dao: UserProfileDao
) : UserProfileRepository {

    override fun get(): Flow<UserProfile?> =
        dao.get().map { it?.toDomain() }

    override suspend fun upsert(profile: UserProfile) =
        dao.upsert(profile.toEntity())
}
