package com.sidharth.lgconnect.util

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.sidharth.lgconnect.domain.model.Marker
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow

object KMLUtils {
    fun createChartKML(type: String): String {
        return """<?xml version="1.0" encoding="UTF-8"?>
        <kml xmlns="http://www.opengis.net/kml/2.2" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:kml="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom">
        <Document>
        <name>Charts Example</name>
        <ScreenOverlay>
        <name><![CDATA[<div style="text-align: center; font-size: 20px; font-weight: bold; vertical-align: middle;">Sample Chart</div>]]></name>
        <description>
        <![CDATA[
        <html>
        <body>${
            when (type) {
                "bar-chart" -> createBarChartKML()
                "line-chart" -> createLineChartKML()
                "gauge-chart" -> createGaugeChartKML()
                "scatter-chart" -> createScatterChartKML()
                "radar-chart" -> createRadarChartKML()
                "pie-chart" -> createPieChartKML()
                else -> ""
            }
        }</body>
        </html>
        ]]></description>
        <overlayXY x="0" y="1" xunits="fraction" yunits="fraction"/>
        <screenXY x="1" y="1" xunits="fraction" yunits="fraction"/>
        <rotationXY x="0" y="0" xunits="fraction" yunits="fraction"/>
        <size x="0" y="0" xunits="fraction" yunits="fraction"/>
        <gx:balloonVisibility>1</gx:balloonVisibility>
        </ScreenOverlay>
        </Document>
        </kml>
        """.trimIndent().trimMargin()
    }

    private fun createBarChartKML(): String =
        """<img src="https://chart.googleapis.com/chart?chs=300x200&cht=bvg&chd=t:10,20,30,40,50&chl=Label+1|Label+2|Label+3|Label+4|Label+5" alt="Bar Chart" />"""

    private fun createLineChartKML(): String =
        """<img src="https://chart.googleapis.com/chart?chs=300x200&cht=lc&chd=t:10,20,30,40,50&chl=Label+1|Label+2|Label+3|Label+4|Label+5" alt="Line Chart" />"""

    private fun createPieChartKML(): String =
        """<img src="https://chart.googleapis.com/chart?chs=300x200&cht=p&chd=t:10,20,30,40,50&chl=Label+1|Label+2|Label+3|Label+4|Label+5" alt="Pie Chart" />"""

    private fun createGaugeChartKML(): String =
        """<img src="https://chart.googleapis.com/chart?chs=300x200&cht=gom&chd=t:70&chl=Gauge" alt="Gauge Chart" />"""

    private fun createScatterChartKML(): String =
        """<img src="https://chart.googleapis.com/chart?chs=300x200&cht=s&chd=t:10,20,30,40,50&chdl=Label+Series&chco=FF0000|00FF00|0000FF&chdlp=b" alt="Scatter Chart" />"""

    private fun createRadarChartKML(): String =
        """<img src="https://chart.googleapis.com/chart?chs=300x200&cht=r&chd=t:10,20,30,40,50&chl=Label+1|Label+2|Label+3|Label+4|Label+5" alt="Radar Chart" />"""

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

    fun lookAt(latLng: LatLng): String {
        return """<LookAt><longitude>${latLng.longitude}</longitude><latitude>${latLng.latitude}</latitude><range>200</range><tilt>0</tilt><heading>79</heading><gx:altitudeMode>relativeToGround</gx:altitudeMode></LookAt>"""
    }

    fun lookAt(camera: CameraPosition): String {
        val zoom =
            156543.03392 * cos(camera.target.latitude * PI / 180) / 2.0.pow(camera.zoom.toDouble())
        return """<LookAt><longitude>${camera.target.longitude}</longitude><latitude>${camera.target.latitude}</latitude><range>${zoom * 1000}</range><tilt>${camera.tilt}</tilt><heading>${camera.bearing}</heading><gx:altitudeMode>relativeToSeaFloor</gx:altitudeMode></LookAt>"""
    }

    fun orbitAround(latLng: LatLng): String {
        return """<gx:duration>3</gx:duration><gx:flyToMode>smooth</gx:flyToMode><LookAt>
        <longitude>${latLng.longitude}</longitude>
        <latitude>${latLng.latitude}</latitude>
        <altitude>0</altitude>
        <heading>79</heading>
        <tilt>60</tilt>
        <range>500</range>
        <gx:altitudeMode>relativeToGround</gx:altitudeMode>
        </LookAt>"""
    }

    fun generateMarkersKml(markers: List<Marker>): String {
        return """<?xml version="1.0" encoding="UTF-8"?>
        <kml xmlns="http://www.opengis.net/kml/2.2" xmlns:gx="http://www.google.com/kml/ext/2.2" xmlns:kml="http://www.opengis.net/kml/2.2" xmlns:atom="http://www.w3.org/2005/Atom">
        ${generateMarker(markers)}
        </kml>
        """.trimIndent()
    }

    private fun generateMarker(markers: List<Marker>): String {
        val kml = StringBuilder()

        kml.append(
            """<Document>
            <name>My Markers</name>
            <Style id="defaultMarker">
                <IconStyle>
                    <Icon>
                        <href>http://maps.google.com/mapfiles/kml/paddle/red-circle.png</href>
                    </Icon>
                </IconStyle>
            </Style>
        """.trimIndent()
        )

        for (marker in markers) {
            kml.append(
                """
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
                                <b>LatLng:</b> ${marker.latLng.latitude},${marker.latLng.longitude}<br>
                            </p>
                        ]]></text>
                    </BalloonStyle>
                </Style>
            </Placemark>
        """.trimIndent()
            )
        }

        kml.append("</Document>")
        return kml.toString()
    }
}