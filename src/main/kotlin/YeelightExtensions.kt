package com.github.jacklt.yeelight

fun flowTuple(duration: Long, mode: Yeelight.FlowMode, value: Int, brightness: Int) =
    "$duration,${mode.id},$value,$brightness"

suspend fun Yeelight.startFlowGreen(endAction: Yeelight.FlowEndAction = Yeelight.FlowEndAction.stay) = easyFlow(
    flowTuples = listOf(
        flowTuple(2000, Yeelight.FlowMode.colorRgb, 0x00ff00, 100),
        flowTuple(1000, Yeelight.FlowMode.colorRgb, 0x00ff00, 30)
    ),
    endAction = endAction
)

suspend fun Yeelight.startFlowRed(endAction: Yeelight.FlowEndAction = Yeelight.FlowEndAction.stay) = easyFlow(
    flowTuples = listOf(
        flowTuple(2000, Yeelight.FlowMode.colorRgb, 0xff0000, 100),
        flowTuple(1000, Yeelight.FlowMode.colorRgb, 0xff0000, 30)
    ),
    endAction = endAction
)

suspend fun Yeelight.startFlowPolice(endAction: Yeelight.FlowEndAction = Yeelight.FlowEndAction.recover) = easyFlow(
    flowTuples = listOf(
        flowTuple(500, Yeelight.FlowMode.colorRgb, 0xff0000, 100),
        flowTuple(200, Yeelight.FlowMode.sleep, 0, 0),
        flowTuple(500, Yeelight.FlowMode.colorRgb, 0x0000ff, 100),
        flowTuple(200, Yeelight.FlowMode.sleep, 0, 0)
    ),
    endAction = endAction,
    repeat = 2
)

suspend fun Yeelight.startFlowSun(endAction: Yeelight.FlowEndAction = Yeelight.FlowEndAction.recover) = easyFlow(
    flowTuples = listOf(
        flowTuple(2000, Yeelight.FlowMode.colorRgb, 0xff0000, 50),
        flowTuple(2000, Yeelight.FlowMode.colorRgb, 0xff0000, 10),
        flowTuple(1000, Yeelight.FlowMode.sleep, 0, 0),
        flowTuple(2000, Yeelight.FlowMode.colorRgb, 0xffff00, 50),
        flowTuple(2000, Yeelight.FlowMode.colorRgb, 0xffff00, 10),
        flowTuple(1000, Yeelight.FlowMode.sleep, 0, 0),
        flowTuple(2000, Yeelight.FlowMode.colorRgb, 0x00ff00, 50),
        flowTuple(1000, Yeelight.FlowMode.sleep, 0, 0),
        flowTuple(2000, Yeelight.FlowMode.colorRgb, 0x00ffff, 50),
        flowTuple(1000, Yeelight.FlowMode.sleep, 0, 0),
        flowTuple(2000, Yeelight.FlowMode.colorRgb, 0x0000ff, 50),
        flowTuple(1000, Yeelight.FlowMode.sleep, 0, 0),
        flowTuple(2000, Yeelight.FlowMode.colorRgb, 0xff00ff, 50),
        flowTuple(1000, Yeelight.FlowMode.sleep, 0, 0)
    ),
    endAction = endAction,
    repeat = 2
)