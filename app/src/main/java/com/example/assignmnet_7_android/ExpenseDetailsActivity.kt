package com.example.assignmnet_7_android

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ExpenseDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_details)

        // Get TextView references
        val expenseNameTextView = findViewById<TextView>(R.id.expenseNameTextView)
        val expenseAmountTextView = findViewById<TextView>(R.id.expenseAmountTextView)
        val expenseDateTextView = findViewById<TextView>(R.id.expenseDateTextView)

        // Retrieve expense details from Intent
        val expenseName = intent.getStringExtra("expenseName")
        val expenseAmount = intent.getStringExtra("expenseAmount")
        val expenseDate = intent.getStringExtra("expenseDate")

        // Display expense details
        expenseNameTextView.text = "Expense name: $expenseName"
        expenseAmountTextView.text = "Amount: $expenseAmount"
        expenseDateTextView.text = "Date: $expenseDate"
    }
}
