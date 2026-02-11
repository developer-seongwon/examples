package org.sw.member.infra.logging

import org.springframework.stereotype.Component
import org.sw.member.domain.config.AppProperties
import org.sw.member.domain.logging.Tracker
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

/**
 * Custom Time-Ordered ID Generator
 *
 * Format: yyyyMMdd-HHmm-ssSSS-SERVER(6)-SEQ(4)+RAND(4)
 * Example: 20260210-1423-15873-EXP101-0001A7K3
 *
 * @see docs/custom-time-ordered-id-spec.md
 */
@Component
class TrackerImpl(
    private val appProperties: AppProperties
) : Tracker {
    private val sequence = AtomicInteger(0)

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    private val timeFormatter = DateTimeFormatter.ofPattern("HHmm")

    companion object {
        private const val SEQ_MAX = 9999
        private const val RAND_LENGTH = 4
        private val BASE36_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()
    }

    /**
     * Core ID를 생성한다.
     *
     * 20260210-1423-15873-EXP101-0001A7K3
     */
    override fun newTransactionId(): String {
        val now = LocalDateTime.now()
        val date = now.format(dateFormatter)
        val time = now.format(timeFormatter)
        val secMs = "%02d%03d".format(now.second, now.nano / 1_000_000)
        val server = appProperties.id
        val seq = "%04d".format(nextSequence())
        val rand = randomBase36(RAND_LENGTH)

        return "$date-$time-$secMs-$server-$seq$rand"
    }

    private fun nextSequence(): Int {
        return sequence.updateAndGet { current ->
            if (current >= SEQ_MAX) 1 else current + 1
        }
    }

    private fun randomBase36(length: Int): String {
        return buildString(length) {
            repeat(length) {
                append(BASE36_CHARS[Random.Default.nextInt(BASE36_CHARS.size)])
            }
        }
    }
}