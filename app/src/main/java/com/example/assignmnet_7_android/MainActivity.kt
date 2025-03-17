package com.example.assignmnet_7_android

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
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

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get UI elements
        val expenseName = findViewById<TextInputEditText>(R.id.expenseName)
        val expenseAmount = findViewById<TextInputEditText>(R.id.expenseAmount)
        val selectDateButton = findViewById<Button>(R.id.selectDateButton)
        selectedDateText = findViewById(R.id.selectedDateText)
        val addExpenseButton = findViewById<Button>(R.id.addExpenseButton)
        val recyclerView = findViewById<RecyclerView>(R.id.expenseRecyclerView)

        // Setup RecyclerView
        expenseAdapter = ExpenseAdapter(expenses) { position -> removeExpense(position) }
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

            // Validate input
            if (name.isEmpty()) {
                Toast.makeText(this, "Expense name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (amountText.isEmpty() || !amountText.matches(Regex("^[0-9]+(\\.[0-9]{1,2})?$"))) {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Add new expense
            val newExpense = Expense(name, "$$amountText (${if (selectedDate != "Not Selected") selectedDate else "No Date"})")
            expenseAdapter.addExpense(newExpense)

            // Clear inputs
            expenseName.text?.clear()
            expenseAmount.text?.clear()
            selectedDate = "Not Selected"
            selectedDateText.text = "Date: Not Selected"
        }
    }

    private fun removeExpense(position: Int) {
        expenseAdapter.removeExpense(position)
        Toast.makeText(this, "Expense removed", Toast.LENGTH_SHORT).show()
    }
}
