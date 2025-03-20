package com.example.assignmnet_7_android

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var expenseAdapter: ExpenseAdapter
    private val expenses = mutableListOf<Expense>()
    private lateinit var selectedDateText: TextView
    private var selectedDate: String = "Not Selected"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("Lifecycle", "onCreate called")

        // Initialize UI elements
        val expenseName = findViewById<TextInputEditText>(R.id.expenseName)
        val expenseAmount = findViewById<TextInputEditText>(R.id.expenseAmount)
        val selectDateButton = findViewById<Button>(R.id.selectDateButton)
        selectedDateText = findViewById(R.id.selectedDateText)
        val addExpenseButton = findViewById<Button>(R.id.addExpenseButton)
        val recyclerView = findViewById<RecyclerView>(R.id.expenseRecyclerView)

        // Setup RecyclerView
        expenseAdapter = ExpenseAdapter(expenses, ::removeExpense, ::showExpenseDetails)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = expenseAdapter

        // Date Picker Dialog
        selectDateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                selectedDateText.text = "Date: $selectedDate"
            }, year, month, day)

            datePickerDialog.show()
        }

        // Handle button click for adding expenses
        addExpenseButton.setOnClickListener {
            val name = expenseName.text.toString().trim()
            val amountText = expenseAmount.text.toString().trim()

            if (name.isEmpty() || amountText.isEmpty()) {
                Toast.makeText(this, "Please enter valid details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if (amount == null) {
                Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newExpense = Expense(name, amount, selectedDate)
            expenses.add(newExpense)
            expenseAdapter.notifyItemInserted(expenses.size - 1)

            // Clear inputs
            expenseName.text?.clear()
            expenseAmount.text?.clear()
            selectedDate = "Not Selected"
            selectedDateText.text = "Date: Not Selected"
        }
    }

    private fun showExpenseDetails(position: Int) {
        val expense = expenses[position]
        val intent = Intent(this, ShowDetailsActivity::class.java)
        intent.putExtra("expenseName", expense.name)
        intent.putExtra("expenseAmount", expense.amount.toString())
        intent.putExtra("expenseDate", expense.date)
        startActivity(intent)
    }

    private fun removeExpense(position: Int) {
        expenses.removeAt(position)
        expenseAdapter.notifyItemRemoved(position)
        Toast.makeText(this, "Expense removed", Toast.LENGTH_SHORT).show()
    }

    // Lifecycle Logging
    override fun onStart() {
        super.onStart()
        Log.d("Lifecycle", "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Lifecycle", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Lifecycle", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Lifecycle", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Lifecycle", "onDestroy called")
    }
}

