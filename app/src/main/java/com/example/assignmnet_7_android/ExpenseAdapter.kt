package com.example.assignmnet_7_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmnet_7_android.models.Expense

class ExpenseAdapter(
    private val expenses: MutableList<Expense>,
    private val onDeleteClick: (Int) -> Unit,
    private val onDetailsClick: (Expense) -> Unit
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val expenseName: TextView = view.findViewById(R.id.expenseNameText)
        val expenseAmount: TextView = view.findViewById(R.id.expenseAmountText)
        val expenseConverted: TextView = view.findViewById(R.id.expenseConvertedText)
        val deleteButton: Button = view.findViewById(R.id.deleteExpenseButton)
        val detailsButton: Button = view.findViewById(R.id.showDetailsButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.expenseName.text = expense.name
        holder.expenseAmount.text = "Original: ${expense.amount} ${expense.currency}"
        holder.expenseConverted.text = "Converted (CAD): $%.2f".format(expense.convertedCost)

        holder.deleteButton.setOnClickListener {
            onDeleteClick(position)
        }

        holder.detailsButton.setOnClickListener {
            onDetailsClick(expense)
        }
    }

    override fun getItemCount(): Int = expenses.size
}
