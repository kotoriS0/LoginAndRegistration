package com.example.loginandregistration

import java.util.*

data class LoanData(
    var name: String = "Unknown",
    var amount: Double = 0.0,
    var dateLent: Date = Date(0),
    var description: String = "",
    var repaid: Double = 0.0,
    var dateRepaid: Date = Date(0),
    var isRepaid: Boolean = true
) {
    fun balanceRemaining() : Double {
        return amount - repaid
    }
}
