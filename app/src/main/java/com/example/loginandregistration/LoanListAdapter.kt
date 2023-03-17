package com.example.loginandregistration

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LoanListAdapter(data: List<LoanData>): RecyclerView.Adapter<LoanListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textViewName: TextView
        var textViewAmount: TextView

        init {
            textViewName = view.findViewById(R.id.textView_itemLoanList_name)
            textViewAmount = view.findViewById(R.id.textView_itemLoanList_amount)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_loan_list, viewGroup, false)
        return ViewHolder(view)
    }
}