package com.example.represponsa.presentation.ui.commons

import com.example.represponsa.data.model.User
import java.util.Date

//User
fun String.validateName(): String? =
    if (this.isBlank()) "Nome não pode ficar vazio"
    else if (this.any { it.isDigit() }) "Nome não pode conter números"
    else null

fun String.validatePhone(): String? =
    when {
        !this.all { it.isDigit() }                  -> "Telefone deve conter apenas números"
        else                                        -> null
    }

fun String.validatePassword(): String? =
    if (this.length < 6) "Senha deve ter ao menos 6 caracteres"
    else null


//Republic
fun String.validateRepublicName(): String? =
    if (this.isBlank()) "Nome não pode ficar vazio"
    else null

fun String.validateAddress(): String? =
    if (this.isBlank()) "Endereço não pode ficar vazio"
    else null

fun String.validateResidentCount(): String? =
    when {
        this.isBlank() -> "Quantidade de moradores obrigatória"
        this.toIntOrNull() == null -> "Informe um número válido"
        this.toInt() <= 0 -> "Deve haver ao menos 1 morador"
        else -> null
    }

fun String.validatePetCount(): String? =
    when {
        this.isBlank() -> null
        this.toIntOrNull() == null -> "Informe um número válido de pets"
        this.toInt() < 0 -> "Número de pets não pode ser negativo"
        else -> null
    }

//Assignment

fun String.validateAssignmentTitle(): String? =
    if (this.isBlank()) "O título não pode ficar em branco" else null

fun User?.validateSelectedResident(): String? =
    if (this == null) "Selecione um morador responsável" else null

fun Date.validateDueDate(): String? =
    if (this.before(Date())) "A data deve ser no futuro" else null