package com.example.assignmnet_7_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

class ExpenseDetailsFragment : Fragment() {

    private val args by navArgs<ExpenseDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_expense_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.detailExpenseName).text = "Name: ${args.expenseName}"
        view.findViewById<TextView>(R.id.detailExpenseAmount).text = "Amount: $${args.expenseAmount}"
        view.findViewById<TextView>(R.id.detailExpenseDate).text = "Date: ${args.expenseDate}"
    }
}
