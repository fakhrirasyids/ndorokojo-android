package com.ndorokojo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ndorokojo.data.models.LivestocksItem
import com.ndorokojo.databinding.ItemTernakRowBinding
import java.text.NumberFormat
import java.util.Locale

class ReportTernakAdapter : RecyclerView.Adapter<ReportTernakAdapter.ViewHolder>() {
    private val listTernak: ArrayList<LivestocksItem> = arrayListOf()
    var onItemClick: ((String, Int) -> Unit)? = null

    fun setList(ternakList: ArrayList<LivestocksItem>) {
        this.listTernak.clear()
        this.listTernak.addAll(ternakList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTernakRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listTernak[position])
    }

    override fun getItemCount() = listTernak.size

    inner class ViewHolder(private var binding: ItemTernakRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ternak: LivestocksItem) {
            with(binding) {
                tvTernakId.text = ternak.code.toString()
                tvStatus.text = ternak.acquiredStatus.toString()

                btnEvent.setOnClickListener {
                    onItemClick?.invoke(ternak.code.toString(), ternak.id!!)
                }
            }
        }
    }
}