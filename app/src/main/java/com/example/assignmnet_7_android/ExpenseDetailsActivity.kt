package com.example.assignmnet_7_android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ExpenseDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_details)

        // Get UI elements
        val expenseNameTextView = findViewById<TextView>(R.id.expenseNameTextView)
        val expenseAmountTextView = findViewById<TextView>(R.id.expenseAmountTextView)
        val expenseDateTextView = findViewById<TextView>(R.id.expenseDateTextView)
        val openWebButton = findViewById<Button>(R.id.openWebButton) // Implicit Intent

        // Retrieve data from Intent Extras
        val expenseName = intent.getStringExtra("expenseName")
        val expenseAmount = intent.getStringExtra("expenseAmount")
        val expenseDate = intent.getStringExtra("expenseDate")

        // Display received data
        expenseNameTextView.text = "Expense: $expenseName"
        expenseAmountTextView.text = "Amount: $expenseAmount"
        expenseDateTextView.text = "Date: $expenseDate"

        // Implicit Intent - Open Web Browser for Financial Tips
        openWebButton.setOnClickListener {
            val url = "https://www.investopedia.com/articles/personal-finance/090415/beginners-guide-managing-your-money.asp"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}
