package com.sidharth.lgconnect.util

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.sidharth.lgconnect.domain.model.Marker
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow

object KMLUtils {
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

    fun screenOverlayImage(): String =
        """<kml xmlns="http://www.opengis.net/kml/2.2" 
            xmlns:atom="http://www.w3.org/2005/Atom"
            xmlns:gx="http://www.google.com/kml/ext/2.2">
            <Document>
            <name>LG Motion</name>
            <Folder>
            <name>Logo</name>
            <ScreenOverlay>
            <name>Logo</name>
            <Icon>
            <href>https://raw.githubusercontent.com/SidharthMudgil/lg-connect/b6d8484f26ef84548c7da03cfb047f95fab36317/logo.png</href>
            </Icon>
            <overlayXY x="0" y="1" xunits="fraction" yunits="fraction"/>
            <screenXY x="0.02" y="0.95" xunits="fraction" yunits="fraction"/>
            <rotationXY x="0" y="0" xunits="fraction" yunits="fraction"/>
            <size x="0.15" y="0.3" xunits="fraction" yunits="fraction"/>
            </ScreenOverlay>
            </Folder>
            </Document>
            </kml>""".trimMargin()

    fun lookAt(camera: CameraPosition): String {
        val zoom =
            156543.03392 * cos(camera.target.latitude * PI / 180) / 2.0.pow(camera.zoom.toDouble())
        return """<LookAt><longitude>${camera.target.longitude}</longitude><latitude>${camera.target.latitude}</latitude><range>${zoom * 1000}</range><tilt>${camera.tilt}</tilt><heading>${camera.bearing}</heading><gx:altitudeMode>relativeToGround</gx:altitudeMode></LookAt>"""
    }

    fun lookAt(latLng: LatLng): String {
        val zoom = 156543.03392 * cos(latLng.latitude * PI / 180) / 2.0.pow(18)
        return """<LookAt><longitude>${latLng.longitude}</longitude><latitude>${latLng.latitude}</latitude><range>${zoom * 1000}</range><tilt>${1}</tilt><heading>${0}</heading><gx:altitudeMode>relativeToGround</gx:altitudeMode></LookAt>"""
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