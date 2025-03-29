package com.example.assignmnet_7_android

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignmnet_7_android.databinding.FragmentExpenseListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException
import java.util.*

class ExpenseListFragment : Fragment() {

    private var _binding: FragmentExpenseListBinding? = null
    private val binding get() = _binding!!

    private lateinit var expenseAdapter: ExpenseAdapter
    private val expenses = mutableListOf<Expense>()
    private var selectedDate: String = "Not Selected"
    private val fileName = "expenses.json"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpenseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expenseAdapter = ExpenseAdapter(expenses, ::removeExpense, ::showExpenseDetails)
        binding.expenseRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.expenseRecyclerView.adapter = expenseAdapter

        loadExpensesFromFile()
        updateTotalExpense()

        binding.selectDateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, y, m, d ->
                    selectedDate = "$d/${m + 1}/$y"
                    binding.selectedDateText.text = getString(R.string.selected_date, selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.addExpenseButton.setOnClickListener {
            val name = binding.expenseNameInput.text.toString().trim()
            val amountText = binding.expenseAmountInput.text.toString().trim()

            if (name.isEmpty() || amountText.isEmpty()) {
                Toast.makeText(requireContext(), R.string.invalid_details, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toDoubleOrNull()
            if (amount == null) {
                Toast.makeText(requireContext(), R.string.invalid_amount, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val expense = Expense(name, amount, selectedDate)
            expenses.add(expense)
            expenseAdapter.notifyItemInserted(expenses.size - 1)
            saveExpensesToFile()
            updateTotalExpense()

            binding.expenseNameInput.text?.clear()
            binding.expenseAmountInput.text?.clear()
            selectedDate = "Not Selected"
            binding.selectedDateText.text = getString(R.string.default_date)
        }

        binding.tipsButton.setOnClickListener {
            val url = "https://www.canada.ca/en/financial-consumer-agency/services/covid-19-managing-financial-health.html"
            startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url)))
        }
    }

    private fun removeExpense(position: Int) {
        expenses.removeAt(position)
        expenseAdapter.notifyItemRemoved(position)
        saveExpensesToFile()
        updateTotalExpense()
    }

    private fun updateTotalExpense() {
        val total = expenses.sumOf { it.amount }
        binding.totalExpenseText.text = "Total Expense: $$total"
    }

    private fun showExpenseDetails(position: Int) {
        val expense = expenses[position]
        val action = ExpenseListFragmentDirections.actionExpenseListFragmentToExpenseDetailsFragment(
            expense.name, expense.amount.toString(), expense.date
        )
        findNavController().navigate(action)
    }

    private fun saveExpensesToFile() {
        try {
            val json = Gson().toJson(expenses)
            requireContext().openFileOutput(fileName, android.content.Context.MODE_PRIVATE).use {
                it.write(json.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadExpensesFromFile() {
        try {
            val file = File(requireContext().filesDir, fileName)
            if (!file.exists()) return

            val json = file.readText()
            val type = object : TypeToken<MutableList<Expense>>() {}.type
            val savedExpenses: MutableList<Expense> = Gson().fromJson(json, type)
            expenses.clear()
            expenses.addAll(savedExpenses)
            expenseAdapter.notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
