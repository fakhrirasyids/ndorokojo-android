package com.ndorokojo.ui.birth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.ndorokojo.R
import com.ndorokojo.databinding.ActivityBirthBinding
import com.ndorokojo.di.Injection
import com.ndorokojo.ui.main.MainActivity
import com.ndorokojo.utils.Constants.alertDialogMessage
import com.ndorokojo.utils.Result

class BirthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBirthBinding
    private val birthViewModel by viewModels<BirthViewModel> {
        BirthViewModelFactory.getInstance(
            Injection.provideApiService(this)
        )
    }

    private lateinit var loadingDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBirthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val inflaterLoading: LayoutInflater = layoutInflater
        val loadingAlert = AlertDialog.Builder(this)
            .setView(inflaterLoading.inflate(R.layout.custom_loading_dialog, null))
            .setCancelable(false)
        loadingDialog = loadingAlert.create()
        binding.toolbar.setNavigationOnClickListener { finish() }

        observeAll()

        setTernak()
        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            btnSave.setOnClickListener {
                if (isValid()) {
                    val builder = AlertDialog.Builder(this@BirthActivity)
                    builder.setCancelable(false)

                    with(builder)
                    {
                        setTitle("Apakah semua data sudah benar?")
                        setMessage("Cek dan pastikan ulang semua data sudah benar")
                        birthViewModel.ternakAge.postValue(edAge.text.toString())

                        setPositiveButton("Benar") { dialog, _ ->
                            dialog.dismiss()

                            birthViewModel.birthTernak()
                                .observe(this@BirthActivity) { result ->
                                    when (result) {
                                        is Result.Loading -> {
                                            loadingDialog.show()
                                        }

                                        is Result.Success -> {
                                            loadingDialog.dismiss()
                                            val builder = AlertDialog.Builder(this@BirthActivity)
                                            builder.setCancelable(false)

                                            with(builder)
                                            {
                                                setTitle("Berhasil Birth Ternak!")
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
                                                this@BirthActivity,
                                                "error : ${result.error}",
                                                "Gagal Input Data"
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


    private fun observeAll() {
        birthViewModel.apply {
            binding.apply {
                if (MainActivity.ternakListSpecies.isNotEmpty()) {
                    val listTernakString = arrayListOf<String>()
                    for (item in MainActivity.ternakListSpecies) {
                        listTernakString.add(item.livestockType.toString())
                    }

                    val ternakRasAdapter = ArrayAdapter(
                        this@BirthActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        listTernakString
                    )
                    binding.edTernakRas.apply {
                        setAdapter(ternakRasAdapter)
                        setOnItemClickListener { _, _, position, _ ->
                            selectedRasTypeId.postValue(MainActivity.ternakListSpecies[position].id!!)
                        }
                    }
                } else {
                    selectedRasTypeId.postValue(MainActivity.ternak!!.id)
                }

                if (MainActivity.kandangList.isNotEmpty()) {
                    val listKandangString = arrayListOf<String>()
                    for (item in MainActivity.kandangList) {
                        listKandangString.add(item.name.toString())
                    }

                    val kandangAdapter = ArrayAdapter(
                        this@BirthActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        listKandangString
                    )
                    binding.edKandang.apply {
                        setAdapter(kandangAdapter)
                        setOnItemClickListener { _, _, position, _ ->
                            selectedKandangId.postValue(MainActivity.kandangList[position].id!!)
                        }
                    }
                }

                listPakan.observe(this@BirthActivity) { listPakan ->
                    if (listPakan != null) {
                        if (selectedPakanId.value != null) {
                            for (item in listPakan) {
                                if (selectedPakanId.value == item.id) {
                                    edPakan.setText(item.jenisPakan)
                                }
                            }
                        } else {
                            edPakan.hint = "Pakan Ternak"
                        }

                        binding.edPakan.isEnabled = true
                        val listPakanString = arrayListOf<String>()
                        for (item in listPakan) {
                            listPakanString.add(item.jenisPakan.toString())
                        }

                        val pakanAdapter = ArrayAdapter(
                            this@BirthActivity,
                            android.R.layout.simple_spinner_dropdown_item,
                            listPakanString
                        )
                        binding.edPakan.apply {
                            setAdapter(pakanAdapter)
                            setOnItemClickListener { _, _, position, _ ->
                                selectedPakanId.postValue(listPakan[position].id!!)
                            }
                        }
                    } else {
                        edPakan.hint = "Pakan Ternak"
                    }
                }

                listLimbah.observe(this@BirthActivity) { listLimbah ->
                    if (listLimbah != null) {
                        if (selectedLimbahId.value != null) {
                            for (item in listLimbah) {
                                if (selectedLimbahId.value == item.id) {
                                    edLimbah.setText(item.pengolahanLimbah)
                                }
                            }
                        } else {
                            edLimbah.hint = "Limbah Ternak"
                        }

                        binding.edLimbah.isEnabled = true
                        val listLimbahString = arrayListOf<String>()
                        for (item in listLimbah) {
                            listLimbahString.add(item.pengolahanLimbah.toString())
                        }

                        val limbahAdapter = ArrayAdapter(
                            this@BirthActivity,
                            android.R.layout.simple_spinner_dropdown_item,
                            listLimbahString
                        )
                        binding.edLimbah.apply {
                            setAdapter(limbahAdapter)
                            setOnItemClickListener { _, _, position, _ ->
                                selectedLimbahId.postValue(listLimbah[position].id!!)
                            }
                        }
                    } else {
                        edLimbah.hint = "Limbah Ternak"
                    }
                }

                val umurAdapter = ArrayAdapter(
                    this@BirthActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    arrayOf(
                        getString(R.string.ternak_umur_anak),
                        getString(R.string.ternak_umur_dewasa)
                    )
                )
                edAge.isEnabled = true
                edAge.setAdapter(umurAdapter)
            }
        }
    }

    private fun isValid() = if (MainActivity.ternakListSpecies.isNotEmpty()) {
        if (binding.edKandang.text.isNullOrEmpty()) {
            alertDialogMessage(this@BirthActivity,"Masukkan Kandang Ternak dengan benar!")
//            binding.edKandangLayout.error = "Masukkan Kandang Ternak dengan benar!"
            false
        } else if (binding.edTernakRas.text.isNullOrEmpty()) {
            alertDialogMessage(this@BirthActivity,"Masukkan Ras Ternak dengan benar!")
//            binding.edTernakRasLayout.error = "Masukkan Ras Ternak dengan benar!"
            false
        } else if (binding.edPakan.text.isNullOrEmpty()) {
            alertDialogMessage(this@BirthActivity,"Masukkan Pakan Ternak dengan benar!")
//            binding.edPakanLayout.error = "Masukkan Pakan Ternak dengan benar!"
            false
        } else if (binding.edLimbah.text.isNullOrEmpty()) {
            alertDialogMessage(this@BirthActivity,"Masukkan Limbah Ternak dengan benar!")
//            binding.edLimbahLayout.error = "Masukkan Limbah Ternak dengan benar!"
            false
        } else if (binding.edAge.text.isNullOrEmpty()) {
            alertDialogMessage(this@BirthActivity,"Masukkan Umur Ternak benar!")
//            binding.edAgeLayout.error = "Masukkan Umur Ternak benar!"
            false
        } else {
            true
        }
    } else {
        if (binding.edKandang.text.isNullOrEmpty()) {
            alertDialogMessage(this@BirthActivity,"Masukkan Kandang Ternak dengan benar!")
//            binding.edKandangLayout.error = "Masukkan Kandang Ternak dengan benar!"
            false
        } else if (binding.edPakan.text.isNullOrEmpty()) {
            alertDialogMessage(this@BirthActivity,"Masukkan Pakan Ternak dengan benar!")
//            binding.edPakanLayout.error = "Masukkan Pakan Ternak dengan benar!"
            false
        } else if (binding.edLimbah.text.isNullOrEmpty()) {
            alertDialogMessage(this@BirthActivity,"Masukkan Limbah Ternak dengan benar!")
//            binding.edLimbahLayout.error = "Masukkan Limbah Ternak dengan benar!"
            false
        } else if (binding.edAge.text.isNullOrEmpty()) {
            alertDialogMessage(this@BirthActivity,"Masukkan Umur Ternak benar!")
//            binding.edAgeLayout.error = "Masukkan Umur Ternak benar!"
            false
        } else {
            true
        }
    }

    private fun setTernak() {
        if (MainActivity.ternak != null) {
            binding.edTernakType.setText(MainActivity.ternak!!.livestockType)
        }

        binding.edTernakRasLayout.isVisible = MainActivity.ternakListSpecies.isNotEmpty()
    }
}