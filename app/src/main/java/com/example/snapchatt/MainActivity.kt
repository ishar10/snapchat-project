package com.example.snapchatt

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage


class MainActivity : AppCompatActivity() {

    var e1:EditText?=null
    var p:EditText?=null
    val mAuth=FirebaseAuth.getInstance();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        e1=findViewById(R.id.editText)
        p=findViewById(R.id.editText2)
        if(mAuth.currentUser!=null)
        {
login()
        }
    }
    fun go(view: View)
    {mAuth.signInWithEmailAndPassword(e1?.text.toString(), p?.text.toString())
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val database = Firebase.database
                val myRef = database.getReference("users")

                val mm=myRef.child(task.result?.user!!.uid)
                val nn=mm.child("email")
                nn.setValue(e1?.text.toString())
               login()
            } else {

                mAuth.createUserWithEmailAndPassword(e1?.text.toString(), p?.text.toString()).addOnCompleteListener(this){task ->
                    if(task.isSuccessful)
                    {  val database = Firebase.database
                        val myRef = database.getReference("users")

                        val mm=myRef.child(task.result?.user!!.uid)
                        val nn=mm.child("email")
                        nn.setValue(e1?.text.toString())
                        login()
                    }
                    else
                    {
                        Toast.makeText(this,"Login failed. try again",Toast.LENGTH_SHORT).show()
                    }

                }
            }


        }

    }
    fun login()
    {
val intent=Intent(this,SnapsActivity::class.java)
        startActivity(intent)
    }
}
