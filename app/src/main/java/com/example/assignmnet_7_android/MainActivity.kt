package com.example.assignmnet_7_android

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Get UI elements
        val expenseName = findViewById<EditText>(R.id.expenseName)
        val expenseAmount = findViewById<EditText>(R.id.expenseAmount)
        val expenseDate = findViewById<EditText>(R.id.expenseDate)
        val addExpenseButton = findViewById<Button>(R.id.addExpenseButton)

        // Handle button click
        addExpenseButton.setOnClickListener {
            val name = expenseName.text.toString().trim()
            val amount = expenseAmount.text.toString().trim()
            val date = expenseDate.text.toString().trim()

            // Validate input
            if (name.isEmpty() || amount.isEmpty()) {
                Toast.makeText(this, "Please enter name and amount", Toast.LENGTH_SHORT).show()
            } else {
                val expenseDetails = "Expense: $name\nAmount: $amount\nDate: ${if (date.isNotEmpty()) date else "Not provided"}"
                Toast.makeText(this, expenseDetails, Toast.LENGTH_LONG).show()
            }
        }
    }
}









