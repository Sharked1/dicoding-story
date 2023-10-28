package com.dicoding.mystoryapp.customui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.mystoryapp.R

class PasswordEditText: AppCompatEditText {
    private val minCharacter = 8
    private val paint = Paint()
    private var isEnough = true
    constructor(context : Context): super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr){
        init()
    }

    private fun init(){
        paint.color = ContextCompat.getColor(context, R.color.red)
        paint.textSize = 28f
        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(input: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val length = input?.length ?: 1
                isEnough = length >= minCharacter
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!isEnough){
            canvas.drawText(ContextCompat.getString(context, R.string.character_minimum), width/2f+90f, height/2f+8f, paint)
        }
    }
}