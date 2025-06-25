package com.albertrg.firebasecourse.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath

@Composable
fun SignBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {

            val width = size.width
            val height = size.height


            val purplePath = Path().apply {
                moveTo(0f, height * 0.24f)
                lineTo(width, height * 0.5f)
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }

            drawPath(
                path = purplePath,
                color = Color(0xFFFC9304)
            )

            val bigTriangle = RoundedPolygon(
                numVertices = 3,
                radius = size.width / 1.5f,
                centerX = 0f,
                centerY = size.height / 30,
                rounding = CornerRounding(
                    size.minDimension / 20f,
                    smoothing = 0.3f
                )
            )

            val roundedPolygonPath = bigTriangle.toPath().asComposePath()
            drawPath(roundedPolygonPath, color = Color(0x5CFC9304))

            val smallTriangleA = RoundedPolygon(
                numVertices = 3,
                radius = size.width / 1.5f,
                centerX = size.width / 100,
                centerY = size.height / 7,
                rounding = CornerRounding(
                    size.minDimension / 20f,
                    smoothing = 0.3f
                )
            )

            translate(smallTriangleA.centerX, smallTriangleA.centerY) {
                scale(scaleX = -1f, scaleY = 1f) {
                    drawPath(roundedPolygonPath, color = Color(0x5CFC9304))
                }
            }

            val smallTriangleB = RoundedPolygon(
                numVertices = 3,
                radius = size.width / 1.5f,
                centerX = size.width / 100,
                centerY = size.height / 4f,
                rounding = CornerRounding(
                    size.minDimension / 20f,
                    smoothing = 0.3f
                )
            )

            translate(smallTriangleB.centerX, smallTriangleB.centerY) {
                scale(scaleX = -1f, scaleY = 1f) {
                    drawPath(roundedPolygonPath, color = Color(0x6FF51904))
                }
            }

        }
    }

}

@Composable
@Preview(showBackground = true)
fun SignBackgroundPreview() {
    SignBackground()
}