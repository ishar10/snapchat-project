package com.example.snapchatt

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SnapsActivity : AppCompatActivity() {
    val mAuth= FirebaseAuth.getInstance();
    var snapslistview:ListView? =null
    var emails:ArrayList<String> = ArrayList()
    var snaps:ArrayList<DataSnapshot> =ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snaps)
        snapslistview = findViewById(R.id.list)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, emails)
        snapslistview?.adapter = adapter
        val database = Firebase.database
        val myRef = database.getReference("users")
        val mm = myRef.child(mAuth.currentUser!!.uid)
        val pp = mm.child("snaps")
        pp.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                emails.add(p0.child("from").value as String)
                snaps.add(p0)
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}


            override fun onChildRemoved(p0: DataSnapshot) {
                var index=0
                for(snap:DataSnapshot in snaps)
                {
                    if(snap.key==p0.key)
                    {
                        snaps.removeAt(index)
                        emails.removeAt(index)
                    }
                    index++
                }
                adapter.notifyDataSetChanged()
            }

        })

snapslistview?.onItemClickListener= AdapterView.OnItemClickListener { parent, view, position, id ->
var snapshot= snaps.get(position)
    var intent= Intent(this,ViewSnapActivity::class.java)
    intent.putExtra("imagename",snapshot.child("from").value as String)
    intent.putExtra("imageurl",snapshot.child("imageurl").value as String)
    intent.putExtra("message",snapshot.child("message").value as String)
    intent.putExtra("snapkey",snapshot.key)

    startActivity(intent)
}

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater=menuInflater
        inflater.inflate(R.menu.snaps,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item?.itemId==R.id.createSnap)
        {
val intent= Intent(this,createsnapActivity::class.java)
            startActivity(intent)

        }
        else if(item?.itemId==R.id.logout)
        {
            mAuth.signOut()
finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mAuth.signOut();
    }
}
