package id.iamrazes.newsapp.db

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseDb {

    private val newsCollectionRef = Firebase.firestore.collection("News")

    fun getAllNews() = newsCollectionRef
    fun getLatestNews() = newsCollectionRef.orderBy("date", Query.Direction.ASCENDING)
}