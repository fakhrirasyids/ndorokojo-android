package com.ndorokojo.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ndorokojo.R
import com.ndorokojo.data.models.RasTernakItem
import com.ndorokojo.data.models.TernakItem
import com.ndorokojo.databinding.ItemButtonSeeMoreBinding
import com.ndorokojo.databinding.ItemButtonTernakGridBinding
import com.ndorokojo.ui.main.MainActivity

class TernakSeeMoreAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val listTernak: ArrayList<TernakItem> = arrayListOf()
    private val listBackgroundColor: ArrayList<Int> = arrayListOf()
    var onTernakClick: ((Int, TernakItem, Int) -> Unit)? = null
    var onSeeMoreClick: (() -> Unit)? = null

    private val allBtnTernakList = arrayListOf<CardView>()

    private lateinit var ternakViewHolder: TernakSeeMoreAdapter.ViewHolder
    private lateinit var seeMoreViewHolder: SeeMoreViewHolder

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

        seeMoreViewHolder = SeeMoreViewHolder(
            ItemButtonSeeMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )


        return if (viewType == TERNAK) {
            ternakViewHolder
        } else {
            seeMoreViewHolder
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
        SEE_MORE
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

    inner class SeeMoreViewHolder(
        binding: ItemButtonSeeMoreBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnSeeMore.setOnClickListener {
                onSeeMoreClick?.invoke()
            }
        }
    }

    companion object {
        const val TERNAK = 0
        const val SEE_MORE = 1
    }
}