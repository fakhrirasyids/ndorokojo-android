package com.ndorokojo.ui.died

import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ndorokojo.R
import com.ndorokojo.databinding.ActivityDiedBinding
import com.ndorokojo.di.Injection
import com.ndorokojo.ui.main.MainActivity
import com.ndorokojo.utils.Constants.alertDialogMessage
import com.ndorokojo.utils.MonthYearPickerDialog
import com.ndorokojo.utils.Result


class DiedActivity : AppCompatActivity(), OnDateSetListener {
    private lateinit var binding: ActivityDiedBinding
    private val diedViewModel by viewModels<DiedViewModel> {
        DiedViewModelFactory.getInstance(
            Injection.provideApiService(this)
        )
    }

    private lateinit var loadingDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val inflaterLoading: LayoutInflater = layoutInflater
        val loadingAlert = AlertDialog.Builder(this)
            .setView(inflaterLoading.inflate(R.layout.custom_loading_dialog, null))
            .setCancelable(false)
        loadingDialog = loadingAlert.create()
        binding.toolbar.setNavigationOnClickListener { finish() }

        observeAll()

        setListeners()
    }

    private fun observeAll() {
        diedViewModel.apply {
            if (MainActivity.ternak != null) {
                binding.edTernakType.setText(MainActivity.ternak!!.livestockType.toString())
            }

            if (MainActivity.ternakListSpecies.isNotEmpty()) {
                val listTernakString = arrayListOf<String>()
                for (item in MainActivity.ternakListSpecies) {
                    listTernakString.add(item.livestockType.toString())
                }

                val ternakRasAdapter = ArrayAdapter(
                    this@DiedActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    listTernakString
                )
                binding.edTernakRas.apply {
                    setAdapter(ternakRasAdapter)
                    setOnItemClickListener { _, _, position, _ ->
                        selectedRasTypeId.postValue(MainActivity.ternakListSpecies[position].id!!)
                        selectedTernakId.postValue(null)
                        binding.edTernak.setText("")
                        binding.edAsalKandang.setText("")
                        diedViewModel.getAllTernakList(MainActivity.ternakListSpecies[position].id!!)
                    }
                }

                binding.edTernakRasLayout.visibility = View.VISIBLE
            } else {
                selectedRasTypeId.postValue(MainActivity.ternak!!.id)
                diedViewModel.getAllTernakList(MainActivity.ternak!!.id)
                binding.edTernakRasLayout.visibility = View.GONE
            }

            isLoadingParent.observe(this@DiedActivity) {
                if (it) {
                    loadingDialog.show()
                } else {
                    loadingDialog.dismiss()
                }
            }

            listTernak.observe(this@DiedActivity) { listTernak ->
                if (listTernak != null) {
                    if (listTernak.isNotEmpty()) {
                        binding.edTernak.isEnabled = true
                        binding.edTernak.hint = "Pilih Ternak"

                        val listTernakString = arrayListOf<String>()
                        for (item in listTernak) {
                            listTernakString.add(item.code.toString())
                        }

                        val ternakAdapter = ArrayAdapter(
                            this@DiedActivity,
                            android.R.layout.simple_spinner_dropdown_item,
                            listTernakString
                        )
                        binding.edTernak.apply {
                            setAdapter(ternakAdapter)
                            setOnItemClickListener { _, _, position, _ ->
                                selectedTernakId.postValue(listTernak[position].id!!)

                                selectedTernakId.observe(this@DiedActivity) { selectedTernak ->
                                    if (selectedTernak != null) {
                                        for (kandang in MainActivity.kandangList) {
                                            if (kandang.id == Integer.parseInt(listTernak[position].kandangId.toString())) {
                                                binding.edAsalKandang.setText(kandang.name)
                                                break
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        binding.edTernak.isEnabled = false
                        binding.edTernak.setAdapter(null)
                        binding.edTernak.setText("")
                        binding.edTernak.hint = "Tidak ada Ternak"
                    }
                } else {
                    binding.edTernak.setAdapter(null)
                    binding.edTernak.setText("")
                    binding.edTernak.hint = "Tidak ada Ternak"
                }

            }
        }
    }

    private fun setListeners() {
        binding.apply {
            edMonthYear.setOnClickListener {
                val pd = MonthYearPickerDialog()
                pd.setListener(this@DiedActivity)
                pd.isCancelable = false
                pd.show(supportFragmentManager, "MonthYearPickerDialog")
            }

            btnSave.setOnClickListener {
                if (isValid()) {
                    diedViewModel.deadType.postValue(edDeadType.text.toString())
                    diedViewModel.deadReason.postValue(edDeadReason.text.toString())
                    val builder = AlertDialog.Builder(this@DiedActivity)
                    builder.setCancelable(false)

                    with(builder)
                    {
                        setTitle("Apakah semua data sudah benar?")
                        setMessage("Cek dan pastikan ulang semua data sudah benar")
                        setPositiveButton("Benar") { dialog, _ ->
                            dialog.dismiss()

                            diedViewModel.diedTernak().observe(this@DiedActivity) { result ->
                                when (result) {
                                    is Result.Loading -> {
                                        loadingDialog.show()
                                    }

                                    is Result.Success -> {
                                        loadingDialog.dismiss()
                                        val builder = AlertDialog.Builder(this@DiedActivity)
                                        builder.setCancelable(false)

                                        with(builder)
                                        {
                                            setTitle("Berhasil Died Ternak!")
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
                                        alertDialogMessage(
                                            this@DiedActivity,
                                            "Gagal Input Data : ${result.error}"
                                        )
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

    private fun isValid() = if (MainActivity.ternakListSpecies.isNotEmpty()) {
        if (binding.edTernakRas.text.isNullOrEmpty()) {
            alertDialogMessage(this@DiedActivity, "Masukkan Ras Ternak dengan benar!")
//            binding.edTernakRasLayout.error = "Masukkan Ras Ternak dengan benar!"
            false
        } else if (binding.edTernak.text.isNullOrEmpty()) {
            alertDialogMessage(this@DiedActivity, "Masukkan Ternak dengan benar!")
//            binding.edPakanLayout.error = "Masukkan Pakan Ternak dengan benar!"
            false
        } else if (binding.edDeadType.text.isNullOrEmpty()) {
            alertDialogMessage(this@DiedActivity, "Masukkan Tipe Kematian Ternak dengan benar!")
//            binding.edLimbahLayout.error = "Masukkan Limbah Ternak dengan benar!"
            false
        } else if (binding.edDeadReason.text.isNullOrEmpty()) {
            alertDialogMessage(this@DiedActivity, "Masukkan Sebab Kematian Ternak benar!")
//            binding.edAgeLayout.error = "Masukkan Umur Ternak benar!"
            false
        } else if (binding.edMonthYear.text.isNullOrEmpty()) {
            alertDialogMessage(this@DiedActivity, "Masukkan Bulan/Tahun Kematian Ternak benar!")
//            binding.edAgeLayout.error = "Masukkan Umur Ternak benar!"
            false
        } else {
            true
        }
    } else {
        if (binding.edTernak.text.isNullOrEmpty()) {
            alertDialogMessage(this@DiedActivity, "Masukkan Ternak dengan benar!")
//            binding.edPakanLayout.error = "Masukkan Pakan Ternak dengan benar!"
            false
        } else if (binding.edDeadType.text.isNullOrEmpty()) {
            alertDialogMessage(this@DiedActivity, "Masukkan Tipe Kematian Ternak dengan benar!")
//            binding.edLimbahLayout.error = "Masukkan Limbah Ternak dengan benar!"
            false
        } else if (binding.edDeadReason.text.isNullOrEmpty()) {
            alertDialogMessage(this@DiedActivity, "Masukkan Sebab Kematian Ternak benar!")
//            binding.edAgeLayout.error = "Masukkan Umur Ternak benar!"
            false
        } else if (binding.edMonthYear.text.isNullOrEmpty()) {
            alertDialogMessage(this@DiedActivity, "Masukkan Bulan/Tahun Kematian Ternak benar!")
//            binding.edAgeLayout.error = "Masukkan Umur Ternak benar!"
            false
        } else {
            true
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, date: Int) {
        diedViewModel.deadMonth.postValue(if (month < 10) "0$month" else month.toString())
        diedViewModel.deadYear.postValue(year.toString())

        binding.edMonthYear.setText(StringBuilder("$month/$year"))
    }
}