package com.example.loginandregistration

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LoanListAdapter(var data: List<LoanData>): RecyclerView.Adapter<LoanListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textViewName: TextView
        var textViewAmountPaid: TextView
        var textViewDate: TextView
        var textViewDateLastRepaid: TextView
        var switchForgiveDebt: Switch
        var textViewDescription: TextView

        init {
            textViewName = view.findViewById(R.id.textView_itemLoanList_name)
            textViewAmountPaid = view.findViewById(R.id.textView_itemLoanList_amountPaid)
            textViewDate = view.findViewById(R.id.textView_itemLoanList_date)
            textViewDateLastRepaid = view.findViewById(R.id.textView_itemLoanList_dateLastRepaid)
            switchForgiveDebt = view.findViewById(R.id.switch_itemLoanList_forgiveDebt)
            textViewDescription = view.findViewById(R.id.textView_itemLoanList_description)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_loan_list, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val loan = data[position]
        viewHolder.textViewName.text = loan.name
        viewHolder.textViewAmountPaid.text = loan.repaid.toString()
        viewHolder.textViewDate.text = loan.dateLent.toString()
        viewHolder.textViewDateLastRepaid.text = loan.dateRepaid.toString()
        viewHolder.switchForgiveDebt.isChecked = loan.isRepaid
        viewHolder.textViewDescription.text = loan.description
    }

    override fun getItemCount() = data.size
}