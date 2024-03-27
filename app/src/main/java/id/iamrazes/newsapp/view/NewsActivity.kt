package id.iamrazes.newsapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import id.iamrazes.newsapp.GlideApp
import id.iamrazes.newsapp.NewsListAdapter
import id.iamrazes.newsapp.NewsViewModel
import id.iamrazes.newsapp.utils.ViewBindingExt.viewBinding
import id.iamrazes.newsapp.databinding.ActivityNewsBinding
import id.iamrazes.newsapp.utils.Constant
import id.iamrazes.newsapp.utils.Resource
import id.iamrazes.newsapp.utils.ToastUtils
import kotlinx.coroutines.flow.collectLatest
import kotlin.properties.Delegates

class NewsActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityNewsBinding::inflate)
    private val viewModel: NewsViewModel by viewModels()
    private var mAdapterNews by Delegates.notNull<NewsListAdapter>()
    private var mTitle: String? = null
    private var mContent: String? = null
    private var mImage: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        getLiveData()
    }
    private fun initView() = with(binding) {

        viewModel.getAllNews()

        mAdapterNews = NewsListAdapter(this@NewsActivity)

        rvNews.apply {
            setHasFixedSize(false)
            layoutManager =
                LinearLayoutManager(this@NewsActivity, LinearLayoutManager.VERTICAL, false)
            adapter = mAdapterNews
        }

        newsImage.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constant.NEWS_TITLE, mTitle)
            bundle.putString(Constant.NEWS_CONTENT, mContent)
            bundle.putString(Constant.NEWS_IMAGE, mImage)

            val intent = Intent(this@NewsActivity, DetailNewsActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    private fun getLiveData() = with(binding) {
        viewModel.apply {
            lifecycleScope.launchWhenStarted {
                allNews.collectLatest {
                    when (it) {
                        is Resource.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            progressBar.visibility = View.GONE
                            mAdapterNews.submitList(it.data)

                            val shuffledData = it.data?.shuffled()
                            if (!shuffledData.isNullOrEmpty()) {
                                val randomData = shuffledData.random()
                                showNewsRandom(randomData.image.toString(), randomData.title.toString())
                                mTitle = randomData.title.toString()
                                mContent = randomData.content.toString()
                                mImage = randomData.image.toString()
                            }
                        }

                        is Resource.Error -> {
                            progressBar.visibility = View.GONE
                            ToastUtils.showMessage(
                                this@NewsActivity,
                                it.message.toString()
                            )
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun showNewsRandom(url: String, title: String){
        GlideApp.with(this)
            .load(url)
            .into(binding.newsImage)

        binding.titleNewsRandom.text = title
    }

}