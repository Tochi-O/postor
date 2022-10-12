package com.example.postorr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.smarteist.autoimageslider.SliderViewAdapter
import com.squareup.picasso.Picasso
import java.util.*

class VoteAdapter() :
    SliderViewAdapter<VoteAdapter.VH>() {
    private var mSliderItems = ArrayList<PostVote>()
    private var mpost = Post()
    var voted=0
    fun renewItems(sliderItems: Post) {
        mSliderItems = sliderItems.photos
        mpost=sliderItems
        notifyDataSetChanged()
    }

    fun addItem(sliderItem: PostVote) {
        mSliderItems.add(sliderItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): VH {
        val inflate: View = LayoutInflater.from(parent.context).inflate(R.layout.image_holder, null)
        //votebtn count btn
        return VH(inflate)
    }

    override fun onBindViewHolder(viewHolder: VH, position: Int) {
        var cuserid = FirebaseAuth.getInstance().currentUser?.uid
        var cusermail = FirebaseAuth.getInstance().currentUser?.email

        // var thepost:Post=mSliderItems[position]
        var thepostvote: PostVote=mSliderItems[position]
        //load image into view
        mSliderItems.sortWith(Comparator { o1: PostVote, o2: PostVote -> o1.num - o2.num })
        mSliderItems.reverse()
        Picasso.get().load(mSliderItems[position].url).fit().into(viewHolder.imageView)

        //view count //votebtn
        var votebtn = viewHolder.votebtnview
        //24hrs aafte date created disabe button
        var countview = viewHolder.votecount

        val DAY = (24 * 60 * 60 * 1000).toLong()
        if( mpost.datecreated.time < System.currentTimeMillis() - DAY){
            votebtn.isEnabled=false
        }else if (mpost.voters.contains(cuserid)){
            //if in voters list disable button
            votebtn.isEnabled=false
        }else {
            votebtn.setOnClickListener {

                thepostvote.num++
                //change in firestore
                countview.text = thepostvote.num.toString()
                mpost.voters.add(cusermail!!)
                votebtn.isEnabled=false

                FirebaseFirestore.getInstance().collection("posts").document(mpost.postId).collection("photos").document(thepostvote.id)
                    .update("num" , thepostvote.num).addOnSuccessListener {
                        if (cuserid != null) {
                            FirebaseFirestore.getInstance().collection("posts").document(mpost.postId).collection("voters").document(cuserid).set(
                                mapOf(
                                "email" to cusermail
                                )
                            )
                        }
                    }
                //add to voters list (userid and email)

                //increment here
            }
        }

        countview.text = thepostvote.num.toString()


    }

    override fun getCount(): Int {
        return mSliderItems.size
    }

    inner class VH(itemView: View) : ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.animageSlide)
        var votebtnview: Button = itemView.findViewById(R.id.voteup)
        var votecount: TextView = itemView.findViewById(R.id.votecount)

    }
}