package com.harshilpadsala.signatureviewjetpackcompose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.dp

//Only kept for reference

object Action{
    const val Idle = 0
    const val Down = 1
    const val Move = 2
    const val Up = 3
}

@Composable
fun DrawInCanvas(modifier: Modifier, path : MutableState<Path>){
    var motionEvent by remember { mutableIntStateOf(Action.Idle) }
    var currentOffset by remember {
        mutableStateOf(Offset.Unspecified)
    }

    val drawModifier = modifier
        .background(Color.Black)
        .fillMaxSize()
        .pointerInput(Unit) {
            awaitEachGesture {
                awaitFirstDown().also {
                    motionEvent = Action.Down
                    currentOffset = it.position
                }

                do {
                    val event: PointerEvent = awaitPointerEvent()
                    event.changes.forEachIndexed { index: Int, pointerInputChange: PointerInputChange ->
                        if (pointerInputChange.positionChange() != Offset.Unspecified) pointerInputChange.consume()
                    }
                    motionEvent = Action.Move
                    currentOffset = event.changes.first().position
                }
                while (event.changes.any { it.pressed })
            }
        }

    Canvas(modifier = drawModifier) {

        when (motionEvent) {

            Action.Down -> {
                path.value.moveTo(currentOffset.x, currentOffset.y)
            }

            Action.Move -> {

                if (currentOffset != Offset.Unspecified) {
                    path.value.lineTo(currentOffset.x, currentOffset.y)
                }

                motionEvent = Action.Idle

            }

            Action.Up -> {

                path.value.lineTo(currentOffset.x, currentOffset.y)
                motionEvent = Action.Idle
            }

            else -> Unit
        }

        drawPath(
            color = Color.White,
            path = path.value,
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}