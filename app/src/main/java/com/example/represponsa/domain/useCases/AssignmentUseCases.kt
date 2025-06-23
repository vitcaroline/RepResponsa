package com.example.represponsa.domain.useCases

import com.example.represponsa.data.model.Assignment
import com.example.represponsa.data.repository.AssignmentRepository

class GetAssignmentsUseCase(private val repository: AssignmentRepository) {
    suspend operator fun invoke(): List<Assignment> = repository.getAssignments()
}

class DeleteAssignmentsUseCase(private val repository: AssignmentRepository) {
    suspend operator fun invoke(ids: List<String>) = repository.deleteAssignments(ids)
}

class UpdateAssignmentUseCase(private val repository: AssignmentRepository) {
    suspend operator fun invoke(assignment: Assignment) = repository.updateAssignment(assignment)
}

class CreateAssignmentUseCase(private val repository: AssignmentRepository) {
    suspend operator fun invoke(assignment: Assignment) = repository.createAssignment(assignment)
}

class GetAssignmentByIdUseCase(private val repo: AssignmentRepository) {
    suspend operator fun invoke(id: String): Assignment? = repo.getAssignmentById(id)
}