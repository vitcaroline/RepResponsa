package com.example.represponsa.domain.useCases

import android.content.Context
import android.net.Uri
import com.example.represponsa.data.model.RentPaymentConfig
import com.example.represponsa.data.repository.ReceiptRepository
import com.example.represponsa.data.repository.RepublicRepository
import javax.inject.Inject

class ReceiptsUseCase @Inject constructor (private val repository: RepublicRepository) {
    suspend operator fun invoke(config: RentPaymentConfig) {
        repository.saveRentPaymentConfig(config)
    }
}

class UploadReceiptUseCase @Inject constructor(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(fileUri: Uri, context: Context): Result<Unit> {
        return repository.uploadReceipt(fileUri, context)
    }
}

class CheckUserPaymentStatusUseCase @Inject constructor(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(userId: String, republicId: String, month: String): Boolean {
        return repository.hasUserPaidForMonth(userId, republicId, month)
    }
}