package com.harshilpadsala.signatureviewjetpackcompose

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.harshilpadsala.signatureviewjetpackcompose.ext.pxToDp
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object CanvasSize{
    const val Height = 500F
    const val Width = 500F
}


@Composable
fun SignaturePad(context: Context){
    val lines = remember {
        mutableStateListOf<Line>()
    }

    Column {
        DrawingScreen(lines)
        ElevatedButton(onClick = {
            val imageBitmap = drawToBitmap(lines)
            saveBitmapToDownloads(context , imageBitmap.asAndroidBitmap() , "harshil")
        }
        ) {
            Text(text = "I do not know")
        }
    }
}

fun CanvasDrawScope.asBitmap(size: Size, onDraw: DrawScope.() -> Unit): ImageBitmap {
    val bitmap = ImageBitmap(size.width.toInt(), size.height.toInt())
    draw(Density(1f), androidx.compose.ui.unit.LayoutDirection.Ltr,
        androidx.compose.ui.graphics.Canvas(bitmap), size) { onDraw() }
    return bitmap
}

fun drawToBitmap(lines: SnapshotStateList<Line>) : ImageBitmap{
    val drawScope = CanvasDrawScope()
    val size = Size(CanvasSize.Width, CanvasSize.Height)
    val bitmap = drawScope.asBitmap(size) {
        lines.forEach { line ->
            drawLine(
                color = line.color,
                start = line.start,
                end = line.end,
                strokeWidth = line.strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
    return bitmap
}

fun saveBitmapToDownloads(context: Context, bitmap: Bitmap, fileName: String) {
    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

    val file = File(downloadsDir, fileName)

    try {
        val outputStream: OutputStream = FileOutputStream(file)

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

        outputStream.flush()
        outputStream.close()

        MediaStore.Images.Media.insertImage(context.contentResolver, file.absolutePath, fileName, null)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Composable
fun DrawingScreen(lines : SnapshotStateList<Line>) {
    Canvas(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .pointerInput(Unit){
                detectTapGestures {
                    val line = Line(
                        start = it,
                        end = it
                    )
                    lines.add(line)
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val line = Line(
                        start = change.position - dragAmount,
                        end = change.position
                    )
                    lines.add(line)
                }
            }
    ) {
        lines.forEach { line ->
            drawLine(
                color = line.color,
                start = line.start,
                end = line.end,
                strokeWidth = line.strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

data class Line(
    val start: Offset,
    val end: Offset,
    val color: Color = Color.Red,
    val strokeWidth: Dp = 1.dp
)