package com.sidharth.lgconnect

enum class ChartType(
    val type: String,

) {
    BAR("bar-chart"),
    LINE("line-chart"),
    SCATTER("scatter-chart"),
    PIE("pie-chart"),
    GAUGE("gauge-chart"),
    RADAR("radar-chart")
}