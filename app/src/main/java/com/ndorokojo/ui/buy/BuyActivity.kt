package com.ndorokojo.ui.buy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ndorokojo.R
import com.ndorokojo.data.models.ItemsItem
import com.ndorokojo.databinding.ActivityBuyBinding
import com.ndorokojo.di.Injection
import com.ndorokojo.ui.main.MainActivity
import com.ndorokojo.utils.Constants
import com.ndorokojo.utils.Constants.alertDialogMessage
import com.ndorokojo.utils.Result
import java.text.NumberFormat
import java.util.Locale

class BuyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuyBinding
    private val buyViewModel by viewModels<BuyViewModel> {
        BuyViewModelFactory(
            Injection.provideApiService(this),
            Injection.provideUserPreferences(this)
        )
    }

    private val isFromIndividualBuy by lazy {
        intent.getBooleanExtra(
            IS_FROM_INDIVIDUAL_BUY_EXTRA,
            false
        )
    }

    private lateinit var loadingDialog: AlertDialog
    private var listSelledTernak: ArrayList<ItemsItem>? = null
    private var eventBuy: ItemsItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val inflaterLoading: LayoutInflater = layoutInflater
        val loadingAlert = AlertDialog.Builder(this)
            .setView(inflaterLoading.inflate(R.layout.custom_loading_dialog, null))
            .setCancelable(false)
        loadingDialog = loadingAlert.create()

        eventBuy = intent.getParcelableExtra(EVENT_BUY)

        observeLoading()
        toggleRasTernak()
        setData()
        observeAll()

        setListeners()
    }

    private fun observeLoading() {
        buyViewModel.isLoadingKandang.observe(this@BuyActivity) {
            if (it) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }
    }

    private fun toggleRasTernak() {
        if (eventBuy == null) {
            if (listSelledTernak!![0].livestockType?.level == 2) {
                binding.edTernakRasLayout.visibility = View.VISIBLE
            } else {
                binding.edTernakRasLayout.visibility = View.GONE
            }
        }
//        } else {
//            if (eventBuy?.livestockType?.level == "2") {
//                binding.edTernakRasLayout.visibility = View.VISIBLE
//            } else {
//                binding.edTernakRasLayout.visibility = View.GONE
//            }
//            binding.edTernakLayout.isEnabled = false
//            binding.edTernak.isEnabled = false
//        }
    }

    private fun setData() {
        if (eventBuy == null) {
//            if (isFromIndividualBuy) {
//            val listTernakString = arrayListOf<String>()
//            for (item in listSelledTernak!!) {
//                listTernakString.add(item.code.toString())
//            }
//
//            val ternakAdapter = ArrayAdapter(
//                this@BuyActivity,
//                android.R.layout.simple_spinner_dropdown_item,
//                listTernakString
//            )
//            binding.edTernak.apply {
//                setAdapter(ternakAdapter)
//                setOnItemClickListener { _, _, position, _ ->
//                    buyViewModel.buyTernakEvent.postValue(listSelledTernak!![position])
//                }
//            }
//            } else {
//                buyViewModel.listKandang.observe(this@BuyActivity) { kandangList ->
//                    if (kandangList != null) {
//                        if (buyViewModel.selectedKandangId.value != null) {
//                            for (item in kandangList) {
//                                if (buyViewModel.selectedKandangId.value == item.id) {
//                                    binding.edTernak.setText(item.name)
//                                }
//                            }
//                        } else {
//                            binding.edTernak.hint = "Pilih Kandang"
//                        }
//
//                        binding.edTernak.isEnabled = true
//                        val listLimbahString = arrayListOf<String>()
//                        for (item in kandangList) {
//                            listLimbahString.add(item.name.toString())
//                        }
//
//                        val kandangAdapter = ArrayAdapter(
//                            this@BuyActivity,
//                            android.R.layout.simple_spinner_dropdown_item,
//                            listLimbahString
//                        )
//                        binding.edTernak.apply {
//                            setAdapter(kandangAdapter)
//                            setOnItemClickListener { _, _, position, _ ->
//                                buyViewModel.selectedKandangId.postValue(kandangList[position].id!!)
//                            }
//                        }
//                    } else {
//                        binding.edTernak.isEnabled = false
//                        binding.edTernak.hint = "Pilih Kandang"
//                    }
//                }
//            }
            if (MainActivity.kandangList.isNotEmpty()) {
                val listKandangString = arrayListOf<String>()
                for (item in MainActivity.kandangList) {
                    listKandangString.add(item.name.toString())
                }

                val kandangAdapter = ArrayAdapter(
                    this@BuyActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    listKandangString
                )
                binding.edKandang.apply {
                    setAdapter(kandangAdapter)
                    setOnItemClickListener { _, _, position, _ ->
                        buyViewModel.selectedKandangId.postValue(MainActivity.kandangList[position].id!!)
                    }
                }
            }
        } else {
            buyViewModel.buyTernakEvent.postValue(eventBuy)
            buyViewModel.listKandang.observe(this@BuyActivity) { kandangList ->
                if (kandangList != null) {
                    if (buyViewModel.selectedKandangId.value != null) {
                        for (item in kandangList) {
                            if (buyViewModel.selectedKandangId.value == item.id) {
                                binding.edKandang.setText(item.name)
                            }
                        }
                    } else {
                        binding.edKandang.hint = "Pilih Kandang untuk Ternak"
                    }

                    binding.edKandang.isEnabled = true
                    val listKandangString = arrayListOf<String>()
                    for (item in kandangList) {
                        listKandangString.add(item.name.toString())
                    }

                    val kandangAdapter = ArrayAdapter(
                        this@BuyActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        listKandangString
                    )
                    binding.edKandang.apply {
                        setAdapter(kandangAdapter)
                        setOnItemClickListener { _, _, position, _ ->
                            buyViewModel.selectedKandangId.postValue(kandangList[position].id!!)
                        }
                    }
                } else {
                    binding.edKandang.isEnabled = false
                    binding.edKandang.hint = "Pilih Kandang untuk Ternak"
                }
            }
        }
    }

    private fun observeAll() {
        buyViewModel.apply {
            buyTernakEvent.observe(this@BuyActivity) { event ->
                if (event != null) {
                    binding.apply {
//                        if (isFromEvent) {
                        edTernak.setText(event.code)
                        if (event.livestockType?.level == 2) {
                            var ternakName = ""
                            for (item in MainActivity.allTernakItem) {
                                if (item.id == Integer.parseInt(event.livestockType!!.parentTypeId.toString())) {
                                    ternakName = item.livestockType.toString()
                                    buyViewModel.getListKandang(Integer.parseInt(event.livestockType!!.parentTypeId.toString()))
                                    break
                                }
                            }
                            edTernakType.setText(ternakName)
                            edTernakRasEvent.setText(event.livestockType.livestockType)

                            if (ternakName.isNotEmpty()) {
                                binding.rasLayout.isVisible = true
                            }
                        } else {
                            buyViewModel.getListKandang(event.livestockType?.id!!)
                            edTernakType.setText(event.livestockType.livestockType)
                        }
//                        } else {
//                            buyViewModel.getListKandang(event.livestockType?.id!!)
//                            edTernakType.setText(MainActivity.ternak?.livestockType)
//                            if (listSelledTernak!![0].livestockType?.level == "2") {
//                                edTernakRas.setText(event.livestockType?.livestockType)
//                            }
//                        }

                        Glide.with(this@BuyActivity)
                            .load(event.soldImage)
                            .placeholder(
                                ContextCompat.getDrawable(
                                    this@BuyActivity,
                                    R.drawable.ndorokojo_logo
                                )
                            )
                            .transition(
                                DrawableTransitionOptions.withCrossFade()
                            )
                            .into(binding.ivTernak)



                        edNamaPenjual.setText(event.kandang?.farmer?.fullname)
                        edNohpPenjual.setText(event.kandang?.farmer?.phone)
                        edJenisKelamin.text = event.gender
                        edUsiaTernak.text = event.age
                        edHargaPenawaran.setText(
                            StringBuilder(
                                "Rp ${
                                    formatNumber(
                                        event.soldProposedPrice.toString().toDouble()
                                    )
                                }"
                            )
                        )
                    }
                }
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            toolbar.setNavigationOnClickListener { finish() }

            btnSave.setOnClickListener {
                if (isValid()) {
                    buyViewModel.buyDealPrice.postValue(Integer.parseInt(edPenawaran.text.toString()))

                    val builder = AlertDialog.Builder(this@BuyActivity)
                    builder.setCancelable(false)

                    with(builder)
                    {
                        setTitle("Apakah anda yakin untuk mengajukan tawaran ini?")
                        setMessage("Cek dan pastikan ulang semua data sudah benar")

                        setPositiveButton("Benar") { dialog, _ ->
                            dialog.dismiss()

                            if (binding.edNamaPenjual.text.toString() == buyViewModel.getFullname()) {
                                alertDialogMessage(
                                    this@BuyActivity,
                                    "Penjual merupakan diri anda sendiri",
                                    "Gagal Buy Ternak"
                                )
                            } else {
                                buyViewModel.buyTernak().observe(this@BuyActivity) { result ->
                                    if (result != null) {
                                        when (result) {
                                            is Result.Loading -> {
                                                loadingDialog.show()
                                            }

                                            is Result.Success -> {
                                                loadingDialog.dismiss()
                                                val builder = AlertDialog.Builder(this@BuyActivity)
                                                builder.setCancelable(false)

                                                with(builder)
                                                {
                                                    setTitle("Berhasil Mengajukan Tawaran Beli!")
                                                    setMessage("Silahkan tunggu konfirmasi dari penjual.")
                                                    setPositiveButton("OK") { dialog, _ ->
                                                        dialog.dismiss()
                                                        val intent = Intent()
                                                        setResult(RESULT_OK, intent)
                                                        finish()
                                                    }
                                                    show()
                                                }
                                            }

                                            is Result.Error -> {
                                                loadingDialog.dismiss()
                                                alertDialogMessage(
                                                    this@BuyActivity,
                                                    result.error,
                                                    "Gagal Buy Ternak"
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        setNegativeButton("Salah") { dialog, _ ->
                            dialog.dismiss()
                        }
                        show()
                    }
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

    private fun isValid() = if (binding.edTernak.text.isNullOrEmpty()) {
        alertDialogMessage(this@BuyActivity, "Masukkan Ternak dengan benar!")
//            binding.edTernakRasLayout.error = "Masukkan Ras Ternak dengan benar!"
        false
    } else if (binding.edPenawaran.text.isNullOrEmpty()) {
        alertDialogMessage(this@BuyActivity, "Masukkan Tawar Harga dengan benar!")
//            binding.edPakanLayout.error = "Masukkan Pakan Ternak dengan benar!"
        false
    } else if (binding.edKandang.text.isNullOrEmpty()) {
        alertDialogMessage(
            this@BuyActivity,
            "Masukkan Kandang untuk Ternak dengan benar!"
        )
//            binding.edLimbahLayout.error = "Masukkan Limbah Ternak dengan benar!"
        false
    } else {
        true
    }


    companion object {
        const val TERNAK_BUY_LIST = "ternak_buy_list"
        const val EVENT_BUY = "event_buy"
        const val IS_FROM_INDIVIDUAL_BUY_EXTRA = "is_from_individual_buy_extra"
        const val IS_FROM_EXTRA = "is_from_extra"
        const val IS_FROM_SEARCH = "is_from_search"
    }
}