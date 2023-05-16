package com.example.newsapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.utils.Constants.BUSINESS
import com.example.newsapp.utils.Constants.ENTERTAINMENT
import com.example.newsapp.utils.Constants.HEALTH
import com.example.newsapp.utils.Constants.HOME
import com.example.newsapp.utils.Constants.SCIENCE
import com.example.newsapp.utils.Constants.SPORTS
import com.example.newsapp.utils.Constants.TECHNOLOGY

class MainActivity : ComponentActivity() {
    // Tabs Title
    private val newsCategories = arrayOf(
        HOME, BUSINESS,
        ENTERTAINMENT, SCIENCE,
        SPORTS, TECHNOLOGY, HEALTH
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }
    }

    // Check internet connection
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // For 29 api or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                    ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            // For below 29 api
            if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
                return true
            }
        }
        return false
    }

    companion object {
        var generalNews: ArrayList<NewsModel> = ArrayList()
        var entertainmentNews: MutableList<NewsModel> = mutableListOf()
        var businessNews: MutableList<NewsModel> = mutableListOf()
        var healthNews: MutableList<NewsModel> = mutableListOf()
        var scienceNews: MutableList<NewsModel> = mutableListOf()
        var sportsNews: MutableList<NewsModel> = mutableListOf()
        var techNews: MutableList<NewsModel> = mutableListOf()
        var apiRequestError = false
        var errorMessage = "error"
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NewsAppTheme {
        Greeting("Android")
    }
}
