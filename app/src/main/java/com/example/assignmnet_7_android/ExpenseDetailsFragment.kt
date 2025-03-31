package com.example.assignmnet_7_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

class ExpenseDetailsFragment : Fragment() {

    // Safe Args: automatically generated class from your navigation graph
    private val args by navArgs<ExpenseDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expense_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Assign values to TextViews using arguments
        val nameText: TextView = view.findViewById(R.id.detailExpenseName)
        val amountText: TextView = view.findViewById(R.id.detailExpenseAmount)
        val dateText: TextView = view.findViewById(R.id.detailExpenseDate)

        nameText.text = getString(R.string.detail_name_format, args.expenseName)
        amountText.text = getString(R.string.detail_amount_format, args.expenseAmount)
        dateText.text = getString(R.string.detail_date_format, args.expenseDate)
    }
}
