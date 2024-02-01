package com.ndorokojo.ui.sell

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ndorokojo.R
import com.ndorokojo.databinding.ActivitySellBinding
import com.ndorokojo.di.Injection
import com.ndorokojo.ui.main.MainActivity
import com.ndorokojo.utils.Constants
import com.ndorokojo.utils.Result
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class SellActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellBinding
    private val sellViewModel by viewModels<SellViewModel> {
        SellViewModelFactory(
            Injection.provideApiService(this)
        )
    }


    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val image = result.data?.data as Uri
            sellViewModel.imageTernak.postValue(uriToFile(image))
        }
    }

    private lateinit var loadingDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellBinding.inflate(layoutInflater)
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
        sellViewModel.apply {
            if (MainActivity.ternak != null) {
                binding.edTernakType.setText(MainActivity.ternak!!.livestockType.toString())
            }

            if (MainActivity.ternakListSpecies.isNotEmpty()) {
                val listTernakString = arrayListOf<String>()
                for (item in MainActivity.ternakListSpecies) {
                    listTernakString.add(item.livestockType.toString())
                }

                val ternakRasAdapter = ArrayAdapter(
                    this@SellActivity,
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
                        sellViewModel.getAllTernakList(MainActivity.ternakListSpecies[position].id!!)
                    }
                }

                binding.edTernakRasLayout.visibility = View.VISIBLE
            } else {
                selectedRasTypeId.postValue(MainActivity.ternak!!.id)
                sellViewModel.getAllTernakList(MainActivity.ternak!!.id)
                binding.edTernakRasLayout.visibility = View.GONE
            }

            isLoadingParent.observe(this@SellActivity) {
                if (it) {
                    loadingDialog.show()
                } else {
                    loadingDialog.dismiss()
                }
            }

            imageTernak.observe(this@SellActivity) {
                binding.ivTernak.setImageBitmap(BitmapFactory.decodeFile(it.path))
            }

            listTernak.observe(this@SellActivity) { listTernak ->
                if (listTernak != null) {
                    if (listTernak.isNotEmpty()) {
                        binding.edTernak.isEnabled = true
                        binding.edTernak.hint = "Pilih Ternak"

                        val listTernakString = arrayListOf<String>()
                        for (item in listTernak) {
                            if (item.soldProposedPrice == null) {
                                listTernakString.add(item.code.toString())
                            }
                        }

                        if (listTernakString.isEmpty()) {
                            binding.edTernak.isEnabled = false
                            binding.edTernak.setAdapter(null)
                            binding.edTernak.setText("")
                            binding.edTernak.hint = "Tidak ada Ternak"
                        } else {
                            val ternakAdapter = ArrayAdapter(
                                this@SellActivity,
                                android.R.layout.simple_spinner_dropdown_item,
                                listTernakString
                            )
                            binding.edTernak.apply {
                                setAdapter(ternakAdapter)
                                setOnItemClickListener { _, _, position, _ ->
                                    selectedTernakId.postValue(listTernak[position].id!!)

                                    selectedTernakId.observe(this@SellActivity) { selectedTernak ->
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
            btnAddTernakImg.setOnClickListener {
                val iGallery = Intent()
                iGallery.action = Intent.ACTION_GET_CONTENT
                iGallery.type = "image/*"
                galleryLauncher.launch(Intent.createChooser(iGallery, "Pilih Gambar Ternak"))
            }

            btnSave.setOnClickListener {
                if (isValid()) {
                    sellViewModel.sellProposedPrice.postValue(Integer.parseInt(edPenawaran.text.toString()))

                    val builder = AlertDialog.Builder(this@SellActivity)
                    builder.setCancelable(false)

                    with(builder)
                    {
                        setTitle("Apakah semua data sudah benar?")
                        setMessage("Cek dan pastikan ulang semua data sudah benar")
                        setPositiveButton("Benar") { dialog, _ ->
                            dialog.dismiss()

                            sellViewModel.updateTernakImage()
                                .observe(this@SellActivity) { res ->
                                    when (res) {
                                        is Result.Loading -> {
                                            loadingDialog.show()
                                        }

                                        is Result.Success -> {
                                            sellViewModel.sellTernak()
                                                .observe(this@SellActivity) { result ->
                                                    when (result) {
                                                        is Result.Loading -> {
                                                        }

                                                        is Result.Success -> {
                                                            loadingDialog.dismiss()
                                                            val builder =
                                                                AlertDialog.Builder(this@SellActivity)
                                                            builder.setCancelable(false)

                                                            with(builder)
                                                            {
                                                                setTitle("Berhasil Sell Ternak!")
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
                                                                this@SellActivity,
                                                                if (result.error == "HTTP 404 ") "Ternak sudah pernah dijual, Silahkan pilih ternak lain!" else result.error,
                                                                "Gagal Sell Ternak"
                                                            )
                                                        }
                                                    }
                                                }
                                        }

                                        is Result.Error -> {
                                            loadingDialog.dismiss()
                                            Constants.alertDialogMessage(
                                                this@SellActivity,
                                                res.error,
                                                "Gagal Sell Ternak"
                                            )
                                        }
                                    }
                                }
//                            sellViewModel.sellTernak().observe(this@SellActivity) { result ->
//                                when (result) {
//                                    is Result.Loading -> {
//                                        loadingDialog.show()
//                                    }
//
//                                    is Result.Success -> {
//                                        sellViewModel.updateTernakImage()
//                                            .observe(this@SellActivity) { res ->
//                                                when (res) {
//                                                    is Result.Loading -> {}
//                                                    is Result.Success -> {
//                                                        loadingDialog.dismiss()
//                                                        val builder =
//                                                            AlertDialog.Builder(this@SellActivity)
//                                                        builder.setCancelable(false)
//
//                                                        with(builder)
//                                                        {
//                                                            setTitle("Berhasil Sell Ternak!")
//                                                            setMessage("Silahkan melanjutkan untuk mengolah data ternak")
//                                                            setPositiveButton("OK") { dialog, _ ->
//                                                                dialog.dismiss()
//                                                                val intent = Intent()
//                                                                setResult(RESULT_OK, intent)
//                                                                finish()
//                                                            }
//                                                            show()
//                                                        }
//                                                    }
//
//                                                    is Result.Error -> {
//                                                        loadingDialog.dismiss()
//                                                        Constants.alertDialogMessage(
//                                                            this@SellActivity,
////                                                            if (res.error == "HTTP 404 ") "Ternak sudah pernah dijual, Silahkan pilih ternak lain!" else res.error,
//                                                            res.error,
//                                                            "Gagal Selsl Ternak"
//                                                        )
//                                                    }
//                                                }
//
//                                            }
//                                    }
//
//                                    is Result.Error -> {
//                                        loadingDialog.dismiss()
//                                        Constants.alertDialogMessage(
//                                            this@SellActivity,
//                                            if (result.error == "HTTP 404 ") "Ternak sudah pernah dijual, Silahkan pilih ternak lain!" else result.error,
//                                            "Gagal Sell Ternak"
//                                        )
//                                    }
//                                }
//                            }
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
            Constants.alertDialogMessage(this@SellActivity, "Masukkan Ras Ternak dengan benar!")
//            binding.edTernakRasLayout.error = "Masukkan Ras Ternak dengan benar!"
            false
        } else if (binding.edTernak.text.isNullOrEmpty()) {
            Constants.alertDialogMessage(this@SellActivity, "Masukkan Ternak dengan benar!")
//            binding.edPakanLayout.error = "Masukkan Pakan Ternak dengan benar!"
            false
        } else if (binding.edPenawaran.text.isNullOrEmpty()) {
            Constants.alertDialogMessage(
                this@SellActivity,
                "Masukkan Harga Penawaran Ternak dengan benar!"
            )
//            binding.edLimbahLayout.error = "Masukkan Limbah Ternak dengan benar!"
            false
        } else {
            true
        }
    } else {
        if (binding.edTernak.text.isNullOrEmpty()) {
            Constants.alertDialogMessage(this@SellActivity, "Masukkan Ternak dengan benar!")
//            binding.edPakanLayout.error = "Masukkan Pakan Ternak dengan benar!"
            false
        } else if (binding.edPenawaran.text.isNullOrEmpty()) {
            Constants.alertDialogMessage(
                this@SellActivity,
                "Masukkan Harga Penawaran Ternak dengan benar!"
            )
//            binding.edLimbahLayout.error = "Masukkan Limbah Ternak dengan benar!"
            false
        } else {
            true
        }
    }

    private fun uriToFile(uri: Uri): File {
        val tempFile = File.createTempFile(
            SimpleDateFormat(
                "dd-MMM-yyyy",
                Locale.US
            ).format(System.currentTimeMillis()),
            ".jpg",
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )

        val inputStream = contentResolver.openInputStream(uri) as InputStream
        val outputStream: OutputStream = FileOutputStream(tempFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return tempFile
    }

}