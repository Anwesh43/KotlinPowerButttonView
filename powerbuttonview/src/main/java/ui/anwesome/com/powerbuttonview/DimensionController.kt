package ui.anwesome.com.powerbuttonview

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.hardware.display.DisplayManager

/**
 * Created by anweshmishra on 23/01/18.
 */
class DimensionController {
    companion object {
        fun getDimensions(activity:Activity):Point {
            val size = Point()
            val manager = activity.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
            val display = manager.getDisplay(0)
            display?.getRealSize(size)
            return size
        }
    }
}