package com.example.assignmnet_7_android

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ShowDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_details)

        val expenseNameTextView = findViewById<TextView>(R.id.expenseNameTextView)
        val expenseAmountTextView = findViewById<TextView>(R.id.expenseAmountTextView)
        val expenseDateTextView = findViewById<TextView>(R.id.expenseDateTextView)

        val expenseName = intent.getStringExtra("expenseName")
        val expenseAmount = intent.getStringExtra("expenseAmount")
        val expenseDate = intent.getStringExtra("expenseDate")

        // Set data in TextViews
        expenseNameTextView.text = "Expense Name: $expenseName"
        expenseAmountTextView.text = "Expense Amount: $expenseAmount"
        expenseDateTextView.text = "Expense Date: $expenseDate"
    }
}
