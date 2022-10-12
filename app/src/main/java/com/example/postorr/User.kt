package com.example.postorr

import java.util.*
import kotlin.collections.ArrayList

class User {

    var uid: String=""
    var emailaddy: String=""
    var password: String=""
    var username: String=""

}

class PostVote{

    var num: Int=0
    var url: String=""
    var id: String=""
   // lateinit var voted:ArrayList<String>

}

class Person{
     var friended: Boolean=false
    lateinit var uid: String
    lateinit var emailaddy: String
}

class Post{
     var emailaddy: String=""
     var desc: String=""
    lateinit var photos: ArrayList<PostVote>
     var userid: String=""
     var datecreated: Date=Date()
     var postId: String=""
    lateinit var voters:ArrayList<String>

}

