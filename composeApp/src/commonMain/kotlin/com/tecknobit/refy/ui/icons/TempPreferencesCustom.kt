package com.tecknobit.refy.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val TempPreferencesCustom: ImageVector
	get() {
		if (_Temp_preferences_custom != null) {
			return _Temp_preferences_custom!!
		}
		_Temp_preferences_custom = ImageVector.Builder(
            name = "TempPreferencesCustom",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
			path(
    			fill = SolidColor(Color.Black),
    			fillAlpha = 1.0f,
    			stroke = null,
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 1.0f,
    			strokeLineCap = StrokeCap.Butt,
    			strokeLineJoin = StrokeJoin.Miter,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(19f, 9f)
				lineToRelative(-1.25f, -2.75f)
				lineTo(15f, 5f)
				lineToRelative(2.75f, -1.25f)
				lineTo(19f, 1f)
				lineToRelative(1.25f, 2.75f)
				lineTo(23f, 5f)
				lineToRelative(-2.75f, 1.25f)
				close()
				moveToRelative(0f, 14f)
				lineToRelative(-1.25f, -2.75f)
				lineTo(15f, 19f)
				lineToRelative(2.75f, -1.25f)
				lineTo(19f, 15f)
				lineToRelative(1.25f, 2.75f)
				lineTo(23f, 19f)
				lineToRelative(-2.75f, 1.25f)
				close()
				moveTo(9f, 20f)
				lineToRelative(-2.5f, -5.5f)
				lineTo(1f, 12f)
				lineToRelative(5.5f, -2.5f)
				lineTo(9f, 4f)
				lineToRelative(2.5f, 5.5f)
				lineTo(17f, 12f)
				lineToRelative(-5.5f, 2.5f)
				close()
				moveToRelative(0f, -4.85f)
				lineTo(10f, 13f)
				lineToRelative(2.15f, -1f)
				lineTo(10f, 11f)
				lineTo(9f, 8.85f)
				lineTo(8f, 11f)
				lineToRelative(-2.15f, 1f)
				lineTo(8f, 13f)
				close()
				moveTo(9f, 12f)
			}
		}.build()
		return _Temp_preferences_custom!!
	}

private var _Temp_preferences_custom: ImageVector? = null
