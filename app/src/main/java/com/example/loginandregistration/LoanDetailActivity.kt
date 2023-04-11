package com.example.loginandregistration

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.example.loginandregistration.LoginActivity.Companion.EXTRA_USER_ID
import com.example.loginandregistration.databinding.ActivityLoanDetailBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class LoanDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoanDetailBinding
    var loanIsEditable = false
    lateinit var loan : LoanData

    var cal = Calendar.getInstance()

    companion object {
        const val TAG = "LoanDetailActivity"
        const val EXTRA_LOAN = "loan"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var passedLoan = intent.getParcelableExtra<LoanData>(EXTRA_LOAN)
        if(passedLoan == null) {
            loan = LoanData()
            loanIsEditable = true
            binding.groupLoanDetailInfo.visibility = View.GONE
            toggleEditable()
        }
        else {

            loan = intent.getParcelableExtra<LoanData>(EXTRA_LOAN) ?: LoanData()
            binding.textViewLoanDetailName.text = loan.name
            binding.textViewLoanDetailAmountPaid.text = "$${loan.repaid}"
            binding.textViewLoanDetailDateLentInfo.text = dateFormater(loan.dateLent)
            binding.textViewLoanDetailDateLastRepaidInfo.text = dateFormater(loan.dateRepaid)
            binding.textViewLoanDetailDescription.text = loan.description
            binding.textViewLoanDetailAmountOwed.text = "$${loan.balanceRemaining()}"
            binding.textViewLoanDetailInitialLoan.text = "$${loan.amount}"
            if (loan.isRepaid) {
                binding.imageViewLoanDetailStatus.setImageDrawable(
                    AppCompatResources.getDrawable(
                        binding.textViewLoanDetailName.context, R.drawable.ic_baseline_check_24
                    )
                )
            } else {
                binding.imageViewLoanDetailStatus.setImageDrawable(
                    AppCompatResources.getDrawable(
                        binding.textViewLoanDetailName.context, R.drawable.ic_baseline_clear_24
                    )
                )
            }
            toggleEditable()
        }

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        binding.buttonLoanDetailSave.setOnClickListener {
            // edit loan data on backendless and on screen

            if(loan.ownerId.isNullOrBlank()) {
                loan.ownerId = intent.getStringExtra(LoginActivity.Companion.EXTRA_USER_ID)!!
            }

            //lateinit var newLoan: LoanData
            loan.ownerId = loan.ownerId
            loan.name = binding.editTextLoanDetailName.text.toString() ?: loan.name
            loan.amount = binding.editTextLoanDetailInitialLoan.toString().toDouble() ?: loan.repaid
            loan.dateLent
            loan.repaid = binding.editTextLoanDetailAmountRepaid.toString().toDouble() ?: loan.repaid
            loan.description = binding.editTextLoanDetailDescription.toString() ?: loan.description
            loan.repaid = binding.editTextLoanDetailAmountRepaid.toString().toDouble() ?: loan.repaid
            loan.dateRepaid

            Backendless.Data.of(LoanData::class.java).save(loan,
            object : AsyncCallback<LoanData> {
                override fun handleResponse(savedLoanData: LoanData) {
                    savedLoanData.ownerId = loan.ownerId
                    savedLoanData.name = binding.editTextLoanDetailName.text.toString() ?: loan.name
                    savedLoanData.amount = binding.editTextLoanDetailInitialLoan.toString().toDouble() ?: loan.repaid
                    savedLoanData.dateLent
                    savedLoanData.repaid = binding.editTextLoanDetailAmountRepaid.toString().toDouble() ?: loan.repaid
                    savedLoanData.description = binding.editTextLoanDetailDescription.toString() ?: loan.description
                    savedLoanData.repaid = binding.editTextLoanDetailAmountRepaid.toString().toDouble() ?: loan.repaid
                    savedLoanData.dateRepaid

                    Backendless.Data.of(LoanData::class.java).save( savedLoanData, object: AsyncCallback<LoanData> {
                        override fun handleResponse(response: LoanData)
                        {
                            // Contact instance has been updated
                            //Log.d(TAG, "handleResponse: $response")
                            if(loan.repaid >= loan.amount) {
                                loan.isRepaid == true
                                binding.imageViewLoanDetailStatus.setImageDrawable(
                                    AppCompatResources.getDrawable(
                                        binding.textViewLoanDetailName.context, R.drawable.ic_baseline_check_24
                                    )
                                )
                            }
                            else {
                                binding.imageViewLoanDetailStatus.setImageDrawable(
                                    AppCompatResources.getDrawable(
                                        binding.textViewLoanDetailName.context, R.drawable.ic_baseline_clear_24
                                    )
                                )
                            }
                            toggleEditable()
                        }

                        override fun handleFault(fault: BackendlessFault?) {
                            Log.d(TAG, "handleFault: $fault")
                        }
                    } )
                }

                override fun handleFault(fault: BackendlessFault) {
                    Log.d(TAG, "handleFault: $fault")
                }
            })
        }

        binding.buttonLoanDetailEditDateLent.setOnClickListener{
                DatePickerDialog(this.baseContext,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.buttonLoanDetailEditDateLastRepaid.setOnClickListener {

            DatePickerDialog(this.baseContext,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.textViewLoanDetailDateLentEdit.text = sdf.format(cal.time)
        binding.textViewLoanDetailDateLastRepaidEdit.text = sdf.format(cal.time)
    }

    fun dateFormater(date: Date): String {
        return "${date.month}/${date.day}/${date.year + 1900}"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_loan_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.menu_item_loan_detail_edit -> {
                toggleEditable()
                true
            }
            R.id.menu_item_loan_detail_delete -> {
                deleteFromBackendless()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteFromBackendless() {
        Backendless.Data.of(LoanData::class.java).remove( loan,
            object : AsyncCallback<Long?> {
                override fun handleResponse(response: Long?) {
                    // Person has been deleted. The response is the
                    // time in milliseconds when the object was deleted
                    Toast.makeText(this@LoanDetailActivity, "${loan.name} Deleted", Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun handleFault(fault: BackendlessFault) {
                    Log.d("BirthdayDetail", "handleFault: ${fault.message}")
                }
            })
    }

    private fun toggleEditable() {
        if (loanIsEditable) {
            loanIsEditable = false
//            binding.buttonLoanDetailSave.isEnabled = false
//            binding.buttonLoanDetailSave.visibility = View.GONE
//            binding.imageViewLoanDetailStatus.setImageDrawable(
//                AppCompatResources.getDrawable(
//                    binding.textViewLoanDetailName.context, R.drawable.ic_baseline_clear_24))
//            binding.editTextLoanDetailName.inputType = InputType.TYPE_NULL
//            binding.editTextLoanDetailName.isEnabled = false
//            binding.editTextLoanDetailAmountRepaid.inputType = InputType.TYPE_NULL
//            binding.editTextLoanDetailAmountRepaid.isEnabled = false
//            binding.editTextLoanDetailInitialLoan.inputType = InputType.TYPE_NULL
//            binding.editTextLoanDetailInitialLoan.isEnabled = false
            //binding.checkBoxLoanDetailIsFullyRepaid.isClickable = false
            binding.groupLoanDetailEdit.visibility = View.VISIBLE
            binding.groupLoanDetailInfo.visibility = View.GONE
        } else {
            loanIsEditable = true
//            binding.buttonLoanDetailSave.isEnabled = true
//            binding.buttonLoanDetailSave.visibility = View.VISIBLE
//            binding.imageViewLoanDetailStatus.setImageDrawable(
//                AppCompatResources.getDrawable(
//                    binding.textViewLoanDetailName.context, R.drawable.ic_baseline_check_24))
//            binding.editTextLoanDetailName.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
//            binding.editTextLoanDetailName.isEnabled = true
//            binding.editTextLoanDetailAmountRepaid.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
//            binding.editTextLoanDetailAmountRepaid.isEnabled = true
//            binding.editTextLoanDetailInitialLoan.inputType = InputType.TYPE_NUMBER_VARIATION_NORMAL
//            binding.editTextLoanDetailInitialLoan.isEnabled = true
            //binding.checkBoxLoanDetailIsFullyRepaid.isClickable = true
            binding.groupLoanDetailEdit.visibility = View.GONE
            binding.groupLoanDetailInfo.visibility = View.VISIBLE
        }
    }
}