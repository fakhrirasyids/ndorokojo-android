package com.ndorokojo.ui.bottomsheetinputdata.fragments.kandang

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.ndorokojo.R
import com.ndorokojo.databinding.FragmentKandangBinding
import com.ndorokojo.ui.bottomsheetinputdata.BottomSheetInputData.Companion.changeFragmentToIndex
import com.ndorokojo.ui.bottomsheetinputdata.BottomSheetInputData.Companion.storeTernakViewModel
import com.ndorokojo.ui.main.tambahkandang.BottomSheetTambahKandang
import com.ndorokojo.ui.map.PickLocationActivity
import com.ndorokojo.utils.Constants

class KandangFragment : Fragment() {
    private lateinit var binding: FragmentKandangBinding
//    private val storeTernakViewModel by viewModels<StoreTernakViewModel> {
//        BottomSheetInputDataViewModelFactory.getInstance(
//            Injection.provideApiService(requireContext()),
//            Injection.provideUserPreferences(requireContext())
//        )
//    }

    private lateinit var loadingDialog: AlertDialog

    private val mapLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val lat = result.data?.extras?.getDouble(PickLocationActivity.EXTRA_LAT, 0.0)
            val lon = result.data?.extras?.getDouble(PickLocationActivity.EXTRA_LON, 0.0)

            if (lat != null && lon != null) {
                binding.apply {
                    storeTernakViewModel?.isLongitudeFilled?.postValue(true)
                    storeTernakViewModel?.isLatitudeFilled?.postValue(true)

                    storeTernakViewModel?.kandangLat?.postValue(lat.toString())
                    storeTernakViewModel?.kandangLon?.postValue(lon.toString())

                    layoutLocation.isVisible = false
                    layoutShowLocation.isVisible = true

                    tvLocationPicked.text = Constants.getAddress(
                        requireContext(),
                        lat,
                        lon
                    )
                }
            } else {
                storeTernakViewModel?.isLongitudeFilled?.postValue(false)
                storeTernakViewModel?.isLatitudeFilled?.postValue(false)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKandangBinding.inflate(inflater, container, false)
        val inflaterLoading: LayoutInflater = layoutInflater
        val loadingAlert = AlertDialog.Builder(requireContext())
            .setView(inflaterLoading.inflate(R.layout.custom_loading_dialog, null))
            .setCancelable(false)
        loadingDialog = loadingAlert.create()

        observeTextChanged()
        observeAll()

        setTextChangedListeners()
        setListeners()

        return binding.root
    }

    private fun observeAll() {
        storeTernakViewModel?.apply {
            binding.apply {
                isLoadingAddress.observe(viewLifecycleOwner) {
                    if (it) {
                        loadingDialog.show()
                    } else {
                        loadingDialog.dismiss()
                    }
                }

                listProvince.observe(viewLifecycleOwner) { listProvince ->
                    if (listProvince != null) {
                        if (selectedProvinceId.value != null) {
                            for (item in listProvince) {
                                if (selectedProvinceId.value == item.id) {
                                    edProvinsi.setText(item.name)
                                }
                            }
                        } else {
                            edProvinsi.hint = "Pilih Provinsi Kandang"
                        }

                        binding.edProvinsi.isEnabled = true
                        val listProvinceString = arrayListOf<String>()
                        for (item in listProvince) {
                            listProvinceString.add(item.name.toString())
                        }

                        val provinceAdapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            listProvinceString
                        )
                        binding.edProvinsi.apply {
                            setAdapter(provinceAdapter)
                            setOnItemClickListener { _, _, position, _ ->
                                getRegencyList(listProvince[position].id!!)
                                selectedProvinceId.postValue(listProvince[position].id!!)
                                selectedRegencyId.postValue(null)
                                selectedDistrictId.postValue(null)
                                selectedVillageId.postValue(null)

                                binding.apply {
                                    edKabupaten.setText("")
                                    edKabupaten.hint = "Pilih Kota/Kabupaten Kandang"
                                    edKabupaten.setAdapter(null)
                                    edKecamatan.setText("")
                                    edKecamatan.hint = "Pilih Kecamatan Kandang"
                                    edKecamatan.setAdapter(null)
                                    edDesaKel.setText("")
                                    edDesaKel.hint = "Pilih Desa/Kelurahan Kandang"
                                    edDesaKel.setAdapter(null)
                                }
                            }
                        }
                    } else {
                        edProvinsi.hint = "Pilih Provinsi Kandang"
                    }
                }

                listRegency.observe(viewLifecycleOwner) { listRegency ->
                    if (listRegency != null) {
                        if (selectedRegencyId.value != null) {
                            for (item in listRegency) {
                                if (selectedRegencyId.value == item.id) {
                                    edKabupaten.setText(item.name)
                                }
                            }
                        } else {
                            edKabupaten.hint = "Pilih Kota/Kabupaten Kandang"
                        }

                        binding.edKabupaten.isEnabled = true
                        val listRegencyString = arrayListOf<String>()
                        for (item in listRegency) {
                            listRegencyString.add(item.name.toString())
                        }

                        val regencyAdapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            listRegencyString
                        )
                        binding.edKabupaten.apply {
                            setAdapter(regencyAdapter)
                            setOnItemClickListener { _, _, position, _ ->
                                getDistrictList(listRegency[position].id!!)
                                selectedRegencyId.postValue(listRegency[position].id!!)
                                selectedDistrictId.postValue(null)
                                selectedVillageId.postValue(null)

                                binding.apply {
                                    edKecamatan.setText("")
                                    edKecamatan.hint = "Pilih Kecamatan Kandang"
                                    edKecamatan.setAdapter(null)
                                    edDesaKel.setText("")
                                    edDesaKel.hint = "Pilih Desa/Kelurahan Kandang"
                                    edDesaKel.setAdapter(null)
                                }
                            }
                        }
                    } else {
                        edKabupaten.hint = "Pilih Kota/Kabupaten Kandang"
                    }
                }

                listDistrict.observe(viewLifecycleOwner) { listDistrict ->
                    if (listDistrict != null) {
                        if (selectedDistrictId.value != null) {
                            for (item in listDistrict) {
                                if (selectedRegencyId.value == item.id) {
                                    edKecamatan.setText(item.name)
                                }
                            }
                        } else {
                            edKecamatan.hint = "Pilih Kecamatan Kandang"
                        }

                        binding.edKecamatan.isEnabled = true
                        val listDistrictString = arrayListOf<String>()
                        for (item in listDistrict) {
                            listDistrictString.add(item.name.toString())
                        }

                        val districtAdapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            listDistrictString
                        )
                        binding.edKecamatan.apply {
                            setAdapter(districtAdapter)
                            setOnItemClickListener { _, _, position, _ ->
                                getVillageList(listDistrict[position].id!!)
                                selectedDistrictId.postValue(listDistrict[position].id!!)
                                selectedVillageId.postValue(null)

                                binding.apply {
                                    edDesaKel.setText("")
                                    edDesaKel.hint = "Pilih Desa/Kelurahan Kandang"
                                    edDesaKel.setAdapter(null)
                                }
                            }
                        }
                    } else {
                        edKecamatan.hint = "Pilih Kecamatan Kandang"
                    }
                }

                listVillage.observe(viewLifecycleOwner) { listVillage ->
                    if (listVillage != null) {
                        if (selectedVillageId.value != null) {
                            for (item in listVillage) {
                                if (selectedVillageId.value == item.id?.toLong()) {
                                    edDesaKel.setText(item.name)
                                }
                            }
                        } else {
                            edDesaKel.hint = "Pilih Desa/Kelurahan Kandang"
                        }

                        binding.edDesaKel.isEnabled = true
                        val listVillageString = arrayListOf<String>()
                        for (item in listVillage) {
                            listVillageString.add(item.name.toString())
                        }

                        val villageAdapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            listVillageString
                        )
                        binding.edDesaKel.apply {
                            setAdapter(villageAdapter)
                            setOnItemClickListener { _, _, position, _ ->
                                selectedVillageId.postValue(listVillage[position].id!!.toLong())
                            }
                        }
                    } else {
                        edDesaKel.hint = "Pilih Desa/Kelurahan Kandang"
                    }
                }
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            edJenisKandang.apply {
                val jenisKandangAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    arrayOf(
                        getString(R.string.jenis_kandang_medium),
                        getString(R.string.jenis_kandang_besar)
                    )
                )
                isEnabled = true
                setAdapter(jenisKandangAdapter)
            }

            edKabupaten.setOnClickListener {
                if (storeTernakViewModel?.selectedProvinceId?.value == null) {
                    alertDialogMessage("Pilih provinsi kandang terlebih dahulu!")
                }
            }

            edKecamatan.setOnClickListener {
                if (storeTernakViewModel?.selectedRegencyId?.value == null) {
                    alertDialogMessage("Pilih Kota/Kabupaten kandang terlebih dahulu!")
                }
            }

            edDesaKel.setOnClickListener {
                if (storeTernakViewModel?.selectedDistrictId?.value == null) {
                    alertDialogMessage("Pilih Kecamatan kandang terlebih dahulu!")
                }
            }

            btnBack.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                builder.setCancelable(false)

                with(builder)
                {
                    setMessage("Apakah anda ingin kembali ke Profil?")
                    setPositiveButton("Ya") { dialog, _ ->
                        dialog.dismiss()
                        changeFragmentToIndex(0)
                    }
                    setNegativeButton("Tidak") { dialog, _ ->
                        dialog.dismiss()
                    }
                    show()
                }
            }

            layoutLocation.setOnClickListener {
                mapLauncher.launch(Intent(requireContext(), PickLocationActivity::class.java))
            }

            btnClearLocation.setOnClickListener {
                storeTernakViewModel?.isLongitudeFilled?.postValue(false)
                storeTernakViewModel?.isLatitudeFilled?.postValue(false)

                layoutLocation.isVisible = true
                layoutShowLocation.isVisible = false
            }

            btnSave.setOnClickListener {
                if (isValid()) {
                    storeTernakViewModel?.apply {
                        kandangName.postValue(binding.edName.text.toString())
                        kandangPanjang.postValue(
                            binding.edPanjang.text.toString().toDouble()
                        )
                        kandangLebar.postValue(
                            binding.edLebar.text.toString().toDouble()
                        )
                        kandangJenis.postValue(binding.edJenisKandang.text.toString())
                        kandangAlamat.postValue(binding.edAddress.text.toString())
                        kandangRtRw.postValue(binding.edRtrw.text.toString())
//                        kandangLat.postValue(binding.edLatitude.text.toString())
//                        kandangLat.postValue("1")
//                        kandangLon.postValue(binding.edLongitude.text.toString())
//                        kandangLon.postValue("1")
                    }

                    val builder = AlertDialog.Builder(requireContext())
                    builder.setCancelable(false)

                    with(builder)
                    {
                        setTitle("Apakah data Kandang sudah benar?")
                        setMessage("Silahkan cek kembali jika terdapat kesalahan data")
                        setPositiveButton("Benar") { dialog, _ ->
                            dialog.dismiss()
                            changeFragmentToIndex(2)
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

    private fun isValid() = if (binding.edName.text.isNullOrEmpty()) {
        alertDialogMessage("Masukkan Nama Kandang dengan benar!")
//        binding.edNameLayout.error = "Masukkan Nama Kandang dengan benar!"
        false
    } else if (binding.edPanjang.text.isNullOrEmpty()) {
        alertDialogMessage("Masukkan Panjang Kandang dengan benar!")
//        binding.edPanjangLayout.error = "Masukkan Panjang Kandang dengan benar!"
        false
    } else if (binding.edLebar.text.isNullOrEmpty()) {
        alertDialogMessage("Masukkan Lebar Kandang dengan benar!")
//        binding.edLebarLayout.error = "Masukkan Lebar Kandang dengan benar!"
        false
    } else if (binding.edJenisKandang.text.isNullOrEmpty()) {
        alertDialogMessage("Masukkan Jenis Kandang dengan benar!")
//        binding.edJenisKandangLayout.error = "Masukkan Jenis Kandang dengan benar!"
        false
    } else if (binding.edProvinsi.text.isNullOrEmpty()) {
        alertDialogMessage("Masukkan Provinsi dengan benar!")
//        binding.edProvinsiLayout.error = "Masukkan Provinsi dengan benar!"
        false
    } else if (binding.edKabupaten.text.isNullOrEmpty()) {
        alertDialogMessage("Masukkan Kota/Kabupaten dengan benar!")
//        binding.edKabupatenLayout.error = "Masukkan Kota/Kabupaten dengan benar!"
        false
    } else if (binding.edKecamatan.text.isNullOrEmpty()) {
        alertDialogMessage("Masukkan Kecamatan dengan benar!")
//        binding.edKecamatanLayout.error = "Masukkan Kecamatan dengan benar!"
        false
    } else if (binding.edDesaKel.text.isNullOrEmpty()) {
        alertDialogMessage("Masukkan Desa/Kelurahan dengan benar!")
//        binding.edDesaKelLayout.error = "Masukkan Desa/Kelurahan dengan benar!"
        false
    } else if (binding.edAddress.text.isNullOrEmpty()) {
        alertDialogMessage("Masukkan Alamat dengan benar!")
//        binding.edAddressLayout.error = "Masukkan Alamat dengan benar!"
        false
    } else if (binding.edRtrw.text.isNullOrEmpty()) {
        alertDialogMessage("Masukkan RT/RW Kandang dengan benar!")
//        binding.edRtrwLayout.error = "Masukkan RT/RW Kandang dengan benar!"
        false
//    } else if (binding.edLongitude.text.isNullOrEmpty()) {
//        alertDialogMessage("Masukkan Longitude Kandang dengan benar!")
////        binding.edAddressLayout.error = "Masukkan Longitude Kandang dengan benar!"
//        false
//    } else if (binding.edLatitude.text.isNullOrEmpty()) {
//        alertDialogMessage("Masukkan Latitude Kandang dengan benar!")
////        binding.edAddressLayout.error = "Masukkan Latitude Kandang dengan benar!"
//        false
    } else if (!binding.layoutShowLocation.isVisible) {
        alertDialogMessage("Masukkan Lokasi dengan benar!")
        false
    } else {
        true
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

    private fun observeTextChanged() {
        storeTernakViewModel?.apply {
            isNameFilled.observe(viewLifecycleOwner) {
                binding.edNameLayout.error = null
            }

            isLebarFilled.observe(viewLifecycleOwner) {
                binding.edLebarLayout.error = null
            }

            isPanjangFilled.observe(viewLifecycleOwner) {
                binding.edPanjangLayout.error = null
            }

            isJenisKandangFilled.observe(viewLifecycleOwner) {
                binding.edJenisKandangLayout.error = null
            }

            isProvinceFilled.observe(viewLifecycleOwner) {
                binding.edProvinsiLayout.error = null
            }

            isRegencyFilled.observe(viewLifecycleOwner) {
                binding.edKabupatenLayout.error = null
            }

            isDistrictFilled.observe(viewLifecycleOwner) {
                binding.edKecamatanLayout.error = null
            }

            isVillageFilled.observe(viewLifecycleOwner) {
                binding.edDesaKelLayout.error = null
            }

            isAddressFilled.observe(viewLifecycleOwner) {
                binding.edAddressLayout.error = null
            }

            isRtRwFilled.observe(viewLifecycleOwner) {
                binding.edRtrwLayout.error = null
            }

//            isLongitudeFilled.observe(viewLifecycleOwner) {
//                binding.edLongitudeLayout.error = null
//            }
//
//            isLatitudeFilled.observe(viewLifecycleOwner) {
//                binding.edLatitudeLayout.error = null
//            }
        }
    }

    private fun setTextChangedListeners() {
        binding.apply {
            edName.addTextChangedListener {
                if (binding.edName.text.toString().isEmpty()) {
                    storeTernakViewModel?.isNameFilled?.postValue(false)
                } else {
                    storeTernakViewModel?.isNameFilled?.postValue(true)
                }
            }

            edPanjang.addTextChangedListener {
                if (binding.edPanjang.text.toString().isEmpty()) {
                    storeTernakViewModel?.isPanjangFilled?.postValue(false)
                } else {
                    storeTernakViewModel?.isPanjangFilled?.postValue(true)
                }
            }

            edLebar.addTextChangedListener {
                if (binding.edLebar.text.toString().isEmpty()) {
                    storeTernakViewModel?.isLebarFilled?.postValue(false)
                } else {
                    storeTernakViewModel?.isLebarFilled?.postValue(true)
                }
            }


            edJenisKandang.addTextChangedListener {
                if (binding.edJenisKandang.text.toString().isEmpty()) {
                    storeTernakViewModel?.isJenisKandangFilled?.postValue(false)
                } else {
                    storeTernakViewModel?.isJenisKandangFilled?.postValue(true)
                }
            }

            edProvinsi.addTextChangedListener {
                if (binding.edProvinsi.text.toString().isEmpty()) {
                    storeTernakViewModel?.isProvinceFilled?.postValue(false)
                } else {
                    storeTernakViewModel?.isProvinceFilled?.postValue(true)
                }
            }

            edKabupaten.addTextChangedListener {
                if (binding.edKabupaten.text.toString().isEmpty()) {
                    storeTernakViewModel?.isRegencyFilled?.postValue(false)
                } else {
                    storeTernakViewModel?.isRegencyFilled?.postValue(true)
                }
            }

            edKecamatan.addTextChangedListener {
                if (binding.edKecamatan.text.toString().isEmpty()) {
                    storeTernakViewModel?.isDistrictFilled?.postValue(false)
                } else {
                    storeTernakViewModel?.isDistrictFilled?.postValue(true)
                }
            }

            edDesaKel.addTextChangedListener {
                if (binding.edDesaKel.text.toString().isEmpty()) {
                    storeTernakViewModel?.isVillageFilled?.postValue(false)
                } else {
                    storeTernakViewModel?.isVillageFilled?.postValue(true)
                }
            }

            edAddress.addTextChangedListener {
                if (binding.edAddress.text.toString().isEmpty()) {
                    storeTernakViewModel?.isAddressFilled?.postValue(false)
                } else {
                    storeTernakViewModel?.isAddressFilled?.postValue(true)
                }
            }

            edRtrw.addTextChangedListener {
                if (binding.edRtrw.text.toString().isEmpty()) {
                    storeTernakViewModel?.isRtRwFilled?.postValue(false)
                } else {
                    storeTernakViewModel?.isRtRwFilled?.postValue(true)
                }
            }

//            edLongitude.addTextChangedListener {
//                if (binding.edLongitude.text.toString().isEmpty()) {
//                    storeTernakViewModel?.isLongitudeFilled?.postValue(false)
//                } else {
//                    storeTernakViewModel?.isLongitudeFilled?.postValue(true)
//                }
//            }
//
//            edLatitude.addTextChangedListener {
//                if (binding.edLatitude.text.toString().isEmpty()) {
//                    storeTernakViewModel?.isLatitudeFilled?.postValue(false)
//                } else {
//                    storeTernakViewModel?.isLatitudeFilled?.postValue(true)
//                }
//            }
        }
    }
}