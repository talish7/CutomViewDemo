package com.tian.customedittext


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat


/**
 * @Author: tian7
 * @Email: 253493510@qq.com
 * @Date: on 2024/7/28: 10: 32
 * @Description: 描述
 */
class EditTextWithClear @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs,defStyleAttr) {


    private var mClearDrawable: Drawable? = null
    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EditTextWithClear,
            0,
            0
        ).apply {
            try {
                val mClearDrawableId = getResourceId(R.styleable.EditTextWithClear_clearIcon, -1)
                if (mClearDrawableId != -1) {
                    mClearDrawable = ContextCompat.getDrawable(context, mClearDrawableId)
                }
            } finally {
                recycle()
            }
        }
    }
    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        toggleIconVisibility()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // 这里假设清除按钮的中心在EditText高度的一半处
        event?.let { e ->
            mClearDrawable?.let {
                if (e.action == MotionEvent.ACTION_UP
                    && e.x > width - it.intrinsicWidth - 20
                    && e.x < width + 20
                    && e.y > height / 2 - it.intrinsicHeight / 2 - 20
                    && e.y < height / 2 + it.intrinsicHeight / 2 + 20
                    ){
                    text?.clear()
                }

            }
        }
        performClick()
        return super.onTouchEvent(event)
    }

    //有些设备只有触摸没有点击，所以这里重写一下点击事件
    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        toggleIconVisibility()
    }

    private fun toggleIconVisibility() {
        val icon =  if (mClearDrawable != null && isFocused) mClearDrawable else null
        setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null)

    }

}