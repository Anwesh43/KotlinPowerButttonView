package ui.anwesome.com.powerbuttonview

/**
 * Created by anweshmishra on 23/01/18.
 */
import android.app.Activity
import android.content.*
import android.graphics.*
import android.view.*
class PowerButtonView(ctx:Context):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = Renderer(this)
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
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
        val state = PowerButtonState()
        fun draw(canvas:Canvas,paint:Paint) {
            paint.color = Color.WHITE
            canvas.save()
            canvas.translate(x,y)
            state.executeCb({
                canvas.rotate(30f*it)
            },1)
            for(i in 0..1) {
                canvas.save()
                val path = Path()
                state.executeCb({
                    canvas.rotate(i*180f*it)
                },0)
                path.moveTo(0f,0f)
                path.lineTo(0f,-w)
                path.lineTo(-w/2,0f)
                canvas.drawPath(path,paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun update(stopcb:(Float)->Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb:()->Unit) {
            state.startUpdating(startcb)
        }
    }
    data class PowerButtonState(var j:Int = 0,var dir:Int = 1,var scaleDir:Float = 0f,var prevScale:Float = 0f) {
        val scales:Array<Float> = arrayOf(0f,0f)
        fun update(stopcb:(Float)->Unit) {
            scales[j] += scaleDir*0.1f
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
                startcb()
            }
        }
        fun executeCb(cb:(Float)->Unit,i:Int) {
            if(i>=0 && i< scales.size) {
                cb(scales[i])
            }
        }
    }
    data class Renderer(var view:PowerButtonView,var time:Int = 0) {
        val animator = Animator(view)
        var powerButton:PowerButton?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                val w = canvas.width.toFloat()
                val h = canvas.height.toFloat()
                powerButton = PowerButton(w/2,h/2,h/3)
            }
            canvas.drawColor(Color.parseColor("#212121"))
            powerButton?.draw(canvas,paint)
            time++
            animator.animate {
                powerButton?.update {
                    animator.stop()
                }
            }
        }
        fun handleTap() {
            powerButton?.startUpdating {
                animator.start()
            }
        }
    }
    companion object {
        fun create(activity:Activity):PowerButtonView {
            val view = PowerButtonView(activity)
            val size = DimensionController.getDimensions(activity)
            activity.addContentView(view,ViewGroup.LayoutParams(size.x/2,size.x/2))
            return view
        }
    }
}