package com.example.snapchatt

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_createsnap.*
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*
import java.util.jar.Manifest

class createsnapActivity : AppCompatActivity() {
var createsnapimageview:ImageView?=null
    var messagaedittext:EditText?=null
val imagename= UUID.randomUUID().toString() +".jpg"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createsnap)
        createsnapimageview=findViewById(R.id.imageView)
        messagaedittext=findViewById(R.id.editText3)
    }
    fun getPhoto()
    {
        val intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,1)
    }
    fun choose(view:View)
    {
        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
        else
            getPhoto()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val  selectedimage=data!!.data
        if(requestCode==1 &&resultCode== Activity.RESULT_OK &&data!=null)
        {
            try {
                val bitmap=MediaStore.Images.Media.getBitmap(this.contentResolver,selectedimage)
                createsnapimageview?.setImageBitmap(bitmap)
            }
            catch(e:Exception){e.printStackTrace()}

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==1)
        {
            if(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                getPhoto()
            }
        }
    }
    fun next(view: View)
    {
       // createsnapimageview?.isDrawingCacheEnabled = true
       // createsnapimageview?.buildDrawingCache()
       // val bitmap=createsnapimageview?.getDrawingCache()
      //  val baos = ByteArrayOutputStream()
       // bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
       // val data = baos.toByteArray()
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val storageRef = FirebaseStorage.getInstance().getReference()

// Create a reference to "mountains.jpg"
        val mountainsRef = storageRef.child("images").child(imagename)
       var uploadTask=mountainsRef.putBytes(data)


       // var uploadTask = FirebaseStorage.getInstance().getReference().child("images").child(imagename).putBytes(data)
      /*  pp.addOnFailureListener {
            // Handle unsuccessful uploads
            Toast.makeText(this,"upload failed",Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            val downloadurl=uploadTask.downloadUrl
            Log.i("..................",downloadurl.toString())


        }*/
        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            mountainsRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                Log.i("...............................",downloadUri.toString())
                val intentt=Intent(this,userActivity::class.java)
                intentt.putExtra("imageurl",downloadUri.toString())
                intentt.putExtra("imagename",imagename)
                intentt.putExtra("message",messagaedittext?.text.toString())
                startActivity(intentt)
            } else {
                // Handle failures
                // ...
            }
        }
    }
}
