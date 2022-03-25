package com.adsum.camel_masterapplication.Activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.adsum.camel_masterapplication.R
import com.adsum.camel_masterapplication.databinding.FragmentDocumentBinding
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.util.*

class PdfViewActivity : AppCompatActivity() {
    private lateinit var fragmentDocumentBinding: FragmentDocumentBinding
    private lateinit var bitmap : Bitmap
    private lateinit var linear : LinearLayout
    var fileName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentDocumentBinding = FragmentDocumentBinding.inflate(layoutInflater)
        setContentView(fragmentDocumentBinding.root)

        setSupportActionBar(fragmentDocumentBinding.toolbar3)
        supportActionBar?.apply {
        }
        fragmentDocumentBinding.backTextView.setOnClickListener {
            finish()
        }

        if (intent != null) {
            if (intent.hasExtra("data") && intent.getStringExtra("data") != null) {
                val datalJson = intent.getStringExtra("data")!!
                val jsonArray = JSONArray(datalJson)
                Log.e("tag", "jsonArray:--" + jsonArray)
                Log.e("tag", "dataJson:--" + datalJson)
                Log.e("tag", "jsonArray.size:--" + jsonArray.length())

                val stk: TableLayout = fragmentDocumentBinding.tableMain

                for (i in 0..jsonArray.length() - 1) {
                    val item: JSONObject = jsonArray.getJSONObject(i)
                    val tbrow = TableRow(this)
                    tbrow.width

                    val t1v = TextView(this)
                    // val t1v=fragmentDocumentBinding.t1v
                    t1v.text = item.getString("name_of_participant")
                    t1v.setTextColor(Color.BLACK)
                    t1v.setBackgroundResource(R.drawable.cell_shape)
                    t1v.gravity = Gravity.CENTER
                    if (t1v.getParent() != null) {
                        (t1v.getParent() as ViewGroup).removeView(t1v)
                    }
                    tbrow.addView(t1v)
                    // val t2v = fragmentDocumentBinding.t2v
                    val t2v = TextView(this)
                    t2v.text = item.getString("rc_camel")
                    t2v.setBackgroundResource(R.drawable.cell_shape)
                    t2v.setTextColor(Color.BLACK)
                    t2v.gravity = Gravity.CENTER
                    if (t2v.getParent() != null) {
                        (t2v.getParent() as ViewGroup).removeView(t2v)
                    }
                    tbrow.addView(t2v)
                    //  val t3v = fragmentDocumentBinding.t3v.
                    val t3v = TextView(this)
                    t3v.text = item.getString("camel_no")
                    t3v.setBackgroundResource(R.drawable.cell_shape)
                    t3v.setTextColor(Color.BLACK)
                    t3v.gravity = Gravity.CENTER
                    if (t3v.getParent() != null) {
                        (t3v.getParent() as ViewGroup).removeView(t3v)
                    }
                    tbrow.addView(t3v)
                    //  val t4v = fragmentDocumentBinding.t4v
//                    val t4v = TextView(this)
//                    t4v.text = item.getString("description")
//                    t4v.setBackgroundResource(R.drawable.cell_shape)
//                    t4v.setTextColor(Color.BLACK)
//                    t4v.gravity = Gravity.CENTER
//                    if (t4v.getParent() != null) {
//                        (t4v.getParent() as ViewGroup).removeView(t4v)
//                    }
//                    tbrow.addView(t4v)
                    if (tbrow.getParent() != null) {
                        (tbrow.getParent() as ViewGroup).removeView(tbrow)
                    }
                    stk.addView(tbrow)
                }
            }
            if (intent.hasExtra("Name") && intent.getStringExtra("Name") != null) {
                val name = intent.getStringExtra("Name")!!
                fileName = name
            }
        }
        Log.e("name", "RaceName -- " +fileName)
        linear = fragmentDocumentBinding.linearLay
        fragmentDocumentBinding.pdfShareButton.setOnClickListener {
            bitmap = LoadBitmap(linear, linear.getWidth(), linear.getHeight())!!
            createPdf()
        }
    }
    private fun LoadBitmap(v: View, width: Int, height: Int): Bitmap? {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        v.draw(canvas)
        return bitmap
    }
    private fun createPdf() {
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        //  Display display = wm.getDefaultDisplay();
        val displaymetrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(displaymetrics)
        val hight = displaymetrics.heightPixels.toFloat()
        val width = displaymetrics.widthPixels.toFloat()
        val convertHighet = hight.toInt()
        val convertWidth = width.toInt()
        val document = PdfDocument()
        val pageInfo: android.graphics.pdf.PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(
            convertWidth,
            convertHighet,
            1
        ).create()
        val page: PdfDocument.Page = document.startPage(pageInfo)
        val canvas: Canvas = page.getCanvas()
        val paint = Paint()
        canvas.drawPaint(paint)
        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true)
        paint.setColor(Color.BLUE)
//        val rectangle = Rect(0, 0, 100, 100)
        canvas.drawBitmap(bitmap,0f,0f,null)
        document.finishPage(page)

        // write the document content
        val targetPdf = Environment.getExternalStorageDirectory().getPath().toString() + "/" +fileName +".pdf"
        val filePath: File
        filePath = File(targetPdf)
        Log.e("path","pdfFilePath ---- "+filePath)
        try {
            document.writeTo(FileOutputStream(filePath))
            Toast.makeText(this, "successfully pdf created", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("e" , "error-- " +e)
            Toast.makeText(this, "Something wrong: $e", Toast.LENGTH_LONG).show()
        }
        document.close()
        openPdf()
    }

    private fun openPdf() {
        val file = File(Environment.getExternalStorageDirectory().getPath().toString() +"/" +fileName+ ".pdf")
        Log.e("path","pdf ---- "+file)
        if (file.exists()) {
            val target = Intent(Intent.ACTION_VIEW)
            val uri: Uri = Uri.fromFile(file)
            intent.setDataAndType(uri, "application/pdf")
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)

            val intent = Intent.createChooser(target, "Open File")
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Log.e("open" ,"Error--- "+e)
                Toast.makeText(this, "No Application for pdf view", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this, "pdf not available", Toast.LENGTH_SHORT).show()
        }
    }
}