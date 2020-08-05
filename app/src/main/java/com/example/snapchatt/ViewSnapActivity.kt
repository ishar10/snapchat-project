package com.example.snapchatt

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.net.HttpURLConnection
import java.net.URL

class ViewSnapActivity : AppCompatActivity() {
    val mAuth= FirebaseAuth.getInstance();
var messagetextview:TextView? =null
    var snapimageview:ImageView? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_snap)
        messagetextview=findViewById(R.id.textView)
        snapimageview=findViewById(R.id.imageView2)
        messagetextview?.text=intent.getStringExtra("message")
        val task = image()
        val m1: Bitmap
        try {
            m1 =
                task.execute(intent.getStringExtra("imageurl"))
                    .get()!!
            snapimageview?.setImageBitmap(m1)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
      class image :AsyncTask<String?, Void?, Bitmap?>() {
         override fun doInBackground(vararg urls: String?): Bitmap? {
             try {
                 val url = URL(urls[0])
                 val c =
                     url.openConnection() as HttpURLConnection
                 c.connect()
                 val `in` = c.inputStream
                 return BitmapFactory.decodeStream(`in`)
             } catch (e: Exception) {
                 e.printStackTrace()
                 return null
             }
         }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val database = Firebase.database
        val myRef = database.getReference("users")

        val mm=myRef.child(mAuth.currentUser!!.uid)
        val nn=mm.child("snaps")
        val ppp=nn.child(intent.getStringExtra("snapkey"))
        ppp.removeValue()

        val storageRef = FirebaseStorage.getInstance().getReference()
        val img=storageRef.child("images")
        val imm=img.child(intent.getStringExtra("imagename"))
imm.delete()
    }
}
