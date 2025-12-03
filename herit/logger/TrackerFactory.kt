package net.herit.logger

import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

class TrackerFactory {
    companion object {
        private val counter: AtomicInteger = AtomicInteger(0)
        fun newInstance(): Tracker {
            val transactionId = transactionId()
            val callId = callId()
            val trackerId = trackerId(transactionId, callId)
            return newInstance(transactionId, trackerId, callId)
        }

        fun newInstance(transactionId: String): Tracker {
            val callId = callId();
            return newInstance(transactionId, trackerId(transactionId(), callId), callId)
        }


        fun newInstance(tracker: Tracker): Tracker {
            return Tracker(tracker.transactionId, tracker.trackerId, callId())
        }

        private fun newInstance(transactionId: String, trackerId: String, callId: Int): Tracker {
            return Tracker(transactionId, trackerId, callId)
        }

        private fun transactionId(): String = UUID.randomUUID().toString();

        private fun callId(): Int {
            return counter.updateAndGet { if (it >= 9999) 0 else it + 1 }
        }

        private fun trackerId(transactionId: String, callId: Int): String {
            return "${transactionId.substring(0, 13)}-${"%04d".format(callId)}"
        }
    }
}
