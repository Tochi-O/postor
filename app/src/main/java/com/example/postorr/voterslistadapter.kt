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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class VoteListAdapter (private val mpeople: ArrayList<String>) : RecyclerView.Adapter<VoteListAdapter.ViewHolder>() {
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val nameTextView: TextView = itemView.findViewById<TextView>(R.id.ivotedname)
       // val addbtn: Button = itemView.findViewById<Button>(R.id.friendadd)

    }

    // ... constructor and member variables
    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VoteListAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.friendsvoted, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: VoteListAdapter.ViewHolder, position: Int) {
        // Get the data model based on position
        val ppname= mpeople[position]
        // Set item views based on your views and data model
        val nametextView1 = viewHolder.nameTextView
        nametextView1.text = ppname



    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return mpeople.size
    }



}