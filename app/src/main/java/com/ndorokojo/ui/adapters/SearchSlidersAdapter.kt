package com.ndorokojo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ndorokojo.R
import com.ndorokojo.data.models.News
import com.ndorokojo.data.models.SearchedNewsItem
import com.ndorokojo.databinding.ItemSlidersRowBinding

class SearchSlidersAdapter : RecyclerView.Adapter<SearchSlidersAdapter.ViewHolder>() {
    private val listNews: ArrayList<SearchedNewsItem> = arrayListOf()
    var onItemClick: ((Int) -> Unit)? = null

    fun setList(newsList: ArrayList<SearchedNewsItem>) {
        this.listNews.clear()
        this.listNews.addAll(newsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSlidersRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listNews[position])
    }

    override fun getItemCount() = listNews.size

    inner class ViewHolder(private var binding: ItemSlidersRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(news: SearchedNewsItem) {
            with(binding) {
                Glide.with(root).load(news.thumbnail)
                    .placeholder(ContextCompat.getDrawable(root.context, R.drawable.ndorokojo_logo))
                    .transition(
                        DrawableTransitionOptions.withCrossFade()
                    )
                    .into(ivSlider)

                tvTitle.text = news.title

                btnEvent.setOnClickListener {
                    onItemClick?.invoke(news.id!!)
                }
            }
        }
    }

}