package com.example.represponsa.data.model

enum class RolesEnum(val label: String) {
    FINANCEIRO("Financeiro"),
    MERCADO("Faxina"),
    MANUTENCAO("Manutenção"),
    PRODUTOS("Produtos"),
    MARKETING("Marketing"),
    SECRETARIA("Secretaria");
    
    companion object {
        fun fromLabel(label: String): RolesEnum? = values().find { it.label == label }
    }
}