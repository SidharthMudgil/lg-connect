package com.sidharth.lgconnect.service

import com.sidharth.lgconnect.domain.model.Marker

object KMLService {
    fun createChartKML(type: String): String {
        return when (type) {
            "bar-chart" -> createBarChartKML()
            "line-chart" -> createLineChartKML()
            "gauge-chart" -> createGaugeChartKML()
            "scatter-chart" -> createScatterChartKML()
            "radar-chart" -> createRadarChartKML()
            "pie-chart" -> createPieChartKML()
            else -> ""
        }
    }


    private fun createBarChartKML(): String {
        return ""
    }

    private fun createLineChartKML(): String {
        return ""
    }

    private fun createPieChartKML(): String {
        return ""
    }

    private fun createGaugeChartKML(): String {
        return ""
    }

    private fun createScatterChartKML(): String {
        return ""
    }

    private fun createRadarChartKML(): String {
        return ""
    }


    fun createMarker(marker: Marker): String {
        return """
        <Placemark>
            <name>${marker.title}</name>
            <description>${marker.subtitle}</description>
            <Point>
                <coordinates>${marker.latLng.longitude},${marker.latLng.latitude},0</coordinates>
            </Point>
            <Style>
                <BalloonStyle>
                    <text><![CDATA[
                        <center><h3>${marker.title}</h3></center>
                        <center><p>${marker.subtitle}</p></center>
                    ]]></text>
                </BalloonStyle>
            </Style>
        </Placemark>
    """.trimIndent()
    }

}