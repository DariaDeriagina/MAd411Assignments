package com.example.assignmnet_7_android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class FooterFragment : Fragment() {
    private lateinit var totalExpenseText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_footer, container, false)
        totalExpenseText = view.findViewById(R.id.totalExpenseText)
        return view
    }

    fun updateTotalExpense(total: Double) {
        if (::totalExpenseText.isInitialized) {
            totalExpenseText.text = "Total Expense: $%.2f".format(total)
        }
    }

}
