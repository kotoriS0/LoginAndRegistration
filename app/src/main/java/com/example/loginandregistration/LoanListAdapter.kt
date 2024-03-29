package com.example.loginandregistration

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import weborb.util.ThreadContext.context


class LoanListAdapter(var data: MutableList<LoanData>): RecyclerView.Adapter<LoanListAdapter.ViewHolder>() {
    companion object {
        const val TAG = "LoanListAdapter"
        const val EXTRA_LOAN = "loan"
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textViewName: TextView
        var textViewAmount: TextView
        var layout: ConstraintLayout
        var context: Context

        init {
            textViewName = view.findViewById(R.id.textView_itemLoanList_name)
            textViewAmount = view.findViewById(R.id.textView_itemLoanList_amount)
            layout = view.findViewById(R.id.layout_itemLoanList)
            context = textViewName.context
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_loan_list, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val loan = data[position]
        //Log.d(TAG, "onBindViewHolder: ${loan.name} / ${loan.balanceRemaining()}")
        viewHolder.textViewName.text = loan.name
        viewHolder.textViewAmount.text = String.format("$%.2f", loan.amount - loan.repaid)

        viewHolder.layout.isLongClickable = true
        viewHolder.layout.setOnLongClickListener {
            val popMenu = PopupMenu(viewHolder.textViewName.context, viewHolder.textViewName)
            popMenu.inflate(R.menu.menu_loan_list)
            popMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuItem_loanList_delete -> {
                        deleteFromBackendless(position)
                        true
                    }
                    else -> true
                }
            }
            popMenu.show()
            true

        }

        viewHolder.layout.isClickable = true
        viewHolder.layout.setOnClickListener {
            val detailIntent =Intent(it.context, LoanDetailActivity::class.java)
            detailIntent.putExtra(EXTRA_LOAN, loan)
            it.context.startActivity(detailIntent)
        }
    }

    private fun deleteFromBackendless(position: Int) {
        Log.d("LoanAdapter", "deleteFromBackendless: Trying to delete ${data[position]}")
        // put in the code to delete the item using the callback from Backendless
        // in the handleResponse, we'll need to also delete the item from the loanList
        // and make sure that the recyclerview is updated

        Backendless.Data.of(LoanData::class.java).remove( data[position],
            object : AsyncCallback<Long?> {
                override fun handleResponse(response: Long?) {
                    // Person has been deleted. The response is the
                    // time in milliseconds when the object was deleted
                    //Toast.makeText(context, "${data[position].name} Deleted", Toast.LENGTH_SHORT).show()
                    data.removeAt(position)
                    //reload page
                }

                override fun handleFault(fault: BackendlessFault) {
                    Log.d("BirthdayDetail", "handleFault: ${fault.message}")
                }
            })

    }

    override fun getItemCount() = data.size

}
