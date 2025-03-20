package com.example.assignmnet_7_android

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
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
    private var footerFragment: FooterFragment? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("Lifecycle", "onCreate called")

        //Initialize the header element
// Initialize Header Fragment
        val headerFragment = HeaderFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.headerContainer, headerFragment)
            .commit()


        // Initialize UI elements
        val expenseName = findViewById<TextInputEditText>(R.id.expenseName)
        val expenseAmount = findViewById<TextInputEditText>(R.id.expenseAmount)
        val selectDateButton = findViewById<Button>(R.id.selectDateButton)
        selectedDateText = findViewById(R.id.selectedDateText)
        val addExpenseButton = findViewById<Button>(R.id.addExpenseButton)
        val recyclerView = findViewById<RecyclerView>(R.id.expenseRecyclerView)
        val financialTipsButton = findViewById<Button>(R.id.tipsButton)

        // Initialize Footer Fragment
        footerFragment = FooterFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.footerContainer, footerFragment!!)
            .commit()

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
                selectedDateText.text = getString(R.string.selected_date, selectedDate)
            }, year, month, day)

            datePickerDialog.show()
        }

        // Handle button click for adding expenses
        addExpenseButton.setOnClickListener {
            val name = expenseName.text.toString().trim()
            val amountText = expenseAmount.text.toString().trim()

            if (name.isEmpty() || amountText.isEmpty()) {
                Toast.makeText(this, R.string.invalid_details, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if (amount == null) {
                Toast.makeText(this, R.string.invalid_amount, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newExpense = Expense(name, amount, selectedDate)
            expenses.add(newExpense)
            expenseAdapter.notifyItemInserted(expenses.size - 1)

            // Update Footer Fragment
            updateTotalExpense()

            // Clear inputs
            expenseName.text?.clear()
            expenseAmount.text?.clear()
            selectedDate = "Not Selected"
            selectedDateText.text = getString(R.string.default_date)
        }

        // Implicit Intent - Open Financial Tips Website
        financialTipsButton.setOnClickListener {
            val url = "https://www.canada.ca/en/financial-consumer-agency/services/covid-19-managing-financial-health.html"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    // Function to Remove an Expense
    private fun removeExpense(position: Int) {
        if (position in expenses.indices) {
            expenses.removeAt(position)
            expenseAdapter.notifyItemRemoved(position)
            expenseAdapter.notifyItemRangeChanged(position, expenses.size)
            updateTotalExpense()
            Toast.makeText(this, R.string.expense_removed, Toast.LENGTH_SHORT).show()
        }
    }

    // Function to Update Total Expense in Footer Fragment
    private fun updateTotalExpense() {
        val total = expenses.sumOf { it.amount }
        footerFragment?.updateTotalExpense(total)
    }

    // Function to Show Expense Details (Explicit Intent)
    private fun showExpenseDetails(position: Int) {
        val expense = expenses[position]
        val intent = Intent(this, ExpenseDetailsActivity::class.java).apply {
            putExtra("expenseName", expense.name)
            putExtra("expenseAmount", expense.amount.toString())
            putExtra("expenseDate", expense.date)
        }
        startActivity(intent)
    }
}
