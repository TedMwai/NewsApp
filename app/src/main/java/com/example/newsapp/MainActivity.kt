package com.example.newsapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.architecture.NewsViewModel
import com.example.newsapp.retrofit.RetrofitHelper
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.utils.Constants.BUSINESS
import com.example.newsapp.utils.Constants.ENTERTAINMENT
import com.example.newsapp.utils.Constants.GENERAL
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

    private lateinit var viewModel: NewsViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NewsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NewsList(generalNews = generalNews)
                }
            }
        }
        //create a view model instance
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        //fetch news from API
        requestNews(newsCategory = GENERAL, newsData = generalNews)
        requestNews(newsCategory = ENTERTAINMENT, newsData = entertainmentNews)
    }

    //create a function to fetch news from API
    private fun requestNews(newsCategory: String, newsData: MutableList<NewsModel>) {
        viewModel.getNews(category = newsCategory)?.observe(this) { news ->
            newsData.clear()
            newsData.addAll(news)
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

    //create a composable function to display news list

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

//create a composable function to display news list
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsList(generalNews: ArrayList<NewsModel>) {
    LazyColumn(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(10.dp)) {
        item {
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 25.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(id = R.string.app_name), style = MaterialTheme.typography.titleMedium)
            }
        }
    }
    //display news list
    for (news in generalNews) {
        NewsCard(news = news)
    }
}

//create a composable function to show a news card
@Composable
fun NewsCard(news: NewsModel) {
    //display news card
    Card(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "News Image",
                modifier = Modifier
                    .padding(10.dp)
                    .size(100.dp))
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = news.headLine, style = MaterialTheme.typography.titleSmall)
                news.description?.let { Text(text = it, style = MaterialTheme.typography.bodySmall) }
            }
            }
        }
    }

/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NewsAppTheme {
        Greeting("Android")
    }
}
*/

@Preview(showBackground = true)
@Composable
fun NewsCardPreview() {
    NewsAppTheme {
        NewsList(generalNews = MainActivity.generalNews)
    }
}