package com.example.represponsa.data.model

enum class RolesEnum(val label: String) {
    FINANCEIRO("Financeiro"),
    MERCADO("Mercado"),
    FAXINA("Faxina"),
    MANUTENCAO("Manutenção"),
    PRODUTOS("Produtos"),
    MARKETING("Marketing"),
    SECRETARIA("Secretaria"),
    CONTAS("Contas"),
    FESTAS("Festas"),
    RELACOES("Relações Externas"),
    LIXO("Lixo");

    companion object {
        fun fromLabel(label: String): RolesEnum? = values().find { it.label == label }
    }
}