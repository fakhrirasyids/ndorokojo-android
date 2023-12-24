package com.ndorokojo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ndorokojo.R
import com.ndorokojo.data.models.Event
import com.ndorokojo.databinding.ItemNewsRowBinding
import com.ndorokojo.ui.main.MainActivity
import java.text.NumberFormat
import java.util.Locale

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    private val listEvent: ArrayList<Event> = arrayListOf()
    var onItemClick: ((Event) -> Unit)? = null

    fun setList(eventList: ArrayList<Event>) {
        this.listEvent.clear()
        this.listEvent.addAll(eventList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNewsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listEvent[position])
    }

    override fun getItemCount() = listEvent.size

    inner class ViewHolder(private var binding: ItemNewsRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event) {
            with(binding) {
                Glide.with(root).load(event.livestockType?.image)
                    .transition(
                        DrawableTransitionOptions.withCrossFade()
                    )
                    .into(ivLogoTernak)

                if (event.livestockType?.level == "2") {
                    if (MainActivity.allTernakItem.isNotEmpty()) {
                        var ternakName = ""

                        for (item in MainActivity.allTernakItem) {
                            if (item.id == Integer.parseInt(event.livestockType.parentTypeId.toString())) {
                                ternakName = item.livestockType.toString()
                            }
                        }
                        tvTernak.text =
                            StringBuilder("$ternakName ${event.livestockType.livestockType} 1 ekor")
                    }
                } else {
                    tvTernak.text = StringBuilder("${event.livestockType?.livestockType} 1 ekor")
                }

                tvPrice.text = StringBuilder(
                    "Rp ${
                        formatNumber(
                            event.soldProposedPrice.toString().toDouble()
                        )
                    }"
                )
                tvPenjual.text = event.kandang?.farmer?.fullname

                btnEvent.setOnClickListener {
                    onItemClick?.invoke(event)
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