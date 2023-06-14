package com.exampl.resizingbitmap

import android.app.Activity
import android.content.Context
import android.hardware.display.DisplayManager
import android.view.Display
import androidx.core.content.getSystemService

object ScreenUtils {
    private const val defaultScreenHeight = 700
    private const val defaultScreenWidth = 350
    fun Activity?.getScreenWidth(): Int {
        this?.let {
            try {
                val defaultDisplay = it.getSystemService<DisplayManager>()?.getDisplay(Display.DEFAULT_DISPLAY)
                defaultDisplay?.let { display ->
                    val displayContext = it.createDisplayContext(display)
                    return displayContext.resources.displayMetrics.widthPixels
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        return defaultScreenWidth
    }

    fun Context?.getScreenWidth(): Int {
        this?.let {
            try {
                val defaultDisplay = it.getSystemService<DisplayManager>()?.getDisplay(Display.DEFAULT_DISPLAY)
                defaultDisplay?.let { display ->
                    val displayContext = it.createDisplayContext(display)
                    return displayContext.resources.displayMetrics.widthPixels
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        return defaultScreenWidth
    }

    fun Activity?.getScreenHeight(): Int {
        this?.let {
            try {
                val defaultDisplay = it.getSystemService<DisplayManager>()?.getDisplay(Display.DEFAULT_DISPLAY)
                defaultDisplay?.let { display ->
                    val displayContext = it.createDisplayContext(display)
                    return displayContext.resources.displayMetrics.heightPixels
                }
            } catch (ex: Exception) {
                ex.printStackTrace()

            }
        }
        return defaultScreenHeight
    }


}