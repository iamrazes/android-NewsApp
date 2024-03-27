package id.iamrazes.newsapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.iamrazes.newsapp.db.FirebaseDb
import id.iamrazes.newsapp.model.News
import id.iamrazes.newsapp.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class NewsViewModel : ViewModel(){
    private val firebaseDatabase: FirebaseDb by lazy { FirebaseDb() }

    private val _allNews = MutableStateFlow<Resource<List<News>>>(Resource.Unspecified())
    val allNews = _allNews.asStateFlow()

    private val _latestNews = MutableStateFlow<Resource<List<News>>>(Resource.Unspecified())
    val latestNews = _latestNews.asStateFlow()


    fun getAllNews() {
        viewModelScope.launch {
            _allNews.emit(Resource.Loading())
        }
        firebaseDatabase.getAllNews().get()
            .addOnSuccessListener {
                val data = it.toObjects(News::class.java)
                viewModelScope.launch {
                    _allNews.emit(Resource.Success(data))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _allNews.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun getLatesNews() {
        viewModelScope.launch {
            _latestNews.emit(Resource.Loading())
        }
        firebaseDatabase.getLatestNews().get()
            .addOnSuccessListener {
                val data = it.toObjects(News::class.java)
                viewModelScope.launch {
                    _latestNews.emit(Resource.Success(data))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _latestNews.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}