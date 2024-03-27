package id.iamrazes.newsapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import id.iamrazes.newsapp.R

object Constant {
    const val NEWS_IMAGE = "news_image"
    const val NEWS_TITLE = "news_title"
    const val NEWS_LINK = "news_link"
    const val NEWS_ID = "article_id"
    const val NEWS_CATEGORY = "news_category"
    const val NEWS_DATE = "news_date"
    const val NEWS_CONTENT = "news_content"

    fun glideRequestOption(context: Context, error: Int): RequestOptions {
        val format = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            Bitmap.CompressFormat.WEBP_LOSSLESS else Bitmap.CompressFormat.PNG

        return RequestOptions()
            .encodeFormat(format)
            .placeholder(getProgressImage(context))
            .error(error)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
    }

    private fun getProgressImage(context: Context?): CircularProgressDrawable? {
        val progressImage = context?.applicationContext?.let { CircularProgressDrawable(it) }
        progressImage?.setColorSchemeColors(
            R.color.colorAccent, R.color.colorAccent,
        )
        progressImage?.centerRadius = 30f
        progressImage?.strokeWidth = 7f
        progressImage?.start()
        return progressImage
    }


}