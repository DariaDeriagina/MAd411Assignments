package com.example.assignmnet_7_android.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmnet_7_android.ExpenseAdapter
import com.example.assignmnet_7_android.R
import com.example.assignmnet_7_android.models.Expense
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.content.Context
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.io.*

class ExpenseListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExpenseAdapter
    private lateinit var totalText: TextView
    private lateinit var fab: FloatingActionButton

    private val expenses = mutableListOf<Expense>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_expense_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.expenseRecyclerView)
        totalText = view.findViewById(R.id.totalExpenseText)
        fab = view.findViewById(R.id.addExpenseFab)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ExpenseAdapter(
            expenses,
            onDeleteClick = { position ->
                expenses.removeAt(position)
                adapter.notifyItemRemoved(position)
                saveExpensesToFile()
                updateTotal()
            },
            onDetailsClick = { expense ->
                val action = ExpenseListFragmentDirections
                    .actionExpenseListFragmentToExpenseDetailsFragment(
                        expenseName = expense.name,
                        expenseAmount = expense.amount.toString(),
                        expenseDate = expense.date
                    )
                findNavController().navigate(action)
            }
        )
        recyclerView.adapter = adapter

        // ✅ Handle new expense from AddExpenseFragment
        findNavController().currentBackStackEntry?.savedStateHandle?.get<Bundle>("newExpense")?.let { bundle ->
            val newExpense = Expense(
                name = bundle.getString("name", ""),
                amount = bundle.getDouble("amount", 0.0),
                date = bundle.getString("date", ""),
                costAssociated = bundle.getBoolean("costAssociated", true),
                currency = bundle.getString("currency", "CAD"),
                convertedCost = bundle.getDouble("convertedCost", 0.0)
            )
            expenses.add(newExpense)
            adapter.notifyItemInserted(expenses.size - 1)
            saveExpensesToFile()
            updateTotal()
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Bundle>("newExpense")
        }

        loadExpensesFromFile()
        updateTotal()

        // 💡 Tip button
        view.findViewById<Button>(R.id.tipsButton).setOnClickListener {
            Snackbar.make(view, "💡 Tip: Track daily to avoid overspending!", Snackbar.LENGTH_LONG).show()
        }

        // ➕ FloatingActionButton
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_expenseListFragment_to_addExpenseFragment)
        }
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
