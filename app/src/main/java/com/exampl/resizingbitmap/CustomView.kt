package com.exampl.resizingbitmap

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import com.exampl.resizingbitmap.ScreenUtils.getScreenWidth

class CustomView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var bitmap: Bitmap? = null
    private var scale = 1.0f
    private val sharedPreferences: SharedPreferences
    private var progresswidth=0
    private var lastTouchX: Float = 0f
    private var lastTouchY: Float = 0f
    private var lastScaleFactor = 0f
    private var gestureDetector: ScaleGestureDetector

    private var offscreenBitmap: Bitmap? = null
    private var offscreenCanvas: Canvas? = null
    private var backgroundImage: Bitmap? = null



    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : this(context, attrs)
    init {
        sharedPreferences = context.getSharedPreferences("CustomViewPrefs", Context.MODE_PRIVATE)
        scale = sharedPreferences.getFloat("scale", 1.0f)
        progresswidth = sharedPreferences.getInt("bitmapWidth", 0)
        gestureDetector = ScaleGestureDetector(context, ScaleListener())

    }

    fun setBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
        invalidate()
    }

    fun setBackgroundBitmap(bitmap: Bitmap) {
        backgroundImage = bitmap
        invalidate() // Request a redraw of the view
    }
    fun setScale(scale: Float) {
        this.scale = scale
        sharedPreferences.edit().putFloat("scale", scale).apply()

        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        if (offscreenBitmap == null) {
            offscreenBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            offscreenCanvas = Canvas(offscreenBitmap!!)
        }

        offscreenCanvas?.let { offscreenCanvas ->
            offscreenCanvas.drawColor(Color.BLUE, PorterDuff.Mode.CLEAR)


          /*  // Draw the background image if available
            backgroundImage?.let {
                val srcRect = Rect(0, 0, it.width, it.height)
                val destRect = Rect(0, 0, canvas.width, canvas.height)
                offscreenCanvas.drawBitmap(it, srcRect, destRect, null)
            }*/
            val viewWidth = width
        val viewHeight = height
        val imageResolution = (context.getScreenWidth() * 70) / 100

            bitmap?.let {
                val bitmapWidth = (imageResolution * scale).toInt()
                val bitmapHeight = (imageResolution * scale).toInt()

                val offsetX = (viewWidth - bitmapWidth) / 2
                val offsetY = (viewHeight - bitmapHeight) / 2

                val scaledBitmap = Bitmap.createScaledBitmap(it, bitmapWidth, bitmapHeight, false)
                progresswidth= (scale*100).toInt()
                sharedPreferences.edit().putInt("bitmapWidth", progresswidth).apply()

                offscreenCanvas.drawBitmap(scaledBitmap, offsetX.toFloat(), offsetY.toFloat(), null)
            }
        }
        canvas.drawBitmap(offscreenBitmap!!, 0f, 0f, null)


        /*        bitmap?.let {
                    val bitmapWidth = (imageResolution* scale).toInt()
                    val bitmapHeight = (imageResolution * scale).toInt()

                    val offsetX = (viewWidth - bitmapWidth) / 2
                    val offsetY = (viewHeight - bitmapHeight) / 2

                    val scaledBitmap = Bitmap.createScaledBitmap(it, bitmapWidth, bitmapHeight, false)
                    progresswidth= (scale*100).toInt()
                     sharedPreferences.edit().putInt("bitmapWidth", progresswidth).apply()

                    canvas.drawBitmap(scaledBitmap, offsetX.toFloat(), offsetY.toFloat(), null)
                }*/
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
        gestureDetector.onTouchEvent(event)
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
    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scaleFactor = detector.scaleFactor

            if (lastScaleFactor == 0f || (scaleFactor > 1 && lastScaleFactor < scaleFactor)
                || (scaleFactor < 1 && lastScaleFactor > scaleFactor)
            ) {
                val x = detector.focusX
                val y = detector.focusY

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
                        scale *= scaleFactor

                        // Limit the scale to a certain range if desired
                        // scale = scale.coerceIn(minScale, maxScale)

                        // Redraw the view
                       invalidate()


                    }
                }
            }

            lastScaleFactor = scaleFactor

            return true
        }
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

        invalidate()

    }

    private fun handleTouchUp(x: Float, y: Float) {

        // Perform action when the user lifts their finger from the view
        // Example: Change the color of the view back to its original color
     //   setBackgroundColor(Color.WHITE)
    }


}
