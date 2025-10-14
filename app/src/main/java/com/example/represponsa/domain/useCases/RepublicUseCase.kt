package com.example.represponsa.domain.useCases

import com.example.represponsa.data.model.Republic
import com.example.represponsa.data.repository.RepublicRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class GetRepublicsUseCase @Inject constructor(
    private val repository: RepublicRepository
) {
    suspend operator fun invoke(): List<Republic> = suspendCancellableCoroutine { cont ->
        repository.getAllRepublics { result ->
            result.fold(
                onSuccess = { cont.resume(it) {} },
                onFailure = { cont.resumeWithException(it) }
            )
        }
    }
}

class GetRepublicByIdUseCase @Inject constructor(
    private val repository: RepublicRepository
) {
    suspend operator fun invoke(id: String): Republic? {
        val allRepublics = GetRepublicsUseCase(repository)()
        return allRepublics.find { it.id == id }
    }
}

class CreateRepublicUseCase @Inject constructor(
    private val repository: RepublicRepository
) {
    suspend operator fun invoke(republic: Republic): Result<Unit> = repository.createRepublic(republic)
}

class UpdateRepublicUseCase @Inject constructor(
    private val repository: RepublicRepository
) {
    suspend operator fun invoke(republic: Republic) {
        repository.updateRepublic(republic)
    }
}