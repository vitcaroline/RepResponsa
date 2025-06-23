package com.example.represponsa.domain.useCases

import com.example.represponsa.data.model.Assignment
import com.example.represponsa.data.repository.AssignmentRepository
import javax.inject.Inject

class GetAssignmentsUseCase @Inject constructor (private val repository: AssignmentRepository) {
    suspend operator fun invoke(): List<Assignment> = repository.getAssignments()
}

class DeleteAssignmentsUseCase @Inject constructor (private val repository: AssignmentRepository) {
    suspend operator fun invoke(ids: List<String>) = repository.deleteAssignments(ids)
}

class UpdateAssignmentUseCase @Inject constructor (private val repository: AssignmentRepository) {
    suspend operator fun invoke(assignment: Assignment) = repository.updateAssignment(assignment)
}

class CreateAssignmentUseCase @Inject constructor (private val repository: AssignmentRepository) {
    suspend operator fun invoke(assignment: Assignment) = repository.createAssignment(assignment)
}

class GetAssignmentByIdUseCase @Inject constructor (private val repo: AssignmentRepository) {
    suspend operator fun invoke(id: String): Assignment? = repo.getAssignmentById(id)
}