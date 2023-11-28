package com.dicoding.mystoryapp.customui

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.mystoryapp.R

class EmailEditText : AppCompatEditText {
    constructor(context: Context): super(context){
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr){
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (p0.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(p0).matches()){

                }
                else {
                    error = resources.getString(R.string.email_error)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }
}