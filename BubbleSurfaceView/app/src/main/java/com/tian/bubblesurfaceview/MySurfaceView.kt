package com.tian.bubblesurfaceview

import android.content.Context
import android.graphics.Color
import android.graphics.DiscretePathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @Author: tian7
 * @Email: 253493510@qq.com
 * @Date: on 2024/8/4: 23: 57
 * @Description: 描述
 */
class MySurfaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SurfaceView(context, attrs) {
    private val colors = arrayOf(Color.RED,Color.BLUE,Color.GREEN,Color.CYAN,Color.YELLOW,Color.MAGENTA,Color.BLACK,Color.WHITE)
    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
        pathEffect = DiscretePathEffect(30f,20f)
    }
    private data class Bubble(var x: Float, var y: Float, var color: Int, var radius: Float)

    private val bubbles = mutableListOf<Bubble>()
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            val x = it.x
            val y = it.y
            val color = colors.random()
            val bubble = Bubble(x,y,color,1f)
            bubbles.add(bubble)
            if (bubbles.size > 30) {
                bubbles.removeAt(0)
            }

        }
        return super.onTouchEvent(event)
    }

    init {
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                if (holder.surface.isValid){
                    val canvas = holder.lockCanvas()
                    canvas.drawColor(Color.BLACK)
                    bubbles.toList().filter { it.radius < 1000f }.forEach {
                        paint.color = it.color
                        canvas.drawCircle(it.x,it.y,it.radius,paint)
                        it.radius += 10f
                    }
                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }
}