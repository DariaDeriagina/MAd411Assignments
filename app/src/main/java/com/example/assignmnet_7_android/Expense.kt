package com.example.assignmnet_7_android

import java.util.Currency

data class Expense(
    val name: String,
    val amount: Double,
    val date: String,
    val costAssociated: Boolean,
    val currency: Currency,
    val convertedCost: Double
)

