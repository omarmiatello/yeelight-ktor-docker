package com.github.jacklt

import com.github.jacklt.yeelight.Yeelight
import com.github.jacklt.yeelight.startFlowPolice
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


var devices: List<Yeelight> = emptyList()

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)
    routing {
        get("/") {
            devices = Yeelight.findDevices()
            call.respondText("Ok ${devices.size}!")
        }
        get("/toggle") {
            if (devices.isEmpty()) devices = Yeelight.findDevices()
            devices.forEach { it.toggle() }
            call.respondText("toggle ${devices.size}!")
        }
        get("/police") {
            if (devices.isEmpty()) devices = Yeelight.findDevices()
            devices.forEach { it.startFlowPolice() }
            call.respondText("police ${devices.size}!")
        }
    }
}

