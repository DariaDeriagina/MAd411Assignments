package com.example.assignmnet_7_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(
    private val expenses: MutableList<Expense>,
    private val onDeleteClick: (Int) -> Unit,
    private val onDetailsClick: (Int) -> Unit
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val expenseName: TextView = view.findViewById(R.id.expenseNameText)
        val expenseAmount: TextView = view.findViewById(R.id.expenseAmountText)
        val deleteButton: Button = view.findViewById(R.id.deleteExpenseButton)
        val detailsButton: Button = view.findViewById(R.id.showDetailsButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.expenseName.text = expense.name
        holder.expenseAmount.text = "$${expense.amount}"

        holder.deleteButton.setOnClickListener { onDeleteClick(position) }
        holder.detailsButton.setOnClickListener { onDetailsClick(position) } // Pass data to new activity
    }


    override fun getItemCount(): Int = expenses.size
}
