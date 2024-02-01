package com.ndorokojo.ui.bottomsheetinputdata.fragments.ternak

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ndorokojo.R
import com.ndorokojo.databinding.FragmentTernakBinding
import com.ndorokojo.di.Injection
import com.ndorokojo.ui.bottomsheetinputdata.BottomSheetInputData
import com.ndorokojo.ui.bottomsheetinputdata.BottomSheetInputData.Companion.bottomSheetInputData
import com.ndorokojo.ui.bottomsheetinputdata.BottomSheetInputData.Companion.storeTernakViewModel
import com.ndorokojo.ui.bottomsheetinputdata.BottomSheetInputDataViewModelFactory
import com.ndorokojo.ui.bottomsheetinputdata.fragments.StoreTernakViewModel
import com.ndorokojo.ui.main.MainActivity
import com.ndorokojo.utils.Constants
import com.ndorokojo.utils.Result

class TernakFragment : Fragment() {
    private lateinit var binding: FragmentTernakBinding


    private lateinit var loadingDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTernakBinding.inflate(inflater, container, false)
        val inflaterLoading: LayoutInflater = layoutInflater
        val loadingAlert = AlertDialog.Builder(requireContext())
            .setView(inflaterLoading.inflate(R.layout.custom_loading_dialog, null))
            .setCancelable(false)
        loadingDialog = loadingAlert.create()

        observeAll()

        setTernak()
        setListeners()

        return binding.root
    }

    private fun observeAll() {
        storeTernakViewModel?.apply {
            binding.apply {
                if (MainActivity.ternakListSpecies.isNotEmpty()) {
                    val listTernakString = arrayListOf<String>()
                    for (item in MainActivity.ternakListSpecies) {
                        listTernakString.add(item.livestockType.toString())
                    }

                    val ternakRasAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        listTernakString
                    )
                    binding.edTernakRas.apply {
                        setAdapter(ternakRasAdapter)
                        setOnItemClickListener { _, _, position, _ ->
                            selectedRasTypeId.postValue(MainActivity.ternakListSpecies[position].id!!)
                            selectedParentTypeId.postValue(Integer.parseInt(MainActivity.ternakListSpecies[position].parentTypeId!!))
                        }
                    }
                } else {
                    selectedRasTypeId.postValue(MainActivity.ternak!!.id)
                    selectedParentTypeId.postValue(MainActivity.ternak!!.id)
                }

//                listPakan.observe(viewLifecycleOwner) { listPakan ->
//                    if (listPakan != null) {
//                        if (selectedPakanId.value != null) {
//                            for (item in listPakan) {
//                                if (selectedPakanId.value == item.id) {
//                                    edPakan.setText(item.jenisPakan)
//                                }
//                            }
//                        } else {
//                            edPakan.hint = "Pakan Ternak"
//                        }
//
//                        binding.edPakan.isEnabled = true
//                        val listPakanString = arrayListOf<String>()
//                        for (item in listPakan) {
//                            listPakanString.add(item.jenisPakan.toString())
//                        }
//
//                        val pakanAdapter = ArrayAdapter(
//                            requireContext(),
//                            android.R.layout.simple_spinner_dropdown_item,
//                            listPakanString
//                        )
//                        binding.edPakan.apply {
//                            setAdapter(pakanAdapter)
//                            setOnItemClickListener { _, _, position, _ ->
//                                selectedPakanId.postValue(listPakan[position].id!!)
//                            }
//                        }
//                    } else {
//                        edPakan.hint = "Pakan Ternak"
//                    }
//                }

                listLimbah.observe(viewLifecycleOwner) { listLimbah ->
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
                            requireContext(),
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

                val genderAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    arrayOf(
                        "JANTAN",
                        "BETINA"
                    )
                )
                edGender.isEnabled = true
                edGender.setAdapter(genderAdapter)

                edGender.setOnItemClickListener { adapterView, view, i, l ->
                    edAge.setText("")
                    val umurAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        arrayOf(
                            getString(R.string.ternak_umur_anak),
                            getString(R.string.ternak_umur_muda),
                            getString(R.string.ternak_umur_dewasa),
                            if (i == 0) "BIBIT PEJANTAN" else "BIBIT INDUK"
                        )
                    )
                    edAge.setAdapter(umurAdapter)

                }

                edAge.isEnabled = true

                edAge.setOnClickListener {
                    if (edGender.text.toString().isEmpty()) {
                        edAge.clearFocus()
                        Constants.alertDialogMessage(
                            requireContext(),
                            "Pilih Gender terlebih dahulu!"
                        )
                    }
                }
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            btnBack.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                builder.setCancelable(false)

                with(builder)
                {
                    setMessage("Apakah anda ingin kembali ke Sensor?")
                    setPositiveButton("Ya") { dialog, _ ->
                        dialog.dismiss()
                        BottomSheetInputData.changeFragmentToIndex(2)
                    }
                    setNegativeButton("Tidak") { dialog, _ ->
                        dialog.dismiss()
                    }
                    show()
                }
            }

            btnSave.setOnClickListener {
                if (isValid()) {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setCancelable(false)

                    with(builder)
                    {
                        setTitle("Apakah semua data sudah benar?")
                        setMessage("Silahkan cek kembali jika terdapat kesalahan data")
                        storeTernakViewModel?.ternakAge?.postValue(edAge.text.toString())

                        setPositiveButton("Benar") { dialog, _ ->
                            dialog.dismiss()
                            var pakan =
                                if (binding.cb1.isChecked) binding.cb1.text.toString() else ""
                            pakan += if (binding.cb2.isChecked) ",${binding.cb2.text}" else ""
                            pakan += if (binding.cb3.isChecked) ",${binding.cb3.text}" else ""
                            pakan += if (binding.cb4.isChecked) ",${binding.cb4.text}" else ""


                            storeTernakViewModel?.storeTernak(
                                pakan,
                                edJml.text.toString().toInt(),
                                edGender.text.toString()
                            )
                                ?.observe(viewLifecycleOwner) { result ->
                                    when (result) {
                                        is Result.Loading -> {
                                            loadingDialog.show()
                                        }

                                        is Result.Success -> {
                                            loadingDialog.dismiss()
                                            val builder = AlertDialog.Builder(requireContext())
                                            builder.setCancelable(false)

                                            with(builder)
                                            {
                                                setTitle("Berhasil Input Data")
                                                setMessage("Silahkan melanjutkan untuk mengolah data ternak")
                                                setPositiveButton("OK") { dialog, _ ->
                                                    dialog.dismiss()
                                                    bottomSheetInputData?.dismiss()
                                                    bottomSheetInputData = null
                                                }
                                                show()
                                            }
                                        }

                                        is Result.Error -> {
                                            loadingDialog.dismiss()
                                            alertDialogMessage("Gagal Input Data : ${result.error}")
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

    //    private fun isValid() = if (MainActivity.ternakListSpecies.isNotEmpty()) {
//        if (binding.edTernakRas.text.isNullOrEmpty()) {
//            alertDialogMessage("Masukkan Ras Ternak dengan benar!")
////            binding.edTernakRasLayout.error = "Masukkan Ras Ternak dengan benar!"
//            false
//        } else if (binding.edPakan.text.isNullOrEmpty()) {
//            alertDialogMessage("Masukkan Pakan Ternak dengan benar!")
////            binding.edPakanLayout.error = "Masukkan Pakan Ternak dengan benar!"
//            false
//        } else if (binding.edLimbah.text.isNullOrEmpty()) {
//            alertDialogMessage("Masukkan Limbah Ternak dengan benar!")
////            binding.edLimbahLayout.error = "Masukkan Limbah Ternak dengan benar!"
//            false
//        } else if (binding.edAge.text.isNullOrEmpty()) {
//            alertDialogMessage("Masukkan Umur Ternak benar!")
////            binding.edAgeLayout.error = "Masukkan Umur Ternak benar!"
//            false
//        } else {
//            true
//        }
//    } else {
//        if (binding.edPakan.text.isNullOrEmpty()) {
//            alertDialogMessage("Masukkan Pakan Ternak dengan benar!")
////            binding.edPakanLayout.error = "Masukkan Pakan Ternak dengan benar!"
//            false
//        } else if (binding.edLimbah.text.isNullOrEmpty()) {
//            alertDialogMessage("Masukkan Limbah Ternak dengan benar!")
////            binding.edLimbahLayout.error = "Masukkan Limbah Ternak dengan benar!"
//            false
//        } else if (binding.edAge.text.isNullOrEmpty()) {
//            alertDialogMessage("Masukkan Umur Ternak benar!")
////            binding.edAgeLayout.error = "Masukkan Umur Ternak benar!"
//            false
//        } else {
//            true
//        }
//    }
    private fun isValid() = if (MainActivity.ternakListSpecies.isNotEmpty()) {
        if (binding.edTernakType.text.toString()
                .lowercase() == "ayam" || binding.edTernakType.text.toString()
                .lowercase() == "itik"
        ) {
            if (binding.edTernakRas.text.isNullOrEmpty()) {
                Constants.alertDialogMessage(
                    requireContext(),
                    "Masukkan Ras Ternak dengan benar!"
                )
                false
            } else if (!binding.cb1.isChecked && !binding.cb2.isChecked && !binding.cb3.isChecked) {
                Constants.alertDialogMessage(
                    requireContext(),
                    "Masukkan Pakan Ternak dengan benar!"
                )
                false
            } else if (binding.edLimbah.text.isNullOrEmpty()) {
                Constants.alertDialogMessage(
                    requireContext(),
                    "Masukkan Limbah Ternak dengan benar!"
                )
                false
            } else if (binding.edAge.text.isNullOrEmpty()) {
                Constants.alertDialogMessage(requireContext(), "Masukkan Umur Ternak benar!")
                false
            } else {
                true
            }
        } else {
            if (binding.edTernakRas.text.isNullOrEmpty()) {
                Constants.alertDialogMessage(
                    requireContext(),
                    "Masukkan Ras Ternak dengan benar!"
                )
                false
            } else if (!binding.cb1.isChecked && !binding.cb2.isChecked && !binding.cb3.isChecked && !binding.cb4.isChecked
            ) {
                Constants.alertDialogMessage(
                    requireContext(),
                    "Masukkan Pakan Ternak dengan benar!"
                )
                false
            } else if (binding.edLimbah.text.isNullOrEmpty()) {
                Constants.alertDialogMessage(
                    requireContext(),
                    "Masukkan Limbah Ternak dengan benar!"
                )
                false
            } else if (binding.edAge.text.isNullOrEmpty()) {
                Constants.alertDialogMessage(requireContext(), "Masukkan Umur Ternak benar!")
                false
            } else {
                true
            }
        }
    } else {
        if (binding.edTernakType.text.toString()
                .lowercase() == "ayam" || binding.edTernakType.text.toString()
                .lowercase() == "itik"
        ) {
            if (!binding.cb1.isChecked && !binding.cb2.isChecked && !binding.cb3.isChecked) {
                Constants.alertDialogMessage(
                    requireContext(),
                    "Masukkan Pakan Ternak dengan benar!"
                )
                false
            } else if (binding.edLimbah.text.isNullOrEmpty()) {
                Constants.alertDialogMessage(
                    requireContext(),
                    "Masukkan Limbah Ternak dengan benar!"
                )
                false
            } else if (binding.edGender.text.isNullOrEmpty()) {
                Constants.alertDialogMessage(
                    requireContext(),
                    "Masukkan Gender Ternak dengan benar!"
                )
                false
            } else if (binding.edAge.text.isNullOrEmpty()) {
                Constants.alertDialogMessage(requireContext(), "Masukkan Umur Ternak dengan benar!")
                false
            } else if (binding.edJml.text.isNullOrEmpty() || (binding.edJml.text.toString()
                    .toInt() == 0)
            ) {
                Constants.alertDialogMessage(
                    requireContext(),
                    "Masukkan Jumlah Ternak dengan benar!"
                )
                false
            } else {
                true
            }
        } else {
            if (!binding.cb1.isChecked && !binding.cb2.isChecked && !binding.cb3.isChecked && !binding.cb4.isChecked) {
                Constants.alertDialogMessage(
                    requireContext(),
                    "Masukkan Pakan Ternak dengan benar!"
                )
                false
            } else if (binding.edLimbah.text.isNullOrEmpty()) {
                Constants.alertDialogMessage(
                    requireContext(),
                    "Masukkan Limbah Ternak dengan benar!"
                )
                false
            } else if (binding.edGender.text.isNullOrEmpty()) {
                Constants.alertDialogMessage(
                    requireContext(),
                    "Masukkan Gender Ternak dengan benar!"
                )
                false
            } else if (binding.edAge.text.isNullOrEmpty()) {
                Constants.alertDialogMessage(requireContext(), "Masukkan Umur Ternak benar!")
                false
            } else if (binding.edJml.text.isNullOrEmpty() || (binding.edJml.text.toString()
                    .toInt() == 0)
            ) {
                Constants.alertDialogMessage(
                    requireContext(),
                    "Masukkan Jumlah Ternak dengan benar!"
                )
                false
            } else {
                true
            }
        }
    }

    private fun alertDialogMessage(message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false)

        with(builder)
        {
            setMessage(message)
            setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            show()
        }
    }

    private fun setTernak() {
        if (MainActivity.ternak != null) {
            binding.edTernakType.setText(MainActivity.ternak!!.livestockType)
        }

        binding.edTernakRasLayout.isVisible = MainActivity.ternakListSpecies.isNotEmpty()
    }
}