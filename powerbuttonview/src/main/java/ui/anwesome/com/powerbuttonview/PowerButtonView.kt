package ui.anwesome.com.powerbuttonview

/**
 * Created by anweshmishra on 23/01/18.
 */
import android.content.*
import android.graphics.*
import android.view.*
class PowerButtonView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class Animator(var view:PowerButtonView,var animated:Boolean = false) {
        fun animate(updatecb:()->Unit) {
            if(animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex:Exception) {

                }
            }
        }
        fun start() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
        fun stop() {
            if(animated) {
                animated = false
            }
        }
    }
    data class PowerButton(var x:Float,var y:Float,var w:Float) {
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.save()
            canvas.translate(x,y)
            canvas.rotate(45f)
            for(i in 0..1) {
                canvas.save()
                canvas.rotate(i*180f)
                val path = Path()
                path.moveTo(0f,0f)
                path.lineTo(0f,w)
                path.lineTo(-w/2,0f)
                canvas.drawPath(path,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun update(stopcb:(Float)->Unit) {

        }
        fun startUpdating(startcb:()->Unit) {

        }
    }
    data class PowerButtonState(var j:Int = 0,var dir:Int = 1,var scaleDir:Float = 0f,var prevScale:Float = 0f) {
        val scales:Array<Float> = arrayOf(0f,0f)
        fun update(stopcb:(Float)->Unit) {
            scales[j] += scaleDir
            if(Math.abs(scales[j]-prevScale) > 1) {
                scales[j] = prevScale + scaleDir
                j += dir
                if(j == scales.size || j == -1) {
                    scaleDir = 0f
                    dir *= -1
                    j+=dir
                    prevScale = scales[j]
                    stopcb(prevScale)
                }
            }
        }
        fun startUpdating(startcb: () -> Unit) {
            if(scaleDir == 0f) {
                scaleDir = 1-2*prevScale
            }
        }
        fun executeCb(cb:(Float)->Unit) {
            if(j>=0 && j< scales.size) {
                cb(scales[j])
            }
        }
    }
}