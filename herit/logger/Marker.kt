package net.herit.logger

import org.slf4j.MarkerFactory

object Marker {
    val Error = MarkerFactory.getMarker("ERROR");
    val Out = MarkerFactory.getMarker("OUT");
    val Call = MarkerFactory.getMarker("CALL");
}