package com.example.postorr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddFriendActivity : AppCompatActivity() {




    lateinit var mainfriendss: ArrayList<Person>
    lateinit var mainfriendids: ArrayList<String>

    lateinit var potenfriendss: ArrayList<Person>
    lateinit var allpeople: ArrayList<Person>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)

        mainfriendss = ArrayList()
        mainfriendids = ArrayList()
        allpeople = ArrayList()

        val rvQuestions = findViewById<View>(R.id.rvfriendnames) as RecyclerView


        // Initialize contacts

        // Initialize contacts
        //q = Contact.createContactsList(20)
        // Create adapter passing in the sample user data
        // Create adapter passing in the sample user data
        val adapter = PeopleListAdapter(allpeople)
        // Attach the adapter to the recyclerview to populate items
        // Attach the adapter to the recyclerview to populate items
        rvQuestions.adapter = adapter
        // Set layout manager to position the items
        // Set layout manager to position the items
        rvQuestions.layoutManager = LinearLayoutManager(this)
        var cUserId = FirebaseAuth.getInstance().currentUser?.uid
        //get list of friends
        if (cUserId != null) {
            FirebaseFirestore.getInstance().collection("user").document(cUserId).collection("friends").get().addOnSuccessListener {
               it.forEach { doc->
                   var afr = Person()
                   afr.uid = doc.data["uid"].toString()
                   afr.friended = true
                   afr.emailaddy = doc.data["emailaddy"].toString()
                   mainfriendss.add(afr)
                   mainfriendids.add(afr.uid)
               }
                //update adapter
            }
        }

        //get list of everyone

        FirebaseFirestore.getInstance().collection("user").get().addOnSuccessListener {
            it.forEach { doc->
                var afr = Person()
                afr.uid = doc.id
                afr.emailaddy = doc.data["emailaddy"].toString()
                if(mainfriendids.contains(afr.uid)){
                    afr.friended=true
                }
                allpeople.add(afr)



            }

            adapter.notifyDataSetChanged()

            //update adapter
        }
        //view list

    }
}