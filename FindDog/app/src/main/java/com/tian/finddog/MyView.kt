package com.tian.finddog

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import android.graphics.Path
import kotlin.random.Random

/**
 * @Author: tian7
 * @Email: 253493510@qq.com
 * @Date: on 2024/8/4: 22: 05
 * @Description: 描述
 */
class MyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val dogBitMap = ContextCompat.getDrawable(context, R.drawable.dog)?.toBitmap(300,300)
    private var dogX = 0f
    private var dogY = 0f
    private val path = Path()
    private fun randomPosition(){
        dogX = Random.nextInt(width).toFloat()
        dogY = Random.nextInt(height).toFloat()
    }
    private val paint = Paint()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.apply {
            dogBitMap?.let {
                canvas.drawBitmap(it, dogX, dogY, null)
            }
            canvas.drawPath(path, paint)
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.apply {
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    randomPosition()
                    path.reset()
                    path.addRect(0f, 0f, width.toFloat(), height.toFloat(), Path.Direction.CW)
                    path.addCircle(x, y, 300f, Path.Direction.CCW)
                    invalidate()
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    path.reset()
                    path.addRect(0f, 0f, width.toFloat(), height.toFloat(), Path.Direction.CW)
                    path.addCircle(x, y, 300f, Path.Direction.CCW)
                    invalidate()
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    path.reset()
                    invalidate()
                    return true
                }
            }
        }
        return false
    }


    override fun performClick(): Boolean {
        return super.performClick()
    }
}