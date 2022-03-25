package com.adsum.camel_masterapplication.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PointF
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.GestureDetector.OnDoubleTapListener
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.ActivityImagePreviewBinding
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream


class ImagePreviewActivity : AppCompatActivity() {
    private lateinit var imagePreviewBinding: ActivityImagePreviewBinding
    var lastEvent: FloatArray? = null
    var d = 0f
    var newRot = 0f
    private var isZoomAndRotate = false
    private var isOutSide = false
    private val NONE = 0
    private val DRAG = 1
    private val ZOOM = 2
    private var mode = NONE
    private val start = PointF()
    private val mid = PointF()
    var oldDist = 1f
    private var xCoOrdinate = 0f
    private  var yCoOrdinate:kotlin.Float = 0f

    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        imagePreviewBinding = ActivityImagePreviewBinding.inflate(layoutInflater)
        setContentView(imagePreviewBinding.root)

        setSupportActionBar(imagePreviewBinding.toolbar3)
        supportActionBar?.apply {
        }

        if (intent != null) {
            if (intent.hasExtra("images") && intent.getStringExtra("images") != null) {
                val images = intent.getStringExtra("images")!!
                Glide.with(this).asBitmap().load(images).into(imagePreviewBinding.imageView)
            }
        }

        with(imagePreviewBinding) {
            imageView.setOnTouchListener(OnTouchListener { v, event ->
                val view: ImageView = v as ImageView
                view.bringToFront()
                supportActionBar?.hide()
                imageLayout.setBackgroundColor(R.color.black)
                viewTransformation(view, event)
                true
            })
        }
        imagePreviewBinding.backTextView.setOnClickListener {
            finish()
        }
        imagePreviewBinding.shareButton.setOnClickListener {
            val imageView = imagePreviewBinding.imageView
            shareImageandText(imageView)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun viewTransformation(view: View, event: MotionEvent) {
        when (event.action and MotionEvent.ACTION_MASK) {

            MotionEvent.ACTION_DOWN -> {
                xCoOrdinate = view.x - event.rawX
                yCoOrdinate = view.y - event.rawY
                start.set(event.x, event.y)
                isOutSide = false
                mode = DRAG
                lastEvent = null
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDist = spacing(event)
                if (oldDist > 1f) {
                    mode = ZOOM
                }
                lastEvent = FloatArray(4)
                lastEvent!![0] = event.getX(0)
                lastEvent!![1] = event.getX(1)
                lastEvent!![2] = event.getY(0)
                lastEvent!![3] = event.getY(1)
            }
            MotionEvent.ACTION_MOVE -> if (!isOutSide) {
                if (mode == ZOOM && event.pointerCount == 2) {
                    val newDist1 = spacing(event)
                    if (newDist1 > 1f) {
                        val scale: Float = newDist1 / oldDist * view.scaleX
                        view.scaleX = scale
                        view.scaleY = scale
                    }
                    if (lastEvent != null) {
                        view.rotation = (view.rotation + (newRot - d)) as Float
                    }
                }
            }
        }
    }
    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt((x * x + y * y).toDouble()).toInt().toFloat()
    }
    private fun shareImageandText(imageView: ImageView) {
        val uri: Uri? = getmageToShare(imageView)
        val intent = Intent(Intent.ACTION_SEND)

        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.putExtra(Intent.EXTRA_TEXT, "Sharing Image")
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
        intent.type = "image/png"

        startActivity(Intent.createChooser(intent, "Share Via"))
    }

    private fun getmageToShare(imageView: ImageView): Uri? {
        val imagefolder = File(cacheDir, "images")
        var uri: Uri? = null
        try {
            imagefolder.mkdirs()
            val file = File(imagefolder, "shared_image.png")
            val outputStream = FileOutputStream(file)
                imageView.setImageURI(uri)
            outputStream.flush()
            outputStream.close()
            uri = FileProvider.getUriForFile(this, "com.anni.shareimage.fileprovider", file)
        } catch (e: Exception) {
            Toast.makeText(this, "" + e.message, Toast.LENGTH_LONG).show()
        }
        return uri
    }
}