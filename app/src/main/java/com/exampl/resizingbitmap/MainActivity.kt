package com.exampl.resizingbitmap

import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import com.exampl.resizingbitmap.ScreenUtils.getScreenWidth

class MainActivity : AppCompatActivity() {

    private lateinit var seekBar: SeekBar
    private lateinit var customView: CustomView
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBar = findViewById(R.id.seekBar)
        customView = findViewById(R.id.customView)

        seekBar.min=35
        val  sharedPreferences = this@MainActivity.getSharedPreferences("CustomViewPrefs", Context.MODE_PRIVATE)
        val progressValue = sharedPreferences.getInt("bitmapWidth", 0)
        if(progressValue==0){
            seekBar.progress=getScreenWidth()
       }
        else{
            seekBar.progress=progressValue
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val scale: Float = progress.toFloat() / 100.0f
                customView.setScale(scale)


            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // Load your bitmap image here and set it to the custom view
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.img_clock_10)
        customView.setBitmap(bitmap)
    }
}