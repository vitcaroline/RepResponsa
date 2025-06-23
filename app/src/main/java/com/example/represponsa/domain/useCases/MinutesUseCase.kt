package com.example.represponsa.domain.useCases

import com.example.represponsa.data.model.Minute
import com.example.represponsa.data.repository.MinutesRepository
import javax.inject.Inject

class GetMinutesUseCase @Inject constructor (private val repository: MinutesRepository) {
    suspend operator fun invoke(): List<Minute> = repository.getMinutes()
}

class UpdateMinuteUseCase @Inject constructor (private val repository: MinutesRepository) {
    suspend operator fun invoke(minute: Minute) = repository.updateMinute(minute)
}

class CreateMinuteUseCase @Inject constructor (private val repository: MinutesRepository) {
    suspend operator fun invoke(minute: Minute) = repository.createMinute(minute)
}

class GetMinuteByIdUseCase @Inject constructor (private val repo: MinutesRepository) {
    suspend operator fun invoke(id: String): Minute? = repo.getMinuteById(id)
}

class DeleteMinuteUseCase @Inject constructor (private val repository: MinutesRepository) {
    suspend operator fun invoke(minuteId: String) {
        repository.deleteMinutes(listOf(minuteId))
    }
}