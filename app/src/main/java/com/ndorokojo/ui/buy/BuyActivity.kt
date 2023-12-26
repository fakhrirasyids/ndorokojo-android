package com.ndorokojo.ui.buy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.ndorokojo.R
import com.ndorokojo.data.models.Event
import com.ndorokojo.data.models.FarmerEvent
import com.ndorokojo.data.models.Kandang
import com.ndorokojo.data.models.KandangEvent
import com.ndorokojo.data.models.LivestockType
import com.ndorokojo.data.models.SearchedEventItem
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
        BuyViewModelFactory.getInstance(
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
    private var listSelledTernak: ArrayList<Event>? = null
    private var eventBuy: Event? = null
    private var searchedEventBuy: SearchedEventItem? = null

    private var isFromSearch: Boolean = false
    private var isFromEvent: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val inflaterLoading: LayoutInflater = layoutInflater
        val loadingAlert = AlertDialog.Builder(this)
            .setView(inflaterLoading.inflate(R.layout.custom_loading_dialog, null))
            .setCancelable(false)
        loadingDialog = loadingAlert.create()

        isFromSearch = intent.getBooleanExtra(IS_FROM_SEARCH, false)
        isFromEvent = intent.getBooleanExtra(IS_FROM_EXTRA, false)
        if (isFromSearch) {
            searchedEventBuy = intent.getParcelableExtra<SearchedEventItem>(EVENT_BUY)

            val event = Event()
            event.id = searchedEventBuy?.id
            event.code = searchedEventBuy?.code

            val livestockType = LivestockType()

            livestockType.id = searchedEventBuy?.livestockType?.id
            livestockType.level = searchedEventBuy?.livestockType?.level
            livestockType.livestockType = searchedEventBuy?.livestockType?.livestockType
            livestockType.parentTypeId =
                searchedEventBuy?.livestockType?.parentTypeId.toString()

            event.livestockType = livestockType
            val kandang = KandangEvent()
            val farmer = FarmerEvent()
            farmer.fullname = searchedEventBuy?.kandang?.farmer?.fullname
            kandang.farmer = farmer
            event.kandang = kandang
            event.soldProposedPrice = searchedEventBuy?.soldProposedPrice
            event.isMine = searchedEventBuy?.isMine
            
            eventBuy = event
        } else {
            if (isFromEvent) {
                eventBuy = intent.getParcelableExtra<Event>(EVENT_BUY)
            } else {
                listSelledTernak = intent.getParcelableArrayListExtra(TERNAK_BUY_LIST)!!
            }
        }

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
            if (listSelledTernak!![0].livestockType?.level == "2") {
                binding.edTernakRasLayout.visibility = View.VISIBLE
            } else {
                binding.edTernakRasLayout.visibility = View.GONE
            }
        } else {
            if (eventBuy?.livestockType?.level == "2") {
                binding.edTernakRasLayout.visibility = View.VISIBLE
            } else {
                binding.edTernakRasLayout.visibility = View.GONE
            }
            binding.edTernakLayout.isEnabled = false
            binding.edTernak.isEnabled = false
        }
    }

    private fun setData() {
        if (eventBuy == null) {
//            if (isFromIndividualBuy) {
            val listTernakString = arrayListOf<String>()
            for (item in listSelledTernak!!) {
                listTernakString.add(item.code.toString())
            }

            val ternakAdapter = ArrayAdapter(
                this@BuyActivity,
                android.R.layout.simple_spinner_dropdown_item,
                listTernakString
            )
            binding.edTernak.apply {
                setAdapter(ternakAdapter)
                setOnItemClickListener { _, _, position, _ ->
                    buyViewModel.buyTernakEvent.postValue(listSelledTernak!![position])
                }
            }
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
                        binding.edKandang.hint = "Pilih Kandang"
                    }

                    binding.edKandang.isEnabled = true
                    val listKandangString = arrayListOf<String>()
                    for (item in kandangList) {
                        listKandangString.add(item.name.toString())
                    }
                    Log.e("NGEODNGEOD", "setData: $listKandangString")

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
                    binding.edKandang.hint = "Pilih Kandang"
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
                        if (event.livestockType?.level == "2") {
                            var ternakName = ""
                            for (item in MainActivity.allTernakItem) {
                                if (item.id == Integer.parseInt(event.livestockType!!.parentTypeId.toString())) {
                                    ternakName = item.livestockType.toString()
                                    buyViewModel.getListKandang(Integer.parseInt(event.livestockType!!.parentTypeId.toString()))
                                    break
                                }
                            }
                            edTernakType.setText(ternakName)
                            edTernakRas.setText(event.livestockType?.livestockType)
                        } else {
                            buyViewModel.getListKandang(event.livestockType?.id!!)
                            edTernakType.setText(event.livestockType?.livestockType)
                        }
//                        } else {
//                            buyViewModel.getListKandang(event.livestockType?.id!!)
//                            edTernakType.setText(MainActivity.ternak?.livestockType)
//                            if (listSelledTernak!![0].livestockType?.level == "2") {
//                                edTernakRas.setText(event.livestockType?.livestockType)
//                            }
//                        }

                        edNamaPenjual.setText(event.kandang?.farmer?.fullname)
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
                        setTitle("Apakah semua data sudah benar?")
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
                                                    setTitle("Berhasil Buy Ternak!")
                                                    setMessage("Silahkan melanjutkan untuk mengolah data ternak")
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
                                                Constants.alertDialogMessage(
                                                    this@BuyActivity,
                                                    if (result.error == "HTTP 404 ") "Ternak sudah pernah dijual, Silahkan pilih ternak lain!" else result.error,
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
        Constants.alertDialogMessage(this@BuyActivity, "Masukkan Ternak dengan benar!")
//            binding.edTernakRasLayout.error = "Masukkan Ras Ternak dengan benar!"
        false
    } else if (binding.edPenawaran.text.isNullOrEmpty()) {
        Constants.alertDialogMessage(this@BuyActivity, "Masukkan Tawar Harga dengan benar!")
//            binding.edPakanLayout.error = "Masukkan Pakan Ternak dengan benar!"
        false
    } else if (binding.edKandang.text.isNullOrEmpty()) {
        Constants.alertDialogMessage(
            this@BuyActivity,
            "Masukkan Kandan untuk Ternak dengan benar!"
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