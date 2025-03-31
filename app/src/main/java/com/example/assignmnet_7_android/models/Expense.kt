package com.example.assignmnet_7_android.models

data class Expense(
    val name: String,
    val amount: Double,
    val date: String,
    val costAssociated: Boolean,
    val currency: String,
    val convertedCost: Double

)


