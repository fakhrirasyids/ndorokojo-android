package com.ndorokojo.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ndorokojo.R
import com.ndorokojo.data.models.TernakItem
import com.ndorokojo.databinding.ItemButtonSeeLessBinding
import com.ndorokojo.databinding.ItemButtonSeeMoreBinding
import com.ndorokojo.databinding.ItemButtonTernakGridBinding
import com.ndorokojo.ui.main.MainActivity

class TernakSeeLessAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val listTernak: ArrayList<TernakItem> = arrayListOf()
    var onTernakClick: ((Int, TernakItem, Int) -> Unit)? = null
    var onSeeLessClick: (() -> Unit)? = null

    private val allBtnTernakList = arrayListOf<CardView>()

    private lateinit var ternakViewHolder: TernakSeeLessAdapter.ViewHolder
    private lateinit var seeLessViewHolder: SeeLessViewHolder

    fun setList(ternakList: ArrayList<TernakItem>) {
        this.listTernak.clear()
        this.allBtnTernakList.clear()
        this.listTernak.addAll(ternakList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        ternakViewHolder = ViewHolder(
            ItemButtonTernakGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

        seeLessViewHolder = SeeLessViewHolder(
            ItemButtonSeeLessBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

        return when (viewType) {
            TERNAK -> {
                ternakViewHolder
            }

            else -> {
                seeLessViewHolder
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (listTernak.isNotEmpty()) {
            if (getItemViewType(position) == TERNAK) {
                allBtnTernakList.add(ternakViewHolder.binding.btnTernak)

                ternakViewHolder.bind(listTernak[position], position)
            }
        }
    }

    override fun getItemCount() = listTernak.size + 1


    override fun getItemViewType(position: Int) = if (position == listTernak.size) {
        SEE_LESS
    } else {
        TERNAK
    }


    inner class ViewHolder(var binding: ItemButtonTernakGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ternak: TernakItem, position: Int) {
            with(binding) {
                Glide.with(root)
                    .load(ternak.image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivTernak)

                tvTernakName.text = ternak.livestockType

                btnTernak.setOnClickListener {
                    onTernakClick?.invoke(ternak.id!!, ternak, position)
                }
            }
        }
    }

    fun changeBackgroundColor(context: Context, position: Int) {
        changeAllBackgroundWhite(context)

        allBtnTernakList[position].setCardBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.gray
            )
        )
    }

    fun changeAllBackgroundWhite(context: Context) {
        for (btnTernakView in allBtnTernakList) {
            btnTernakView.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.white
                )
            )
        }
    }

    inner class SeeLessViewHolder(
        binding: ItemButtonSeeLessBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnSeeLess.setOnClickListener {
                onSeeLessClick?.invoke()
            }
        }
    }

    companion object {
        const val TERNAK = 0
        const val SEE_LESS = 2
    }
}