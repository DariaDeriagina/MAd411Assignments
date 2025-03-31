package com.example.assignmnet_7_android.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.assignmnet_7_android.R
import com.example.assignmnet_7_android.RetrofitInstance
import com.example.assignmnet_7_android.models.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AddExpenseFragment : Fragment() {

    private lateinit var currencySpinner: Spinner
    private lateinit var costEditText: EditText
    private lateinit var convertedCostTextView: TextView
    private lateinit var progressBar: ProgressBar
    private var exchangeRates: Map<String, Double> = emptyMap()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_add_expense, container, false)

        val nameEditText: EditText = view.findViewById(R.id.expenseName)
        val dateEditText: EditText = view.findViewById(R.id.expenseDate)
        val costCheckBox: CheckBox = view.findViewById(R.id.costAssociatedCheckBox)
        costEditText = view.findViewById(R.id.expenseCost)
        currencySpinner = view.findViewById(R.id.currencySpinner)
        convertedCostTextView = view.findViewById(R.id.convertedCostTextView)
        progressBar = view.findViewById(R.id.progressBar)
        val saveButton: Button = view.findViewById(R.id.saveButton)

        // Date Picker
        dateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    dateEditText.setText("$day/${month + 1}/$year")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Fetch currency data
        lifecycleScope.launch {
            progressBar.visibility = View.VISIBLE
            try {
                val result = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getRates("cad")
                }
                exchangeRates = result.conversion_rates
                progressBar.visibility = View.GONE

                val currencyCodes = exchangeRates.keys.sorted()
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, currencyCodes)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                currencySpinner.adapter = adapter

                val defaultIndex = currencyCodes.indexOf("cad")
                if (defaultIndex != -1) currencySpinner.setSelection(defaultIndex)

                currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        calculateConvertedCost()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

                costEditText.setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) calculateConvertedCost()
                }

            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Failed to load exchange rates: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Save
        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val date = dateEditText.text.toString()
            val costAssociated = costCheckBox.isChecked
            val amount = costEditText.text.toString().toDoubleOrNull() ?: 0.0
            val currency = currencySpinner.selectedItem.toString()
            val convertedCost = calculateConvertedCost()

            val expense = Expense(
                name = name,
                amount = amount,
                date = date,
                costAssociated = costAssociated,
                currency = currency,
                convertedCost = convertedCost
            )

            findNavController().previousBackStackEntry?.savedStateHandle?.set("newExpense", bundleOf(
                "name" to expense.name,
                "amount" to expense.amount,
                "date" to expense.date,
                "costAssociated" to expense.costAssociated,
                "currency" to expense.currency,
                "convertedCost" to expense.convertedCost
            ))

            findNavController().popBackStack()
        }

        return view
    }

    private fun calculateConvertedCost(): Double {
        val amount = costEditText.text.toString().toDoubleOrNull() ?: 0.0
        val currency = currencySpinner.selectedItem.toString()
        val rate = exchangeRates[currency] ?: 1.0
        val converted = amount * rate
        convertedCostTextView.text = getString(R.string.converted_text, converted)
        return converted
    }
}
