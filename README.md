# yeelight-kt [Deprecated] new project https://github.com/omarmiatello/yeelight-kt
Control your Xiaomi Yeelight lamp from Kotlin. Use ktor library for network.

[![](https://jitpack.io/v/jacklt/yeelight-kt.svg)](https://jitpack.io/#jacklt/yeelight-kt)

## Setup

Add this in your root `build.gradle` file:
```gradle
repositories {
    // ...
    maven { url "https://jitpack.io" }
}
```

Grab via Gradle (v4 or later):
```groovy
implementation 'com.github.jacklt:yeelight-kt:1.0.0'
```

## Examples: How to use

### Sample

**[build.gradle](demo/build.gradle)**

**[Main.kt](demo/src/main/kotlin/Main.kt)**

    fun main() = runBlocking {
        println("Start search...")
        val devices = Yeelight.findDevices()
        println("Found ${devices.size} devices:\n${devices.joinToString("\n")}")
        devices.forEach { yeelight ->
            yeelight.startFlowPolice()
        }
        println("Finish!")
    }

**Result**:

    Start search...
    Found 2 devices:
    Yeelight(info={Cache-Control=max-age=3600, Location=yeelight://192.168.85.186:55443, Server=POSIX UPnP/1.0 YGLC/1, id=0x00000000031b0f7c, model=color, fw_ver=70, support=get_prop set_default set_power toggle set_bright start_cf stop_cf set_scene cron_add cron_get cron_del set_ct_abx set_rgb set_hsv set_adjust adjust_bright adjust_ct adjust_color set_music set_name, power=off, bright=1, color_mode=1, ct=4000, rgb=16750848, hue=36, sat=100}, ip=192.168.85.186, port=55443)
    Yeelight(info={Cache-Control=max-age=3600, Location=yeelight://192.168.85.25:55443, Server=POSIX UPnP/1.0 YGLC/1, id=0x00000000047e92c2, model=bslamp1, fw_ver=169, support=get_prop set_default set_power toggle set_bright start_cf stop_cf set_scene cron_add cron_get cron_del set_ct_abx set_rgb set_hsv set_adjust adjust_bright adjust_ct adjust_color set_music set_name, power=off, bright=1, color_mode=1, ct=4000, rgb=16750592, hue=36, sat=100}, ip=192.168.85.25, port=55443)
    192.168.85.186 --> {"method":"start_cf","params":[8,0,"500,1,16711680,100,200,7,0,0,500,1,255,100,200,7,0,0"],"id":1}
                   <-- {"id":1, "result":["ok"]}
    192.168.85.25 --> {"method":"start_cf","params":[8,0,"500,1,16711680,100,200,7,0,0,500,1,255,100,200,7,0,0"],"id":1}
                  <-- {"id":1, "result":["ok"]}
    Finish!


### Web server with [Ktor](https://ktor.io)

**[build.gradle](demo-ktor-docker/build.gradle)**

**[README.md](demo-ktor-docker/README.md)** (for Docker)

You can start a local web server (`./gradlew run`) or use Docker for:

Find devices
```
http://localhost:8080
```

Switch on/off devices (+ find devices if needed)
```
http://localhost:8080/toggle
```

Start "Police flow" (+ find devices if needed)
```
http://localhost:8080/police
```
