package com.example.loginandregistration

import java.util.*
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoanData(
    var name: String = "Unknown",
    var amount: Double = 0.0,
    var dateLent: Date = Date(0),
    var description: String = "",
    var repaid: Double = 0.0,
    var dateRepaid: Date = Date(0),
    var isRepaid: Boolean = true,
    var ownerId: String? = null,
    var objectId: String? = null
) : Parcelable {
    fun balanceRemaining(): Double {
        return amount - repaid
    }
}
