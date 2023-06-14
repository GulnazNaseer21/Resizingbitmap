package com.exampl.resizingbitmap

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.exampl.resizingbitmap.ScreenUtils.getScreenWidth
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var seekBar: SeekBar
    private lateinit var customView: CustomView

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
            customView=findViewById(R.id.customView)
            seekBar=findViewById(R.id.sb_scaling)
        seekBar.min = 35
        val sharedPreferences =
            this.getSharedPreferences("CustomViewPrefs", Context.MODE_PRIVATE)
        val progressValue = sharedPreferences?.getInt("bitmapWidth", 0)
        if (progressValue == 0) {
            seekBar.progress = this.getScreenWidth()
        } else {
            seekBar.progress = progressValue!!
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
        // val bitmap = BitmapFactory.decodeResource(resources, R.drawable.img_clock_10)
        //  customView.setBitmap(bitmap)

        main()
        // Load the image from resources
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.full_screen)

        // Set the loaded image as the background of the CustomCanvasView
        customView.setBackgroundBitmap(bitmap)

/*        // Show the bottom sheet
        val bottomSheetFragment = CustomBottomSheetFragment(customView)
        bottomSheetFragment.show(supportFragmentManager, "CustomBottomSheetFragment")*/

    }


    private suspend fun loadBitmapImage(resourceId: Int): android.graphics.Bitmap =
        withContext(Dispatchers.Default) {
            val options = BitmapFactory.Options().apply {
                // You can set additional options for bitmap decoding here if needed
            }
            BitmapFactory.decodeResource(resources, resourceId, options)
        }

    private fun main() = runBlocking {
        val resourceId = R.drawable.img_clock_10 // Replace with your image resource ID
        val image = withContext(Dispatchers.Default) {
            loadBitmapImage(resourceId)
        }

        customView.setBitmap(image)
    }


    class CustomBottomSheetFragment(private val customView: CustomView) : BottomSheetDialogFragment() {
        private lateinit var seekBar: SeekBar

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.bottom_sheet_layout, container, false)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            // Perform any additional setup or customization of the bottom sheet here

            // Example: Set click listener for a button in the bottom sheet
            val button = view.findViewById<Button>(R.id.closeButton)
            button.setOnClickListener {
                // Handle button click
                Toast.makeText(requireContext(),"test",Toast.LENGTH_LONG).show()
            }

            seekBar = view.findViewById(R.id.seekBar1)

            seekBar.min = 35
            val sharedPreferences =
                context?.getSharedPreferences("CustomViewPrefs", Context.MODE_PRIVATE)
            val progressValue = sharedPreferences?.getInt("bitmapWidth", 0)
            if (progressValue == 0) {
                seekBar.progress = context.getScreenWidth()
            } else {
                seekBar.progress = progressValue!!
            }

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    val scale: Float = progress.toFloat() / 100.0f
                    customView.setScale(scale)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })
        }
    }
}






