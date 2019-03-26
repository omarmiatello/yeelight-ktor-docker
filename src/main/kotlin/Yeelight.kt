package com.github.jacklt.yeelight

import com.google.gson.Gson
import io.ktor.network.selector.ActorSelectorManager
import io.ktor.network.sockets.Datagram
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.util.cio.write
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.io.readUTF8Line
import kotlinx.coroutines.launch
import kotlinx.io.core.ByteReadPacket
import java.net.InetSocketAddress

data class Yeelight(val info: Map<String, String>, val ip: String, val port: Int) {

    private suspend fun send(cmd: Cmd) = aSocket(ioSelector).tcp().connect(ip, port).use { s ->
        s.openWriteChannel(autoFlush = true).write("$cmd\r\n")
        s.openReadChannel().readUTF8Line()
    }.also {
        println("$ip --> $cmd\n${"".padEnd(ip.length, ' ')} <-- $it")
    }

    suspend fun easyFlow(flowTuples: List<String>, endAction: FlowEndAction, repeat: Int = 1) =
        startColorFlow(repeat * flowTuples.size, endAction, flowTuples.joinToString(","))

    // Docs: https://www.yeelight.com/download/Yeelight_Inter-Operation_Spec.pdf

    suspend fun getProperties(propertiesNames: List<String>) =
        send(Cmd("get_prop", propertiesNames))

    suspend fun setCurrentAsDefault() =
        send(Cmd("set_default"))

    suspend fun setPower(isOn: Boolean = true, effect: SpeedEffect = SpeedEffect.smooth, duration: Long = 500) =
        send(Cmd("set_power", listOf(if (isOn) "on" else "off", effect, duration)))

    suspend fun toggle() =
        send(Cmd("toggle"))

    suspend fun setBrightness(brightness: Int, effect: SpeedEffect = SpeedEffect.smooth, duration: Long = 500) =
        send(Cmd("set_bright", listOf(brightness.coerceIn(1..100), effect, duration)))

    suspend fun startColorFlow(count: Int, action: FlowEndAction, flowExpression: String) =
        send(Cmd("start_cf", listOf(count, action.id, flowExpression)))

    suspend fun stopColorFlow() =
        send(Cmd("stop_cf"))

    suspend fun setScene() =
        send(Cmd("set_scene", TODO("Missing params")))

    suspend fun cronAdd() =
        send(Cmd("cron_add", TODO("Missing params")))

    suspend fun cronGet() =
        send(Cmd("cron_get", TODO("Missing params")))

    suspend fun cronDel() =
        send(Cmd("cron_del", TODO("Missing params")))

    suspend fun setColorTemperature(color: Int, effect: SpeedEffect = SpeedEffect.smooth, duration: Long = 500) =
        send(Cmd("set_ct_abx", listOf(color.coerceIn(1700..6500), effect, duration)))

    suspend fun setColorRgb(color: Int, effect: SpeedEffect = SpeedEffect.smooth, duration: Long = 500) =
        send(Cmd("set_rgb", listOf(color.coerceIn(0..0xffffff), effect, duration)))


    private data class Cmd(val method: String, val params: List<Any> = emptyList(), val id: Int = 1) {
        override fun toString() = Gson().toJson(this)
    }

    enum class SpeedEffect { sudden, smooth }

    enum class FlowEndAction(val id: Int) { recover(0), stay(1), off(2) }

    enum class FlowMode(val id: Int) { colorRgb(1), colorTemperature(2), sleep(7) }

    companion object {
        val ioSelector = ActorSelectorManager(Dispatchers.IO)
        suspend fun findDevices(searchTime: Long = 1000): List<Yeelight> {
            return coroutineScope {
                mutableListOf<Yeelight>().also { list ->
                    aSocket(ioSelector).udp().bind().use { udp ->
                        udp.send(
                            Datagram(
                                ByteReadPacket("M-SEARCH * HTTP/1.1\r\nMAN:\"ssdp:discover\"\r\nST:wifi_bulb\r\n".toByteArray()),
                                InetSocketAddress("239.255.255.250", 1982)
                            )
                        )
                        launch {
                            udp.incoming.consumeEach {
                                val info: Map<String, String> = it.packet.readText().lines()
                                    .map { it.split(": ", limit = 2) }
                                    .filter { it.size == 2 && it[1].isNotEmpty() }
                                    .map { it[0] to it[1] }.toMap()
                                val address = info.getValue("Location").split("//")[1]
                                    .split(":").let { it[0] to it[1].toInt() }
                                list += Yeelight(info, address.first, address.second)
                            }
                        }
                        delay(searchTime)
                    }
                }.distinct()
            }
        }
    }
}