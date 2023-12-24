package com.ndorokojo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ndorokojo.R
import com.ndorokojo.data.models.Event
import com.ndorokojo.data.models.Kandang
import com.ndorokojo.databinding.ItemKandangColumnBinding
import com.ndorokojo.databinding.ItemNewsRowBinding
import com.ndorokojo.ui.main.MainActivity
import java.text.NumberFormat
import java.util.Locale

class KandangAdapter : RecyclerView.Adapter<KandangAdapter.ViewHolder>() {
    private val listEvent: ArrayList<Kandang> = arrayListOf()
    var onItemClick: ((Int) -> Unit)? = null

    fun setList(eventList: ArrayList<Kandang>) {
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
        fun bind(event: Kandang) {
            with(binding) {
                tvNamaKandang.text = event.name
                tvTernakAvailable.text = event.livestocksCount

                btnEvent.setOnClickListener {
                    onItemClick?.invoke(Integer.parseInt(event.livestocksCount.toString()))
                }
            }
        }
    }

    fun formatNumber(number: Double): String {
        val formatter = NumberFormat.getInstance(
            Locale(
                "id",
                "ID"
            )
        )
        return formatter.format(number)
    }
}