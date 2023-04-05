package com.example.loginandregistration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.example.loginandregistration.LoginActivity.Companion.EXTRA_USER_ID
import com.example.loginandregistration.databinding.ActivityLoanListBinding

class LoanListActivity : AppCompatActivity() {
    companion object{
        const val TAG = "LoanListActivity"
        const val EXTRA_LOAN = "loan"
    }

    private lateinit var binding: ActivityLoanListBinding
    lateinit var adapter: LoanListAdapter
    lateinit var userId: String
    var loans = listOf<LoanData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoanListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra(EXTRA_USER_ID)!!
        retrieveAllData(userId)
    }

    private fun retrieveAllData(userId: String) {
        val whereClause = "ownerId = '$userId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause
        Backendless.Data.of(LoanData::class.java).find(queryBuilder, object : AsyncCallback<List<LoanData>?> {
            override fun handleResponse(foundContacts: List<LoanData>?) {
                // all Contact instances have been found
                loans = foundContacts!!
                Log.d(TAG, "handleResponse: $loans")
                adapter = LoanListAdapter(loans)
                binding.recyclerViewLoanList.adapter = adapter
                binding.recyclerViewLoanList.layoutManager = LinearLayoutManager(this@LoanListActivity)
            }

            override fun handleFault(fault: BackendlessFault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Log.d(TAG, "handleFault: ${fault.message}")
            }
        })
    }
}