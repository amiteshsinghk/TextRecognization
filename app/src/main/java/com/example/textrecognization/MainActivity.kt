package com.example.textrecognization

import android.content.Intent
import android.graphics.Point
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import java.io.IOException
import com.google.firebase.ml.vision.FirebaseVision

import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnFailureListener

import com.google.firebase.ml.vision.text.FirebaseVisionText

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions
import java.util.*
import com.google.firebase.ml.vision.text.RecognizedLanguage





class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        choose_button.setOnClickListener {
            details.text=""
            var i: Intent = Intent()
            i.type = "image/*"
            i.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(i, "Choose Image"), 111)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==111){
            image_view.setImageURI(data?.data)
            val image: FirebaseVisionImage
            try {
                image = FirebaseVisionImage.fromFilePath(applicationContext, data?.data!!)
                val textRecognizer = FirebaseVision.getInstance()
                    .onDeviceTextRecognizer

// Or, to provide language hints to assist with language detection:
// See https://cloud.google.com/vision/docs/languages for supported languages

// Or, to provide language hints to assist with language detection:
// See https://cloud.google.com/vision/docs/languages for supported languages
//                val options = FirebaseVisionCloudTextRecognizerOptions.Builder()
//                    .setLanguageHints(Arrays.asList("en", "hi"))
//                    .build()
//                val textRecognizer = FirebaseVision.getInstance()
//                    .getCloudTextRecognizer(options)




                textRecognizer.processImage(image)
                    .addOnSuccessListener {
                        // Task completed successfully
                        // ...
//                        details.text = it.text
                        val resultText: String = it.getText()
                        for (block in it.getTextBlocks()) {
                            val blockText: String = block.getText()
                            val blockConfidence: Float? = block.confidence
                            val blockLanguages: List<RecognizedLanguage> =
                                block.getRecognizedLanguages()
                            val blockCornerPoints: Array<Point> = block.getCornerPoints() as Array<Point>
                            val blockFrame: Rect? = block.getBoundingBox()
                            for (line in block.getLines()) {
                                val lineText: String = line.getText()
                                val lineConfidence: Float? = line.getConfidence()
                                val lineLanguages: List<RecognizedLanguage> =
                                    line.getRecognizedLanguages()
                                val lineCornerPoints: Array<Point> = line.getCornerPoints() as Array<Point>
                                val lineFrame: Rect? = line.getBoundingBox()
                                for (element in line.getElements()) {
                                    val elementText: String = element.getText()
                                    val elementConfidence: Float? = element.getConfidence()
                                    val elementLanguages: List<RecognizedLanguage> =
                                        element.getRecognizedLanguages()
                                    val elementCornerPoints: Array<Point> =
                                        element.getCornerPoints() as Array<Point>
                                    val elementFrame: Rect? = element.getBoundingBox()
                                    details.append(element.text+" ")
                                }
                                details.append("\n")
                            }
                            details.append("\n")
                        }
                    }
                    .addOnFailureListener {
                        // Task failed with an exception
                        // ...
                        Toast.makeText(this,it.toString(),Toast.LENGTH_LONG).show()
                    }

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
}