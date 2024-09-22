package com.tian.draw

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * @Author: tian7
 * @Email: 253493510@qq.com
 * @Date: on 2024/9/22: 10: 42
 * @Description: 描述
 */
class DrawingBoard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SurfaceView(context, attrs), SurfaceHolder.Callback{
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val surfaceHolder: SurfaceHolder = holder
    private val path: Path
    private var flag: Boolean = false

    init {
        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        path = Path()
        surfaceHolder?.addCallback(this)
    }

    private fun drawCanvas() {
       Thread{
           while (flag){
               val canvas = surfaceHolder.lockCanvas()
               if(canvas != null) {
                   canvas.drawPath(path, paint)
                   surfaceHolder.unlockCanvasAndPost(canvas)
               }
           }
       }.start()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(event.x, event.y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(event.x, event.y)
            }
        }
        return super.onTouchEvent(event)
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        flag = true
        drawCanvas()
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        flag = false
    }



}