package com.example.loginandregistration

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView


class LoanListAdapter(var data: List<LoanData>): RecyclerView.Adapter<LoanListAdapter.ViewHolder>() {
    companion object {
        const val TAG = "LoanListAdapter"
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textViewName: TextView
        var textViewAmount: TextView
        var layout: ConstraintLayout

        init {
            textViewAmount = view.findViewById(R.id.textView_itemLoanList_amount)
            textViewName = view.findViewById(R.id.waefwafweafsf)
            layout = view.findViewById(R.id.layout_itemLoanList)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.activity_loan_detail, viewGroup, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val loan = data[position]
        Log.d(TAG, "onBindViewHolder: ${loan.name} / ${loan.balanceRemaining()}")
        viewHolder.textViewName.text = loan.name
        viewHolder.textViewAmount.text = "$${loan.balanceRemaining()}"
    }

    override fun getItemCount() = data.size

    /*
    val popMenu = PopupMenu(context, holder.textViewBorrower)
            popMenu.inflate(R.menu.menu_loan_list_context)
            popMenu.setOnMenuItemClickListener {
                 when(it.itemId) {
                    R.id.menu_loanlist_delete -> {
                        deleteFromBackendless(position)
                        true
                    }
                     else -> true
                 }
            }
            popMenu.show()
            true
     */
}