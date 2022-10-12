package com.example.postorr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.activity_vote.*
import java.util.*
import kotlin.collections.ArrayList
import java.util.Collections




class VoteActivity : AppCompatActivity() {



    lateinit var postId: String
    lateinit var apost: Post
    lateinit var votersname: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote)


        postId = intent.getStringExtra("postId").toString()
        apost = Post()
        val imageSlider = findViewById<SliderView>(R.id.imageSlider)
        votersname = ArrayList()

        // Initialize contacts

        // Initialize contacts
        //q = Contact.createContactsList(20)
        // Create adapter passing in the sample user data
        // Create adapter passing in the sample user data
      //  val adapter = VoteAdapter(apost)
        // Attach the adapter to the recyclerview to populate items
        // Attach the adapter to the recyclerview to populate items
      //  imageslider.adapter = adapter
        // Set layout manager to position the items
        // Set layout manager to position the items
      //  imageslider.layoutManager = LinearLayoutManager(this)
        // That's all!

        val rvQuestions = findViewById<View>(R.id.rvpeoplevote) as RecyclerView


        // Initialize contacts

        // Initialize contacts
        //q = Contact.createContactsList(20)
        // Create adapter passing in the sample user data
        // Create adapter passing in the sample user data
        val adapter1 = VoteListAdapter(votersname)
        // Attach the adapter to the recyclerview to populate items
        // Attach the adapter to the recyclerview to populate items
        rvQuestions.adapter = adapter1
        // Set layout manager to position the items
        // Set layout manager to position the items
        rvQuestions.layoutManager = LinearLayoutManager(this)

        val adapter = VoteAdapter()
        //adapter.notifyDataSetChanged()
        apost.photos= ArrayList()
        apost.voters= ArrayList()
        adapter.renewItems(apost)
        imageSlider.setSliderAdapter(adapter)
        //adapter.notifyDataSetChanged()
        imageSlider.isAutoCycle = false
        imageSlider.stopAutoCycle()
        // adapter.notifyDataSetChanged()

        FirebaseFirestore.getInstance().collection("posts").document(postId).get().addOnSuccessListener { doc->
            if(doc.data!=null) {
                apost.postId = doc.id
                apost.emailaddy = doc.data!!["emailaddy"] as String
                apost.userid = doc.data!!["userid"] as String
                apost.desc = doc.data?.get("desc") as String
                val timestamp = doc.data?.get("datecreated") as Timestamp
                apost.datecreated = timestamp.toDate()
                apost.photos = ArrayList()
                apost.voters = ArrayList()

                FirebaseFirestore.getInstance().collection("posts").document(postId).collection("photos").get().addOnSuccessListener { doc2 ->
                    doc2.forEach {doc3->
                        var apvote=PostVote()
                        apvote.num = (doc3.data["num"] as Long).toInt()
                        apvote.url = doc3.data["url"] as String
                        apvote.id = doc3.id
                        apost.photos.add(apvote)
                        Log.d("photos length",apost.photos.size.toString())

                    }
                    //sort photos
                    Log.d("photos length",apost.photos.size.toString())
                    apost.photos.sortWith(Comparator { o1: PostVote, o2: PostVote -> o1.num - o2.num })
                    apost.photos.reverse()
                    adapter.renewItems(apost)
                    adapter.notifyDataSetChanged()
                }
                FirebaseFirestore.getInstance().collection("posts").document(postId).collection("voters").get().addOnSuccessListener { doc2 ->
                    doc2.forEach {doc4->

                        apost.voters.add(doc4.id)
                        votersname.add(doc4.data["email"].toString())
                        Log.d("photos length",apost.voters.size.toString())

                    }
                    adapter1.notifyDataSetChanged()
                    adapter.renewItems(apost)

                }


            }



        }

    }
}