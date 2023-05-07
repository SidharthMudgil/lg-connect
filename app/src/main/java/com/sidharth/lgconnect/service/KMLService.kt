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

    fun createLogo(url: String): String {
        return """
        <?xml version="1.0" encoding="UTF-8"?>
        <kml xmlns="http://www.opengis.net/kml/2.2" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:kml="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom">
            <Document id ="logo">
                <name>LG Connect</name>
                <Folder>
                    <name>Logos</name>
                    <ScreenOverlay>
                    <name>Logo</name>
                    <Icon><href>${url}</href> </Icon>
                    <overlayXY x="0" y="1" xunits="fraction" yunits="fraction"/>
                    <screenXY x="0.02" y="0.95" xunits="fraction" yunits="fraction"/>
                    <rotationXY x="0" y="0" xunits="fraction" yunits="fraction"/>
                    <size x="0.6" y="0.4" xunits="fraction" yunits="fraction"/>
                    </ScreenOverlay>
                </Folder>
            </Document>
        </kml>""".trimIndent()
    }

    fun createMarker(marker: Marker): String {
        return """
        <?xml version="1.0" encoding="UTF-8"?>
        <kml xmlns="http://www.opengis.net/kml/2.2">
            <Document>
                <name>My Markers</name>
                <Style id="defaultMarker">
                  <IconStyle>
                    <Icon>
                      <href>http://maps.google.com/mapfiles/kml/paddle/red-circle.png</href>
                    </Icon>
                  </IconStyle>
                </Style>
        
                <Placemark>
                    <styleUrl>#defaultMarker</styleUrl>
                    <name>${marker.title}</name>
                    <Point>
                        <coordinates>${marker.latLng.longitude},${marker.latLng.latitude},0</coordinates>
                    </Point>
                    <Style>
                        <BalloonStyle>
                            <text><![CDATA[
                            <center><h3>${marker.title}</h3><hr></center>
                            <p>
                                <b>Address:</b> ${marker.subtitle}<br>
                                <b>LatLng:</b> ${marker.latLng}<br>
                            </p>
                            ]]></text>
                        </BalloonStyle>
                    </Style>
                </Placemark>
            </Document>
        </kml>""".trimIndent()
    }
}