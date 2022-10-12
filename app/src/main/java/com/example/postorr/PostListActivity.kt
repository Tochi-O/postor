package com.example.postorr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class PostListActivity : AppCompatActivity() {




    lateinit var mainfriendss: ArrayList<Person>
    lateinit var mainfriendids: ArrayList<String>

    lateinit var friendspost: ArrayList<Post>
    lateinit var allpeople: ArrayList<Person>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_list)


        //get all friended id


        //get all posts and keep the ones made by friends

        mainfriendss = ArrayList()
        mainfriendids = ArrayList()
        friendspost = ArrayList()
        val rvQuestions = findViewById<View>(R.id.rvposts) as RecyclerView


        // Initialize contacts

        // Initialize contacts
        //q = Contact.createContactsList(20)
        // Create adapter passing in the sample user data
        // Create adapter passing in the sample user data
        val adapter = PostListAdapter(friendspost)
        // Attach the adapter to the recyclerview to populate items
        // Attach the adapter to the recyclerview to populate items
        rvQuestions.adapter = adapter
        // Set layout manager to position the items
        // Set layout manager to position the items
        rvQuestions.layoutManager = LinearLayoutManager(this)
        var cUserId = FirebaseAuth.getInstance().currentUser?.uid
        //get list of friends
        if (cUserId != null) {
            FirebaseFirestore.getInstance().collection("user").document(cUserId).collection("friended").get().addOnSuccessListener {
                it.forEach { doc->
                    var afr = Person()
                    afr.uid = doc.id
                    mainfriendids.add(afr.uid)
                }

                //update adapter
                //friendspost = ArrayList()
                FirebaseFirestore.getInstance().collection("posts").get().addOnSuccessListener {
                    it.forEach { doc ->
                        var aPost = Post()
                        aPost.userid = doc.data["userid"].toString()
                        aPost.desc = doc.data["desc"].toString()
                        aPost.emailaddy = doc.data["emailaddy"].toString()
                        val timestamp = doc.data["datecreated"] as Timestamp
                        aPost.datecreated = timestamp.toDate()
                        aPost.postId = doc.id

                        aPost.photos = ArrayList()
                        FirebaseFirestore.getInstance().collection("posts").document(doc.id)
                            .collection("photos").get().addOnSuccessListener {
                                it.forEach { doc ->
                                    var aphoto: PostVote = PostVote()
                                    aphoto.url = doc.data["url"].toString()
                                    aphoto.num = (doc.data["num"] as Long).toInt()
                                    aPost.photos.add(aphoto)
                                }
                                Log.d("length",aPost.photos.size.toString())

                            }
                        if (mainfriendids.contains(aPost.userid)) {
                            friendspost.add(aPost)
                        }

                        //update adapter
                    }
                    //view list
                    adapter.notifyDataSetChanged()
                    Log.d("length",friendspost.size.toString())

                }

            }
        }

        //get list of everyone


    }
}