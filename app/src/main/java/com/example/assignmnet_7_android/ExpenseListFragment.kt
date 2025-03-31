package com.example.assignmnet_7_android.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmnet_7_android.ExpenseAdapter
import com.example.assignmnet_7_android.R
import com.example.assignmnet_7_android.models.Expense
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.content.Context
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class ExpenseListFragment : Fragment() {

    private lateinit var nameInput: TextInputEditText
    private lateinit var amountInput: TextInputEditText
    private lateinit var selectedDateText: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExpenseAdapter
    private lateinit var totalText: TextView

    private val expenses = mutableListOf<Expense>()
    private var selectedDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_expense_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameInput = view.findViewById(R.id.expenseNameInput)
        amountInput = view.findViewById(R.id.expenseAmountInput)
        selectedDateText = view.findViewById(R.id.selectedDateText)
        totalText = view.findViewById(R.id.totalExpenseText)
        recyclerView = view.findViewById(R.id.expenseRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ExpenseAdapter(expenses)
        recyclerView.adapter = adapter

        loadExpensesFromFile()
        updateTotal()

        view.findViewById<Button>(R.id.selectDateButton).setOnClickListener {
            showDatePicker()
        }

        view.findViewById<Button>(R.id.addExpenseButton).setOnClickListener {
            addExpense()
        }

        view.findViewById<Button>(R.id.tipsButton).setOnClickListener {
            Snackbar.make(view, "💡 Tip: Track daily to avoid overspending!", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _, year, month, day ->
            val date = "$day/${month + 1}/$year"
            selectedDate = date
            selectedDateText.text = date
        },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun addExpense() {
        val name = nameInput.text.toString()
        val amount = amountInput.text.toString().toDoubleOrNull()
        val date = if (selectedDate.isNotEmpty()) selectedDate else "Not Set"

        if (name.isBlank() || amount == null) {
            Toast.makeText(requireContext(), "Enter valid name and amount", Toast.LENGTH_SHORT).show()
            return
        }

        val expense = Expense(
            name = name,
            amount = amount,
            date = date,
            costAssociated = true,
            currency = "CAD", // default
            convertedCost = amount // default same as amount
        )

        expenses.add(expense)
        adapter.notifyItemInserted(expenses.size - 1)
        saveExpensesToFile()
        updateTotal()

        nameInput.text?.clear()
        amountInput.text?.clear()
        selectedDateText.text = getString(R.string.default_date)
        selectedDate = ""
    }

    private fun updateTotal() {
        val total = expenses.sumOf { it.convertedCost }
        totalText.text = "Total Expense: $%.2f".format(total)
    }

    private fun saveExpensesToFile() {
        try {
            val json = Gson().toJson(expenses)
            requireContext().openFileOutput("expenses.json", Context.MODE_PRIVATE).use {
                it.write(json.toByteArray())
            }
        } catch (e: IOException) {
            Toast.makeText(requireContext(), "Save failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadExpensesFromFile() {
        try {
            val file = File(requireContext().filesDir, "expenses.json")
            if (!file.exists()) return

            val json = file.readText()
            val type = object : TypeToken<List<Expense>>() {}.type
            val loaded = Gson().fromJson<List<Expense>>(json, type)

            expenses.clear()
            expenses.addAll(loaded)
            adapter.notifyDataSetChanged()
        } catch (e: IOException) {
            Toast.makeText(requireContext(), "Load failed", Toast.LENGTH_SHORT).show()
        }
    }
}
