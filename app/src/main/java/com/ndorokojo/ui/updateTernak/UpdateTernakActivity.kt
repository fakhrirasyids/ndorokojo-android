package com.ndorokojo.ui.updateTernak

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.DatePicker
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.ndorokojo.R
import com.ndorokojo.databinding.ActivityUpdateTernakBinding
import com.ndorokojo.di.Injection
import com.ndorokojo.ui.detailkandang.DetailKandangActivity
import com.ndorokojo.ui.detailkandang.DetailKandangViewModel
import com.ndorokojo.ui.detailkandang.DetailKandangViewModelFactory
import com.ndorokojo.utils.Constants.alertDialogMessage
import com.ndorokojo.utils.MonthYearPickerDialog
import com.ndorokojo.utils.Result

class UpdateTernakActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding: ActivityUpdateTernakBinding

    private val ternakName by lazy { intent.getStringExtra(EXTRA_TERNAK_NAME) }
    private val ternakId by lazy { intent.getIntExtra(EXTRA_TERNAK_ID, -1) }

    private val isFromJual by lazy { intent.getBooleanExtra(EXTRA_IS_FROM_JUAL, false) }


    private val updateTernakViewModel by viewModels<UpdateTernakViewModel> {
        UpdateTernakViewModelFactory(
            Injection.provideApiService(this)
        )
    }

    private lateinit var loadingDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateTernakBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (isFromJual) {
            binding.toolbar.title = "Update Ternak ke Terjual"
            binding.edMonthYear.hint = "Bulan/Tahun Terjual"
        } else {
            binding.toolbar.title = "Update Ternak ke Mati"
            binding.edMonthYear.hint = "Bulan/Tahun Mati"
        }

        val inflater: LayoutInflater = layoutInflater
        val loadingAlert = AlertDialog.Builder(this@UpdateTernakActivity)
            .setView(inflater.inflate(R.layout.custom_loading_dialog, null))
            .setCancelable(true)
        loadingDialog = loadingAlert.create()

        binding.edTernak.setText(ternakName)

        observeLoading()
        observeResponseMessage()

        setListeners()
    }

    private fun observeLoading() {
        updateTernakViewModel.isLoading.observe(this@UpdateTernakActivity) {
            if (it) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }
    }

    private fun observeResponseMessage() {
        updateTernakViewModel.responseMessage.observe(this@UpdateTernakActivity) {
            alertDialogMessage(this@UpdateTernakActivity, "Error : $it", "Gagal Update Ternak")
        }
    }

    private fun setListeners() {
        binding.apply {
            toolbar.setNavigationOnClickListener { finish() }

            edMonthYear.setOnClickListener {
                val pd = MonthYearPickerDialog()
                pd.setListener(this@UpdateTernakActivity)
                pd.isCancelable = false
                pd.show(supportFragmentManager, "MonthYearPickerDialog")
            }

            btnSave.setOnClickListener {
                if (edMonthYear.text.toString().isEmpty()) {
                    alertDialogMessage(
                        this@UpdateTernakActivity,
                        "Masukkan Bulan/Tahun dengan benar!"
                    )
                } else {
                    val builder = AlertDialog.Builder(this@UpdateTernakActivity)
                    builder.setCancelable(false)

                    with(builder)
                    {
                        setTitle("Apakah semua data sudah benar?")
                        setMessage("Cek dan pastikan ulang semua data sudah benar")
                        setPositiveButton("Benar") { dialog, _ ->
                            dialog.dismiss()

                            if (isFromJual) {
                                updateTernakViewModel.updateTernak(ternakId, "JUAL")
                                    .observe(this@UpdateTernakActivity) { result ->
                                        when (result) {
                                            is Result.Loading -> {
                                                updateTernakViewModel.isLoading.postValue(true)
                                            }

                                            is Result.Success -> {
                                                updateTernakViewModel.isLoading.postValue(false)
                                                val builder = AlertDialog.Builder(this@UpdateTernakActivity)
                                                builder.setCancelable(false)

                                                with(builder)
                                                {
                                                    setTitle("Berhasil Update Ternak!")
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
                                                updateTernakViewModel.isLoading.postValue(false)
                                                updateTernakViewModel.responseMessage.postValue(result.error)
                                            }
                                        }
                                    }
                            } else {
                                updateTernakViewModel.diedTernak(ternakId)
                                    .observe(this@UpdateTernakActivity) { result ->
                                        when (result) {
                                            is Result.Loading -> {
                                                updateTernakViewModel.isLoading.postValue(true)
                                            }

                                            is Result.Success -> {
                                                updateTernakViewModel.isLoading.postValue(false)
                                                val builder = AlertDialog.Builder(this@UpdateTernakActivity)
                                                builder.setCancelable(false)

                                                with(builder)
                                                {
                                                    setTitle("Berhasil Update Ternak!")
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
                                                updateTernakViewModel.isLoading.postValue(false)
                                                updateTernakViewModel.responseMessage.postValue(result.error)
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

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, date: Int) {
        updateTernakViewModel.terjualMonth.postValue(if (month < 10) "0$month" else month.toString())
        updateTernakViewModel.terjualYear.postValue(year.toString())

        binding.edMonthYear.setText(StringBuilder("$month/$year"))
    }

    companion object {
        const val EXTRA_TERNAK_NAME = "extra_ternak_name"
        const val EXTRA_TERNAK_ID = "extra_ternak_id"

        const val EXTRA_IS_FROM_JUAL = "extra_is_from_jual"
    }
}