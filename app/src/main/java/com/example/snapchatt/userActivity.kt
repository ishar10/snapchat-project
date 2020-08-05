package com.example.snapchatt
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class userActivity : AppCompatActivity() {
    var chooseUserlistview:ListView?=null
    var emails:ArrayList<String> = ArrayList()
    var keys:ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        chooseUserlistview=findViewById(R.id.chooseuserlistview)
        val adapter= ArrayAdapter(this,android.R.layout.simple_list_item_1,emails)
        chooseUserlistview?.adapter=adapter
        val database = Firebase.database
        val myRef = database.getReference("users")

        val mm=myRef.addChildEventListener(object:ChildEventListener{

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val email= p0.child("email").value as String
                emails.add(email)
                keys.add(p0.key!!)
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot) {}

        })

        chooseUserlistview?.onItemClickListener= AdapterView.OnItemClickListener { parent, view, position, id ->
            val snapMap: Map<String,String> =mapOf("from" to FirebaseAuth.getInstance().currentUser!!.email!! ,"imagename" to intent.getStringExtra("imagename"),"imageurl" to intent.getStringExtra("imageurl"), "message" to intent.getStringExtra("message")  )
            val database = Firebase.database
            val myRef = database.getReference("users")

            val mm=myRef.child(keys.get(position))
            val nn=mm.child("snaps")
            nn.push().setValue(snapMap)
            val intent=Intent(this,SnapsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)

        }
    }
}



