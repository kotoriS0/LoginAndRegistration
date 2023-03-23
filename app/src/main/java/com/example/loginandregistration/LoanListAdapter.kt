package com.example.loginandregistration

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import java.util.*


class LoanListAdapter(var data: List<LoanData>): RecyclerView.Adapter<LoanListAdapter.ViewHolder>() {
    companion object {
        const val TAG = "LoanListAdapter"
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textViewName: TextView
        var textViewAmountPaid: TextView
        var textViewDate: TextView
        var textViewDateLastRepaid: TextView
        //var switchForgiveDebt: Switch
        var textViewDescription: TextView
        var imageViewStatus: ImageView
        var textViewAmountOwed: TextView
        var textViewInitialLoan: TextView

        init {
            textViewName = view.findViewById(R.id.textView_itemLoanList_name)
            textViewAmountPaid = view.findViewById(R.id.textView_itemLoanList_amountPaid)
            textViewDate = view.findViewById(R.id.textView_itemLoanList_date)
            textViewDateLastRepaid = view.findViewById(R.id.textView_itemLoanList_dateLastRepaid)
            //switchForgiveDebt = view.findViewById(R.id.switch_itemLoanList_forgiveDebt)
            textViewDescription = view.findViewById(R.id.textView_itemLoanList_description)
            imageViewStatus = view.findViewById(R.id.imageView_itemLoanList_status)
            textViewAmountOwed = view.findViewById(R.id.textView_itemLoanList_amountOwed)
            textViewInitialLoan = view.findViewById(R.id.textView_itemLoanList_initialLoan)
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
        viewHolder.textViewAmountPaid.text = "$${loan.repaid}"
        viewHolder.textViewDate.text = dateFormater(loan.dateLent)
        viewHolder.textViewDateLastRepaid.text = dateFormater(loan.dateRepaid)
        //viewHolder.switchForgiveDebt.isChecked = loan.isRepaid
        viewHolder.textViewDescription.text = loan.description
        viewHolder.textViewAmountOwed.text = "$${loan.balanceRemaining()}"
        viewHolder.textViewInitialLoan.text = "$${loan.amount}"
        if(loan.isRepaid) {
            viewHolder.imageViewStatus.setImageDrawable(
                AppCompatResources.getDrawable(
                    viewHolder.textViewName.context, R.drawable.ic_baseline_check_24))
        }
        else {
            viewHolder.imageViewStatus.setImageDrawable(
                AppCompatResources.getDrawable(
                    viewHolder.textViewName.context, R.drawable.ic_baseline_clear_24))
        }

        // add menu with option to forgive debt, add amount paid, delete item
        viewHolder.switchForgiveDebt.setOnClickListener {
            if(viewHolder.switchForgiveDebt.isChecked) {
                viewHolder.imageViewStatus.setImageDrawable(
                    AppCompatResources.getDrawable(
                        viewHolder.textViewName.context, R.drawable.ic_baseline_check_24))
                // set isRepaid to true

                Backendless.Data.of(LoanData::class.java)
                    .save(loan, object : AsyncCallback<LoanData> {
                        override fun handleResponse(savedLoan: LoanData) {
                            savedLoan.isRepaid = true
                            savedLoan.dateRepaid = Date()
                            Backendless.Data.of(LoanData::class.java)
                                .save(savedLoan, object : AsyncCallback<LoanData?> {
                                    override fun handleResponse(response: LoanData?) {
                                        // Contact instance has been updated
                                        Log.d(TAG, "date: ${savedLoan.dateRepaid} handleResponse: $response")
                                    }

                                    override fun handleFault(fault: BackendlessFault) {
                                        Log.d(TAG, "handleFault ${fault.message}")
                                        // an error has occurred, the error code can be retrieved with fault.getCode()
                                    }
                                })
                        }

                        override fun handleFault(fault: BackendlessFault) {
                            // an error has occurred, the error code can be retrieved with fault.getCode()
                        }
                    })
            }
        }
    }

    override fun getItemCount() = data.size

    fun dateFormater(date: Date): String {
        return "${date.month}/${date.date}/${date.year + 1900} ${date.hours}:${date.minutes}"
    }

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