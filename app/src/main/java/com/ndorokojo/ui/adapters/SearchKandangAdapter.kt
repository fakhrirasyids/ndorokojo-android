package com.ndorokojo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ndorokojo.data.models.SearchedKandangItem
import com.ndorokojo.databinding.ItemKandangColumnBinding

class SearchKandangAdapter : RecyclerView.Adapter<SearchKandangAdapter.ViewHolder>() {
    private val listEvent: ArrayList<SearchedKandangItem> = arrayListOf()
    var onItemClick: ((Int) -> Unit)? = null

    fun setList(eventList: ArrayList<SearchedKandangItem>) {
        this.listEvent.clear()
        this.listEvent.addAll(eventList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemKandangColumnBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listEvent[position])
    }

    override fun getItemCount() = listEvent.size

    inner class ViewHolder(private var binding: ItemKandangColumnBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(kandang: SearchedKandangItem) {
            with(binding) {
                tvTitle.text = StringBuilder("Nama Kandang ${kandang.livestockType} : ")
                tvNamaKandang.text = kandang.name
                tvTernakAvailable.text = kandang.statistic?.available.toString()

                btnEvent.setOnClickListener {
                    onItemClick?.invoke(kandang.id!!)
                }
            }
        }
    }
}