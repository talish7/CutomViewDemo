package com.tian.customdraw

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.withRotation
import androidx.core.graphics.withTranslation
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Math.PI
import kotlin.io.path.Path
import kotlin.io.path.moveTo
import kotlin.math.cos
import kotlin.math.sin

/**
 * @Author: tian7
 * @Email: 253493510@qq.com
 * @Date: on 2024/8/3: 14: 07
 * @Description: 描述
 *
 */
class MyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr),LifecycleObserver {

    private var mWidth = 0f
    private var mHeight = 0f
    private var mRadius = 0f
    private var mAngle = 10f
    private var sineWavePath = android.graphics.Path()

    private val solidLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
        color = ContextCompat.getColor(context, R.color.white)
    }

    private val textPaint = Paint().apply {
        textSize = 50f
        typeface = Typeface.DEFAULT_BOLD
        color = ContextCompat.getColor(context, R.color.white)
    }

    private val dashLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
        color = ContextCompat.getColor(context, R.color.yellow)
    }

    private val fillLinePaint = Paint().apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.white)
    }

    private var rotatingJob: Job? = null


    //view尺寸最终确定回调
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
        mRadius = if (w > h / 2)  h / 4f else w / 2f
        mRadius -= 20f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        dLog("onMeasure")
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        dLog("onLayout")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        dLog("onDraw")
        drawAxes(canvas)
        drawText(canvas)
        drawDashCircle(canvas)
        drawVector(canvas)
        drawProjection(canvas)
        drawSineWave(canvas)
    }

    private fun drawAxes(canvas: Canvas){
        canvas.withTranslation(mWidth / 2f, mHeight / 2f) {
            drawLine(-mWidth / 2f, 0f, mWidth / 2f, 0f, solidLinePaint)
            drawLine(0f, -mHeight / 2f, 0f, mHeight / 2f, solidLinePaint)
        }
        canvas.withTranslation(mWidth / 2f, mHeight * 0.75f) {
            drawLine(-mWidth / 2f, 0f, mWidth / 2f, 0f, solidLinePaint)
        }

    }

    private fun drawText(canvas: Canvas){
         canvas.apply {
             drawRect(100f,100f,600f,250f,solidLinePaint)
             drawText("指数函数与旋转矢量",120f,195f,textPaint)
         }
    }

    private fun drawDashCircle(canvas: Canvas){
        canvas.withTranslation(mWidth / 2f, mHeight * 0.75f) {
            drawCircle(0f, 0f, mRadius, dashLinePaint)
        }
    }

    private fun drawVector(canvas: Canvas){
        canvas.withTranslation(mWidth / 2f, mHeight * 0.75f) {
            withRotation(-mAngle) {
                drawLine(0f, 0f, mRadius , 0f, solidLinePaint)
            }
        }
    }

    private fun drawProjection(canvas: Canvas){
        canvas.withTranslation(mWidth/2,mHeight/2) {
            drawCircle(mRadius * cos(mAngle.toRadian()),0f,20f,fillLinePaint)
        }
        canvas.withTranslation(mWidth/2,mHeight/4*3) {
            drawCircle(mRadius * cos(mAngle.toRadian()),0f,20f,fillLinePaint)
        }
        canvas.withTranslation(mWidth/2,mHeight/4*3) {
            val x = mRadius * cos(mAngle.toRadian())
            val y = mRadius * sin(mAngle.toRadian())
            withTranslation(x,-y) {
                drawLine(0f, 0f, 0f , y, solidLinePaint)
                drawLine(0f,0f,0f,-(mHeight/4-y),dashLinePaint)
            }
        }
    }

    private fun drawSineWave(canvas: Canvas){
        canvas.withTranslation(mWidth/2,mHeight/2) {
            val count = 50
            val dy = mHeight / 2 /count
            sineWavePath.reset()
            sineWavePath.moveTo(mRadius * cos(mAngle.toRadian()), 0f)
            repeat(count){
                val x = mRadius * cos(it * -0.15 + mAngle.toRadian()).toFloat()
                val y = it * -dy
                sineWavePath.quadTo(x,y,x,y)
            }
            drawPath(sineWavePath,solidLinePaint)
            drawTextOnPath("hello",sineWavePath,1000f,0f,textPaint)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startRotation(){
        rotatingJob = CoroutineScope(Dispatchers.Main).launch {
            while (true){
                mAngle += 5f
                invalidate()
                delay(50)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun stopRotation(){
        rotatingJob?.cancel()
    }

    private fun Float.toRadian(): Float{
        return (this / 180 * PI).toFloat()
    }
    fun dLog(msg: String){
        Log.d("tian",msg)
    }
}
