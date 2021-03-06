package gus.android.mvvmhilt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import gus.android.mvvmhilt.model.Blog
import gus.android.mvvmhilt.ui.MainStateEvent
import gus.android.mvvmhilt.ui.MainViewModel
import gus.android.mvvmhilt.utlil.DataState
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG = "AppDebug"

    private val viewModel: MainViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        subscribeObservers()
        viewModel.setStateEvent(MainStateEvent.GetBlogEvents)
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(this, Observer { dataState ->
            when(dataState) {
                is DataState.Success<List<Blog>> -> {
                    displayProgressBar(false)
                    appendBlogTitles(dataState.data)
                }
                is DataState.Error -> {
                    displayProgressBar(false)
                    displayError(dataState.exception.message)
                }
                is DataState.Loading -> {
                    displayProgressBar(true)
                }
            }

        })
    }

    private fun displayError(message: String?) {
        if(message != null) {
            text.text = message
        } else {
            text.text = "Unknown error"
        }
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        progress_bar.visibility = if(isDisplayed) View.VISIBLE else View.GONE
    }

    private fun appendBlogTitles(blogs: List<Blog>) {
        val sb = StringBuilder()
        for(blog in blogs) {
            sb.append(blog.title + '\n')
        }
        text.text = sb.toString()
    }
}