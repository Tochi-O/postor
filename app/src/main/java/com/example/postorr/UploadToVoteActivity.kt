package com.example.postorr

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_upload_to_vote.*
import java.util.*
import kotlin.collections.ArrayList

class UploadToVoteActivity : AppCompatActivity() {



    //store uris of picked images
    private var images: ArrayList<Uri?>? = null

    //current position/index of selected images
    private var position = 0

    //request code to pick image(s)
    private val PICK_IMAGES_CODE = 0

    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    lateinit var receiptRef: DocumentReference
    var currentUser = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()

    lateinit var postId: String
    lateinit var desc: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_to_vote)
        FirebaseApp.initializeApp(applicationContext);

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        postId = intent.getStringExtra("Id").toString()
        desc = intent.getStringExtra("desc").toString()



        //init list
        images = ArrayList()

        //setup image switcher
        imageSwitcher.setFactory { ImageView(applicationContext) }

        //pick images clicking this button
        pickImagesBtn.setOnClickListener {
            pickImagesIntent()
        }

        //switch to next image clicking this button
        nextBtn.setOnClickListener {
            if (position < images!!.size-1){
                position++
                imageSwitcher.setImageURI(images!![position])
            }
            else{
                //no more images
                Toast.makeText(this, "No More images...", Toast.LENGTH_SHORT).show()
            }
        }

        saveImagesBtn.setOnClickListener {
            uploadImage(images!!, Calendar.getInstance().time.toString())
        }

        //switch to previous image clicking this button
        previousBtn.setOnClickListener {
            if (position > 0){
                position--
                imageSwitcher.setImageURI(images!![position])
            }
            else{
                //no more images
                Toast.makeText(this, "No More images...", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun pickImagesIntent(){
        val  intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image(s)"), PICK_IMAGES_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGES_CODE){

            if (resultCode == Activity.RESULT_OK){

                if (data!!.clipData != null){
                    //picked multiple images
                    //get number of picked images
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count){
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        //add image to list
                        images!!.add(imageUri)
                    }
                    //set first image from list to image switcher
                    imageSwitcher.setImageURI(images!![0])
                    position = 0;
                }
                else{
                    //picked single image
                    val imageUri = data.data
                    //set image to image switcher
                    imageSwitcher.setImageURI(imageUri)
                    position = 0;
                }

            }

        }
    }

    //uploadmultipleimages to firebase storage under a datetime string

    private fun uploadImage(filePath: ArrayList<Uri?>, savetime: String){

        //if(currentUser!=null) {
        receiptRef = db.collection("users").document(currentUser!!.uid)
            .collection("posts").document()
        // }
        for (fp in filePath) {
            if (fp != null) {
                val ref = storageReference?.child(
                    "uploads/" + savetime + "/" + UUID.randomUUID().toString()
                )
                val uploadTask = ref?.putFile(fp!!)

                val urlTask =
                    uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
                            }
                        }else{

                            return@Continuation ref.downloadUrl

                        }
                    })?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downloadUri = task.result
                            addUploadRecordToDb(downloadUri.toString(),receiptRef)
                        } else {
                            // Handle failures
                        }
                    }?.addOnFailureListener {

                    }
            } else {
                Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
            }


        }
        var cuserid = FirebaseAuth.getInstance().currentUser?.uid
        var cusermail = FirebaseAuth.getInstance().currentUser?.email

        var post: Post=Post()
        if (cusermail != null) {
            post.emailaddy=cusermail
        }
        if (cuserid != null) {
            post.userid = cuserid
        }
        post.datecreated = Calendar.getInstance().time
        post.postId = postId
        post.photos=ArrayList()
        post.voters=ArrayList()
        post.desc = desc
        FirebaseFirestore.getInstance().collection("posts").document(postId).set(post, SetOptions.merge()).addOnSuccessListener {
            val inte =  Intent(this,VoteActivity::class.java)
            inte.putExtra("postId",postId)
            startActivity(inte)
        }

        //
    }



    private fun addUploadRecordToDb(uri: String, reref: DocumentReference){

        val data = HashMap<String, Any>()
        data["imageUrl"] = uri

        //if (currentUser != null) {

        var pvote:PostVote= PostVote()
        pvote.url = uri
        pvote.num = 0
        //pvote.id = postId

        FirebaseFirestore.getInstance().collection("posts").document(postId).collection("photos").add(pvote)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Saved to DB", Toast.LENGTH_LONG).show()

            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving to DB", Toast.LENGTH_LONG).show()
            }
        //   }



    }

}