package com.exampl.resizingbitmap

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.exampl.resizingbitmap.ScreenUtils.getScreenWidth

class MainActivity : AppCompatActivity() {

    private lateinit var seekBar: SeekBar
    private lateinit var seekBarResize: SeekBar
    private lateinit var customView: CustomView
//    private lateinit var customViewOld: CustomViewOld
    private lateinit var  mainLayout: ViewGroup
    private lateinit var  image: ImageView
    private var xDelta:Float = 0f
    private var yDelta:Float = 0f
    private var lastTouchX: Float = 0f
    private var lastTouchY: Float = 0f
    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar = findViewById(R.id.seekBar)
        seekBarResize = findViewById(R.id.seekBarResize)
        customView = findViewById(R.id.customView)
        //customViewOld = findViewById(R.id.customViewOld)
        image=findViewById(R.id.image)
        mainLayout=findViewById(R.id.main)
        image.setOnTouchListener(onTouchListener())

//        seekBar.min=35
        val  sharedPreferences = this@MainActivity.getSharedPreferences("CustomViewPrefs", Context.MODE_PRIVATE)
        val progressValue = sharedPreferences.getInt("bitmapWidth", 0)
        if(progressValue==0){
           // seekBar.max=getScreenWidth()
            seekBar.progress=getScreenWidth()
            Toast.makeText(this,"Test ${seekBar.progress}",Toast.LENGTH_SHORT).show()
       }
        else{
            seekBar.progress=progressValue
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Toast.makeText(this@MainActivity,"Progress $progress}",Toast.LENGTH_SHORT).show()



                if(progress>=35){
                val scale: Float = progress.toFloat() / 100.0f
                customView.setScale(scale)}
                else{
                    Toast.makeText(this@MainActivity,"ElseProgress $progress}",Toast.LENGTH_SHORT).show()

                }
              //  customViewOld.setScale(scale)


            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {


            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {


            }
        })


  /*      seekBarResize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val sizeMultiplier = progress / 100f

                val requestOptions = RequestOptions()
                    .transform(GranularRoundedCorners(0f, 0f, 0f, 0f))
                    .override((image.width * sizeMultiplier).toInt(), (image.height * sizeMultiplier).toInt())


            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })*/
        // Load your bitmap image here and set it to the custom view
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.img_clock_10)
        val bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.img_clock_11)
        customView.setBitmap(bitmap)
       // customViewOld.setBitmap(bitmap1)
    }
    private fun onTouchListener(): View.OnTouchListener? {
        return View.OnTouchListener { view, event ->
            val x = event.rawX.toInt()
            val y = event.rawY.toInt()
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    val lParams = view.layoutParams as RelativeLayout.LayoutParams
                    xDelta = (x - lParams.leftMargin).toFloat()
                    yDelta = (y - lParams.topMargin).toFloat()
                }

                MotionEvent.ACTION_UP -> Toast.makeText(
                    this,
                    "thanks for new location! xDelta..$xDelta----yDelta..$yDelta", Toast.LENGTH_SHORT
                )
                    .show()

                MotionEvent.ACTION_MOVE -> {
                    val layoutParams = view
                        .layoutParams as RelativeLayout.LayoutParams
                    layoutParams.leftMargin = (x - xDelta).toInt()
                    layoutParams.topMargin = (y - yDelta).toInt()
                    layoutParams.rightMargin = 0
                    layoutParams.bottomMargin = 0
                    view.layoutParams = layoutParams
                }
            }
            mainLayout!!.invalidate()
            true
        }
    }


}