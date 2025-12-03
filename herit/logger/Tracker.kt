package net.herit.logger

class Tracker(
    val transactionId: String,
    val trackerId: String,
    val callId: Int
) {

    override fun toString(): String {
        return "[$transactionId][$trackerId][${"%04d".format(callId)}]"
    }
}