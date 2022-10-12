package com.example.postorr

import android.content.Intent
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class PostListAdapter (private val mPosts: ArrayList<Post>) : RecyclerView.Adapter<PostListAdapter.ViewHolder>() {
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val nameTextView: TextView = itemView.findViewById<TextView>(R.id.apostname)
        val descTextView: TextView = itemView.findViewById<TextView>(R.id.aposttitle)
        val openbtnview: Button = itemView.findViewById(R.id.openpost)

    }

    // ... constructor and member variables
    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostListAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.eachpost, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: PostListAdapter.ViewHolder, position: Int) {
        // Get the data model based on position
        val apost: Post = mPosts[position]
        // Set item views based on your views and data model
        val nametextView1 = viewHolder.nameTextView
        nametextView1.text = apost.emailaddy
        val desctextView1 = viewHolder.descTextView
        desctextView1.text = apost.desc

        viewHolder.openbtnview.setOnClickListener {
            //go to vote
            var intent =  Intent(viewHolder.itemView.context, VoteActivity::class.java)
            intent.putExtra("postId",apost.postId)
            viewHolder.itemView.context.startActivity(intent)
        }




    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return mPosts.size
    }

}
