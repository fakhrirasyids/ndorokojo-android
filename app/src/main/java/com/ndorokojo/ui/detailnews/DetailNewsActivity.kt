package com.ndorokojo.ui.detailnews

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ndorokojo.R
import com.ndorokojo.databinding.ActivityDetailNewsBinding
import com.ndorokojo.di.Injection
import com.ndorokojo.ui.main.MainViewModel
import com.ndorokojo.ui.main.MainViewModelFactory

class DetailNewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailNewsBinding
    private val id by lazy { intent.getIntExtra(KEY_NEWS_ID, -1) }
    private val isFromBrebes by lazy { intent.getBooleanExtra(KEY_IS_FROM_BREBES, false) }
    private val detailNewsActivity by viewModels<DetailNewsViewModel> {
        DetailNewsViewModelFactory(
            Injection.provideApiService(this),
            id,
            isFromBrebes
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeLoading()
        observeNewsDetail()
        observeError()
        observeMessage()

        setListeners()
    }

    private fun observeLoading() {
        detailNewsActivity.isLoading.observe(this@DetailNewsActivity) {
            showLoading(it)
        }
    }

    private fun observeNewsDetail() {
        detailNewsActivity.newsDetail.observe(this@DetailNewsActivity) { detail ->
            if (detail != null) {
                binding.apply {
                    Glide.with(this@DetailNewsActivity).load(detail.thumbnail)
                        .placeholder(
                            ContextCompat.getDrawable(
                                applicationContext,
                                R.drawable.ndorokojo_logo
                            )
                        )
                        .transition(
                            DrawableTransitionOptions.withCrossFade()
                        )
                        .into(ivDetail)

                    tvTitle.text = detail.title

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tvContent.text = Html.fromHtml(detail.content, Html.FROM_HTML_MODE_LEGACY)
                    } else {
                        tvContent.text = detail.content
                    }
                }
            }
        }
    }

    private fun observeError() {
        detailNewsActivity.isError.observe(this@DetailNewsActivity) {
            showError(it)
        }
    }

    private fun observeMessage() {
        detailNewsActivity.responseMessage.observe(this@DetailNewsActivity) {
            binding.tvErrorMessage.text = StringBuilder("Error: $it")
        }
    }

    private fun setListeners() {
        binding.apply {
            toolbar.setNavigationOnClickListener { finish() }
            btnRetry.setOnClickListener {
                if (isFromBrebes) {
                    detailNewsActivity.getBrebesDetailList(id)
                } else {
                    detailNewsActivity.getFinanceDetailList(id)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressbar.isVisible = isLoading
            layoutContent.isVisible = !isLoading
        }
    }

    private fun showError(isError: Boolean) {
        binding.apply {
            layoutError.isVisible = isError
            layoutContent.isVisible = !isError
        }
    }

    companion object {
        const val KEY_NEWS_ID = "key_news_id"
        const val KEY_IS_FROM_BREBES = "key_is_from_brebes"
    }
}