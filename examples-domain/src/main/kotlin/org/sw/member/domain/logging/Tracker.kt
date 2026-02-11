package org.sw.member.domain.logging

interface Tracker {


    fun newTransactionId(): String

    companion object {
        const val HEADER_TRANSACTION_ID = "X-Transaction-Id"
        const val LOGGER_TRANSACTION_ID = "transactionId"
    }
}