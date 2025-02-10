package com.tecknobit.refy.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Preview: ImageVector
    get() {
        if (_Preview != null) {
            return _Preview!!
        }
        _Preview = ImageVector.Builder(
            name = "Preview",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 16f,
            viewportHeight = 16f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(2f, 2f)
                horizontalLineToRelative(12f)
                lineToRelative(1f, 1f)
                verticalLineToRelative(10f)
                lineToRelative(-1f, 1f)
                horizontalLineTo(2f)
                lineToRelative(-1f, -1f)
                verticalLineTo(3f)
                lineToRelative(1f, -1f)
                close()
                moveToRelative(0f, 11f)
                horizontalLineToRelative(12f)
                verticalLineTo(3f)
                horizontalLineTo(2f)
                verticalLineToRelative(10f)
                close()
                moveToRelative(11f, -9f)
                horizontalLineTo(3f)
                verticalLineToRelative(3f)
                horizontalLineToRelative(10f)
                verticalLineTo(4f)
                close()
                moveToRelative(-1f, 2f)
                horizontalLineTo(4f)
                verticalLineTo(5f)
                horizontalLineToRelative(8f)
                verticalLineToRelative(1f)
                close()
                moveToRelative(-3f, 6f)
                horizontalLineToRelative(4f)
                verticalLineTo(8f)
                horizontalLineTo(9f)
                verticalLineToRelative(4f)
                close()
                moveToRelative(1f, -3f)
                horizontalLineToRelative(2f)
                verticalLineToRelative(2f)
                horizontalLineToRelative(-2f)
                verticalLineTo(9f)
                close()
                moveTo(7f, 8f)
                horizontalLineTo(3f)
                verticalLineToRelative(1f)
                horizontalLineToRelative(4f)
                verticalLineTo(8f)
                close()
                moveToRelative(-4f, 3f)
                horizontalLineToRelative(4f)
                verticalLineToRelative(1f)
                horizontalLineTo(3f)
                verticalLineToRelative(-1f)
                close()
            }
        }.build()
        return _Preview!!
    }

private var _Preview: ImageVector? = null
