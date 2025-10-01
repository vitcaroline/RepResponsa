package com.example.represponsa.domain.useCases

import com.example.represponsa.data.model.RentPaymentConfig
import com.example.represponsa.data.repository.RepublicRepository
import javax.inject.Inject

class SaveRentPaymentConfigUseCase @Inject constructor(
    private val repository: RepublicRepository
) {
    suspend operator fun invoke(config: RentPaymentConfig) {
        repository.saveRentPaymentConfig(config)
    }
}