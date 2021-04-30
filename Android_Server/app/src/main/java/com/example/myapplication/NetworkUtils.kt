package com.example.myapplication

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import java.math.BigInteger
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.UnknownHostException
import java.nio.ByteOrder
import java.util.*

fun getIPAddress(useIPv4: Boolean): String? {
    try {
        val interfaces: List<NetworkInterface> = Collections.list(NetworkInterface.getNetworkInterfaces())
        for (intf in interfaces) {
            val addrs: List<InetAddress> = Collections.list(intf.getInetAddresses())
            for (addr in addrs) {
                if (!addr.isLoopbackAddress()) {
                    val sAddr: String = addr.getHostAddress()
                    //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                    val isIPv4 = sAddr.indexOf(':') < 0
                    if (useIPv4) {
                        if (isIPv4) return sAddr
                    } else {
                        if (!isIPv4) {
                            val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                            return if (delim < 0) sAddr.toUpperCase() else sAddr.substring(0, delim).toUpperCase()
                        }
                    }
                }
            }
        }
    } catch (ignored: Exception) {
    } // for now eat exceptions
    return ""
}

fun wifiIpAddress(context: Context): String? {
    val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    var ipAddress = wifiManager.connectionInfo.ipAddress

    // Convert little-endian to big-endianif needed
    if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
        ipAddress = Integer.reverseBytes(ipAddress)
    }
    val ipByteArray: ByteArray = BigInteger.valueOf(ipAddress.toLong()).toByteArray()
    val ipAddressString: String?
    ipAddressString = try {
        InetAddress.getByAddress(ipByteArray).hostAddress
    } catch (ex: UnknownHostException) {
        Log.e("WIFIIP", "Unable to get host address.")
        null
    }
    return ipAddressString
}