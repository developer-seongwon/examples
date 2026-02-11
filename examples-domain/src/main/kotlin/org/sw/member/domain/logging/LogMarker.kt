package org.sw.member.domain.logging

import org.slf4j.Marker
import org.slf4j.MarkerFactory

object LogMarker {
    val OUT: Marker = MarkerFactory.getMarker("OUT")
    val CALL: Marker = MarkerFactory.getMarker("CALL")
    val ERROR: Marker = MarkerFactory.getMarker("ERROR").apply {
        add(OUT)
        add(CALL)
    }
}
