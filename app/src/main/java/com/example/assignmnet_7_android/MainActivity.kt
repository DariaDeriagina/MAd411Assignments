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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var expenseAdapter: ExpenseAdapter
    private val expenses = mutableListOf<Expense>()
    private lateinit var selectedDateText: TextView
    private var selectedDate: String = "Not Selected"
    private var footerFragment: FooterFragment? = null
    private val fileName = "expenses.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("Lifecycle", "onCreate called")

        // Initialize header fragment
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

        // Initialize footer fragment
        footerFragment = FooterFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.footerContainer, footerFragment!!)
            .commit()

        // Setup RecyclerView
        expenseAdapter = ExpenseAdapter(expenses, ::removeExpense, ::showExpenseDetails)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = expenseAdapter

        // Load saved expenses
        loadExpensesFromFile()
        expenseAdapter.notifyDataSetChanged()
        updateTotalExpense()

        // Date Picker
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

        // Add expense button
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

            // Save to file
            saveExpensesToFile()

            // Update footer
            updateTotalExpense()

            // Clear inputs
            expenseName.text?.clear()
            expenseAmount.text?.clear()
            selectedDate = "Not Selected"
            selectedDateText.text = getString(R.string.default_date)
        }

        // Financial Tips - Implicit Intent
        financialTipsButton.setOnClickListener {
            val url = "https://www.canada.ca/en/financial-consumer-agency/services/covid-19-managing-financial-health.html"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    // Remove expense
    private fun removeExpense(position: Int) {
        if (position in expenses.indices) {
            expenses.removeAt(position)
            expenseAdapter.notifyItemRemoved(position)
            expenseAdapter.notifyItemRangeChanged(position, expenses.size)
            saveExpensesToFile()
            updateTotalExpense()
            Toast.makeText(this, R.string.expense_removed, Toast.LENGTH_SHORT).show()
        }
    }

    // Update footer fragment
    private fun updateTotalExpense() {
        val total = expenses.sumOf { it.amount }
        footerFragment?.updateTotalExpense(total)
    }

    // Show details (explicit intent)
    private fun showExpenseDetails(position: Int) {
        val expense = expenses[position]
        val intent = Intent(this, ExpenseDetailsActivity::class.java).apply {
            putExtra("expenseName", expense.name)
            putExtra("expenseAmount", expense.amount.toString())
            putExtra("expenseDate", expense.date)
        }
        startActivity(intent)
    }

    // Save expenses to JSON file
    private fun saveExpensesToFile() {
        try {
            val json = Gson().toJson(expenses)
            openFileOutput(fileName, MODE_PRIVATE).use {
                it.write(json.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // Load expenses from JSON file
    private fun loadExpensesFromFile() {
        try {
            val file = File(filesDir, fileName)
            if (!file.exists()) return

            val json = file.readText()
            if (json.isNotBlank()) {
                val type = object : TypeToken<MutableList<Expense>>() {}.type
                val savedExpenses: MutableList<Expense> = Gson().fromJson(json, type)
                expenses.clear()
                expenses.addAll(savedExpenses)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to load saved expenses", Toast.LENGTH_SHORT).show()
        }
    }
}
