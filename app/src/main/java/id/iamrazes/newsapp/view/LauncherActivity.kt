package id.iamrazes.newsapp.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import id.iamrazes.newsapp.R

@Suppress("DEPRECATION")
class LauncherActivity : AppCompatActivity() {

    private val SPLASH_DELAY_MS = 3000L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        supportActionBar?.hide()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            initialCheck()
        }, SPLASH_DELAY_MS)
    }

    private fun initialCheck() {
        if (!isFinishing && isInternetAvailable()) {
            startActivity(Intent(this@LauncherActivity, NewsActivity::class.java))
            finish()
        } else if (!isFinishing) {
            showBadConnectionDialog()
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun showBadConnectionDialog() {
        AlertDialog.Builder(this)
            .setTitle("Bad Connection, Try Again!")
            .setMessage("Check Your Internet Connectivity.")
            .setPositiveButton("OK") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                finishAffinity()
            }
            .setCancelable(false)
            .show()
    }
}