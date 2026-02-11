package org.sw.member.domain.logging

import org.slf4j.MDC

sealed interface SourceToTarget {
    fun toString(service: String): String
    override fun toString(): String

    companion object {
        private const val WIDTH = 5
        private var serviceName: String = ""

        fun init(name: String) {
            serviceName = name
        }

        internal fun getServiceName(): String = serviceName

        fun non(): SourceToTarget = Non
        fun left(target: String): SourceToTarget = Left(target, Arrow.EACH)
        fun leftIn(target: String): SourceToTarget = Left(target, Arrow.RIGHT)
        fun leftOut(target: String): SourceToTarget = Left(target, Arrow.LEFT)
        fun right(target: String): SourceToTarget = Right(target, Arrow.EACH)
        fun rightIn(target: String): SourceToTarget = Right(target, Arrow.LEFT)
        fun rightOut(target: String): SourceToTarget = Right(target, Arrow.RIGHT)

        inline fun withDirection(direction: SourceToTarget, block: () -> Unit) {
            MDC.put("direction", direction.toString())
            try { block() } finally { MDC.remove("direction") }
        }

        // 왼쪽 타겟: 중앙정렬, 홀수 여백은 왼쪽([쪽)으로
        internal fun padLeft(value: String): String {
            val trimmed = if (value.length > WIDTH) value.substring(0, WIDTH) else value
            val total = WIDTH - trimmed.length
            val r = total / 2
            val l = total - r
            return " ".repeat(l) + trimmed + " ".repeat(r)
        }

        // 오른쪽 타겟: 중앙정렬, 홀수 여백은 오른쪽(]쪽)으로
        internal fun padRight(value: String): String {
            val trimmed = if (value.length > WIDTH) value.substring(0, WIDTH) else value
            val total = WIDTH - trimmed.length
            val l = total / 2
            val r = total - l
            return " ".repeat(l) + trimmed + " ".repeat(r)
        }

        internal fun padCenter(value: String): String {
            val trimmed = if (value.length > WIDTH) value.substring(0, WIDTH) else value
            val total = WIDTH - trimmed.length
            val l = total / 2
            val r = total - l
            return " ".repeat(l) + trimmed + " ".repeat(r)
        }
    }
}

enum class Arrow(val symbol: String) {
    EACH("<->"),
    LEFT("<--"),
    RIGHT("-->"),
    NONE("   ")
}

private object Non : SourceToTarget {
    override fun toString(service: String): String {
        val left = SourceToTarget.padLeft("") + " " + Arrow.NONE.symbol
        val right = Arrow.NONE.symbol + " " + SourceToTarget.padRight("")
        return "[$left${SourceToTarget.padCenter(service)}$right]"
    }

    override fun toString(): String = toString(SourceToTarget.getServiceName())
}

private class Left(val target: String, val arrow: Arrow) : SourceToTarget {
    override fun toString(service: String): String {
        val left = SourceToTarget.padLeft(target) + " " + arrow.symbol
        val right = Arrow.NONE.symbol + " " + SourceToTarget.padRight("")
        return "[$left${SourceToTarget.padCenter(service)}$right]"
    }

    override fun toString(): String = toString(SourceToTarget.getServiceName())
}

private class Right(val target: String, val arrow: Arrow) : SourceToTarget {
    override fun toString(service: String): String {
        val left = SourceToTarget.padLeft("") + " " + Arrow.NONE.symbol
        val right = arrow.symbol + " " + SourceToTarget.padRight(target)
        return "[$left${SourceToTarget.padCenter(service)}$right]"
    }

    override fun toString(): String = toString(SourceToTarget.getServiceName())
}
