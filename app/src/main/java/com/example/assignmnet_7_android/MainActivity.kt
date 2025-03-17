package com.example.assignmnet_7_android

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var expenseAdapter: ExpenseAdapter
    private val expenses = mutableListOf<Expense>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get UI elements
        val expenseName = findViewById<EditText>(R.id.expenseName)
        val expenseAmount = findViewById<EditText>(R.id.expenseAmount)
        val expenseDate = findViewById<EditText>(R.id.expenseDate)
        val addExpenseButton = findViewById<Button>(R.id.addExpenseButton)
        val recyclerView = findViewById<RecyclerView>(R.id.expenseRecyclerView)

        // Setup RecyclerView
        expenseAdapter = ExpenseAdapter(expenses) { position -> removeExpense(position) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = expenseAdapter

        // Handle button click for adding expenses
        addExpenseButton.setOnClickListener {
            val name = expenseName.text.toString().trim()
            val amount = expenseAmount.text.toString().trim()

            if (name.isEmpty() || amount.isEmpty()) {
                Toast.makeText(this, "Please enter name and amount", Toast.LENGTH_SHORT).show()
            } else {
                val newExpense = Expense(name, "$$amount")
                expenseAdapter.addExpense(newExpense)
                expenseName.text.clear()
                expenseAmount.text.clear()
                expenseDate.text.clear()  // Clear date input as well
            }
        }
    }

    private fun removeExpense(position: Int) {
        expenseAdapter.removeExpense(position)
        Toast.makeText(this, "Expense removed", Toast.LENGTH_SHORT).show()
    }
}
