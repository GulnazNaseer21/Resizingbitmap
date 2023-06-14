package com.exampl.resizingbitmap

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.exampl.resizingbitmap.ScreenUtils.getScreenWidth

class CustomViewOld(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var bitmap: Bitmap? = null
    private var scale = 1.0f
    private val sharedPreferences: SharedPreferences
    private var progresswidth=0
    private var lastTouchX: Float = 0f
    private var lastTouchY: Float = 0f
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : this(context, attrs)
    init {
        sharedPreferences = context.getSharedPreferences("CustomViewPrefs", Context.MODE_PRIVATE)
        scale = sharedPreferences.getFloat("scale", 1.0f)
        progresswidth = sharedPreferences.getInt("bitmapWidth", 0)
    }

    fun setBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
        invalidate()
    }

    fun setScale(scale: Float) {
        this.scale = scale
        sharedPreferences.edit().putFloat("scale", scale).apply()

        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val viewWidth = width
        val viewHeight = height
        val imageResolution = (context.getScreenWidth() * 70) / 100

        bitmap?.let {
            val bitmapWidth = (imageResolution* scale).toInt()
            val bitmapHeight = (imageResolution * scale).toInt()

            val offsetX = (viewWidth - bitmapWidth) / 2
            val offsetY = (viewHeight - bitmapHeight) / 2

            val scaledBitmap = Bitmap.createScaledBitmap(it, bitmapWidth, bitmapHeight, false)
            progresswidth= (scale*100).toInt()
             sharedPreferences.edit().putInt("bitmapWidth", progresswidth).apply()

            canvas.drawBitmap(scaledBitmap, offsetX.toFloat(), offsetY.toFloat(), null)
        }
     /*   bitmap?.let {

            val imageResolution = (context.getScreenWidth() * 70) / 100

            val newWidth = (imageResolution * scale).toInt()
            val newHeight = (imageResolution * scale).toInt()


            //val newWidth = (it.width * scale).toInt()
            //val newHeight = (it.height * scale).toInt()

            val scaledBitmap = Bitmap.createScaledBitmap(it, newWidth, newHeight, true)
            canvas.drawBitmap(scaledBitmap, 0f, 0f, null)
        }*/
    }


    // Rest of your code...

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        val viewWidth = width
        val viewHeight = height
        val imageResolution = (context.getScreenWidth() * 70) / 100

        bitmap?.let {
            val bitmapWidth = (imageResolution * scale).toInt()
            val bitmapHeight = (imageResolution * scale).toInt()

            val offsetX = (viewWidth - bitmapWidth) / 2
            val offsetY = (viewHeight - bitmapHeight) / 2

            // Check if touch event occurred within the bounds of the drawn bitmap
            if (x >= offsetX && x < offsetX + bitmapWidth && y >= offsetY && y < offsetY + bitmapHeight) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> handleTouchDown(x, y)
                    MotionEvent.ACTION_MOVE -> handleTouchMove(x, y)
                    MotionEvent.ACTION_UP -> handleTouchUp(x, y)
                }
                return true
            }
        }

        return super.onTouchEvent(event)
    }
    private fun handleTouchDown(x: Float, y: Float) {
        lastTouchX = x
        lastTouchY = y
        // Perform any custom behavior here...
      //  setBackgroundColor(Color.BLUE)

    }

    private fun handleTouchMove(x: Float, y: Float) {
        val deltaX = x - lastTouchX
        val deltaY = y - lastTouchY
        // Perform any custom behavior here...
        translationX += deltaX
        translationY += deltaY
        lastTouchX = x
        lastTouchY = y
    }

    private fun handleTouchUp(x: Float, y: Float) {
        // Perform action when the user lifts their finger from the view
        // Example: Change the color of the view back to its original color
     //   setBackgroundColor(Color.WHITE)
    }


}
