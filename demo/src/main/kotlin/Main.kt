import com.github.jacklt.yeelight.Yeelight
import com.github.jacklt.yeelight.startFlowPolice
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("Start search...")
    val devices = Yeelight.findDevices()
    println("Found ${devices.size} devices:\n${devices.joinToString("\n")}")
    devices.forEach { yeelight ->
        yeelight.startFlowPolice()
    }
    println("Finish!")
}