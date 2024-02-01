package com.ndorokojo.ui.updateprofile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.ndorokojo.R
import com.ndorokojo.databinding.ActivityUpdateProfileBinding
import com.ndorokojo.di.Injection
import com.ndorokojo.ui.main.MainActivity
import com.ndorokojo.ui.splash.SplashActivity
import com.ndorokojo.utils.Constants.IS_FROM_AUTH
import com.ndorokojo.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfileBinding

    private val isFromAuth by lazy { intent.getBooleanExtra(IS_FROM_AUTH, false) }

    private val updateProfileViewModel by viewModels<UpdateProfileViewModel> {
        UpdateProfileViewModelFactory(
            Injection.provideApiService(this),
            Injection.provideUserPreferences(this)
        )
    }

    private lateinit var loadingDialog: AlertDialog

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!isFromAuth) {
            binding.toolbar.title = getString(R.string.edit_profile)
            binding.toolbar.navigationIcon = getDrawable(R.drawable.ic_back)
        }

        val inflater: LayoutInflater = layoutInflater
        val loadingAlert = AlertDialog.Builder(this@UpdateProfileActivity)
            .setView(inflater.inflate(R.layout.custom_loading_dialog, null))
            .setCancelable(true)
        loadingDialog = loadingAlert.create()

        observeAll()
        observeTextChanged()

        setGenderOccupationSpinner()

        setTextChangedListeners()
        setListeners()
    }

    private fun observeAll() {
        updateProfileViewModel.apply {
            isLoadingParent.observe(this@UpdateProfileActivity) {
                showParentLoading(it)
            }

            isLoadingAddress.observe(this@UpdateProfileActivity) {
                if (it) {
                    loadingDialog.show()
                } else {
                    loadingDialog.dismiss()
                }
            }

            isErrorFetchingProfileInfo.observe(this@UpdateProfileActivity) {
                showParentError(it)
            }

            userProfileInfo.observe(this@UpdateProfileActivity) { profile ->
                if (profile != null) {
                    binding.apply {
                        edUsername.setText(profile.username ?: "")
                        edName.setText(profile.fullname ?: "")
                        edEmail.setText(profile.email ?: "")
                        edPhoneNumber.setText(profile.phone ?: "")
                        edAddress.setText(profile.address ?: "")

                        if (profile.occupation != null) {
                            edOccupation.setText(profile.occupation)
                        } else {
                            edOccupation.hint = "Pilih Pekerjaan"
                        }

                        if (profile.gender != null) {
                            edGender.setText(profile.gender)
                        } else {
                            edGender.hint = "Pilih Jenis Kelamin"
                        }

                        edAge.setText(
                            (if (profile.age == null) "" else profile.age.toString())
                        )
                        edKelompokTernak.setText(profile.kelompokTernak ?: "")

                        selectedProvinceId.postValue(
                            if (profile.provinceId != null) Integer.parseInt(
                                profile.provinceId.toString()
                            ) else null
                        )
                        selectedRegencyId.postValue(
                            if (profile.regencyId != null) Integer.parseInt(
                                profile.regencyId.toString()
                            ) else null
                        )
                        selectedDistrictId.postValue(
                            if (profile.districtId != null) Integer.parseInt(
                                profile.districtId.toString()
                            ) else null
                        )
                        selectedVillageId.postValue(
                            if (profile.villageId != null) profile.villageId.toString()
                                .toLong() else null
                        )

//                        listProvince.observe(this@UpdateProfileActivity) { listProvince ->
//                            if (listProvince != null) {
//                                if (selectedProvinceId.value != null) {
//                                    for (item in listProvince) {
//                                        if (selectedProvinceId.value == item.id) {
//                                            edProvinsi.setText(item.name)
//                                        }
//                                    }
//                                } else {
//                                    edProvinsi.hint = "Pilih Provinsi"
//                                }
//                            } else {
//                                edProvinsi.hint = "Pilih Provinsi"
//                            }
//                        }

//                        listRegency.observe(this@UpdateProfileActivity) { listRegency ->
//                            if (listRegency != null) {
//                                if (selectedRegencyId.value != null) {
//                                    for (item in listRegency) {
//                                        if (selectedRegencyId.value == item.id) {
//                                            edKabupaten.setText(item.name)
//                                        }
//                                    }
//                                } else {
//                                    edKabupaten.hint = "Pilih Kota/Kabupaten"
//                                }
//                            } else {
//                                edKabupaten.hint = "Pilih Kota/Kabupaten"
//                            }
//                        }

//                        listDistrict.observe(this@UpdateProfileActivity) { listDistrict ->
//                            if (listDistrict != null) {
//                                if (selectedDistrictId.value != null) {
//                                    for (item in listDistrict) {
//                                        if (selectedDistrictId.value == item.id) {
//                                            edKecamatan.setText(item.name)
//                                        }
//                                    }
//                                } else {
//                                    edKecamatan.hint = "Pilih Kecamatan"
//                                }
//                            } else {
//                                edKecamatan.hint = "Pilih Kecamatan"
//                            }
//                        }

//                        listVillage.observe(this@UpdateProfileActivity) { listVillage ->
//                            if (listVillage != null) {
//                                if (selectedVillageId.value != null) {
//                                    for (item in listVillage) {
//                                        if (selectedVillageId.value == item.id?.toLong()) {
//                                            edDesaKel.setText(item.name)
//                                        }
//                                    }
//                                } else {
//                                    edDesaKel.hint = "Pilih Desa/Kelurahan"
//                                }
//                            } else {
//                                edDesaKel.hint = "Pilih Desa/Kelurahan"
//                            }
//                        }
                    }
                }
            }

            listProvince.observe(this@UpdateProfileActivity) { listProvince ->
                if (listProvince != null) {
                    if (selectedProvinceId.value != null) {
                        for (item in listProvince) {
                            if (selectedProvinceId.value == item.id) {
                                binding.edProvinsi.setText(item.name)
                            }
                        }
                    }

                    binding.edProvinsi.isEnabled = true
                    val listProvinceString = arrayListOf<String>()
                    for (item in listProvince) {
                        listProvinceString.add(item.name.toString())
                    }

                    val provinceAdapter = ArrayAdapter(
                        this@UpdateProfileActivity,
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
                                edKabupaten.setAdapter(null)
                                edKecamatan.setText("")
                                edKecamatan.setAdapter(null)
                                edDesaKel.setText("")
                                edDesaKel.setAdapter(null)
                            }
                        }
                    }
                }
            }

            listRegency.observe(this@UpdateProfileActivity) { listRegency ->
                if (listRegency != null) {
                    if (selectedRegencyId.value != null) {
                        for (item in listRegency) {
                            if (selectedRegencyId.value == item.id) {
                                binding.edKabupaten.setText(item.name)
                            }
                        }
                    }

                    binding.edKabupaten.isEnabled = true
                    val listRegencyString = arrayListOf<String>()
                    for (item in listRegency) {
                        listRegencyString.add(item.name.toString())
                    }

                    val regencyAdapter = ArrayAdapter(
                        this@UpdateProfileActivity,
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
                                edKecamatan.setAdapter(null)
                                edDesaKel.setText("")
                                edDesaKel.setAdapter(null)
                            }
                        }
                    }
                }
            }

            listDistrict.observe(this@UpdateProfileActivity) { listDistrict ->
                if (listDistrict != null) {
                    if (selectedDistrictId.value != null) {
                        for (item in listDistrict) {
                            if (selectedDistrictId.value == item.id) {
                                binding.edKecamatan.setText(item.name)
                            }
                        }
                    }

                    binding.edKecamatan.isEnabled = true
                    val listDistrictString = arrayListOf<String>()
                    for (item in listDistrict) {
                        listDistrictString.add(item.name.toString())
                    }

                    val districtAdapter = ArrayAdapter(
                        this@UpdateProfileActivity,
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
                                edDesaKel.setAdapter(null)
                            }
                        }
                    }
                }
            }

            listVillage.observe(this@UpdateProfileActivity) { listVillage ->
                if (listVillage != null) {
                    if (selectedVillageId.value != null) {
                        for (item in listVillage) {
                            if (selectedVillageId.value == item.id?.toLong()) {
                                binding.edDesaKel.setText(item.name)
                            }
                        }
                    }

                    binding.edDesaKel.isEnabled = true
                    val listVillageString = arrayListOf<String>()
                    for (item in listVillage) {
                        listVillageString.add(item.name.toString())
                    }

                    val villageAdapter = ArrayAdapter(
                        this@UpdateProfileActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        listVillageString
                    )
                    binding.edDesaKel.apply {
                        setAdapter(villageAdapter)
                        setOnItemClickListener { _, _, position, _ ->
                            selectedVillageId.postValue(listVillage[position].id!!.toLong())
                        }
                    }
                }
            }
        }
    }

    private fun setGenderOccupationSpinner() {
        binding.apply {
            val occupationAdapter = ArrayAdapter(
                this@UpdateProfileActivity,
                android.R.layout.simple_spinner_dropdown_item,
                arrayOf(
                    getString(R.string.occupation_peternak),
                    getString(R.string.occupation_pedagang_peternak)
                )
            )
            edOccupation.isEnabled = true
            edOccupation.setAdapter(occupationAdapter)

            val genderAdapter = ArrayAdapter(
                this@UpdateProfileActivity,
                android.R.layout.simple_spinner_dropdown_item,
                arrayOf(getString(R.string.gender_laki_laki), getString(R.string.gender_perempuan))
            )
            edGender.isEnabled = true
            edGender.setAdapter(genderAdapter)
        }
    }

    private fun setListeners() {
        binding.apply {
            toolbar.setNavigationOnClickListener { finish() }

            edKabupaten.setOnClickListener {
                if (updateProfileViewModel.selectedProvinceId.value == null) {
                    alertDialogMessage("Pilih provinsi terlebih dahulu!")
                }
            }

            edKecamatan.setOnClickListener {
                if (updateProfileViewModel.selectedRegencyId.value == null) {
                    alertDialogMessage("Pilih Kota/Kabupaten terlebih dahulu!")
                }
            }

            edDesaKel.setOnClickListener {
                if (updateProfileViewModel.selectedDistrictId.value == null) {
                    alertDialogMessage("Pilih Kecamatan terlebih dahulu!")
                }
            }

            btnSave.setOnClickListener {
                if (isValid()) {
                    val username = binding.edUsername.text.toString()
                    val fullname = binding.edName.text.toString()
                    val email = binding.edEmail.text.toString()
                    val phone = binding.edPhoneNumber.text.toString()
                    val address = binding.edAddress.text.toString()
                    val occupation = binding.edOccupation.text.toString()
                    val gender = binding.edGender.text.toString()
                    val age = Integer.parseInt(binding.edAge.text.toString())
                    val kelompokTernak = binding.edKelompokTernak.text.toString()
                    val province = updateProfileViewModel.selectedProvinceId.value!!
                    val regency = updateProfileViewModel.selectedRegencyId.value!!
                    val district = updateProfileViewModel.selectedDistrictId.value!!
                    val village = updateProfileViewModel.selectedVillageId.value!!

                    lifecycleScope.launch(Dispatchers.Main) {
                        updateProfileViewModel.updateUser(
                            fullname,
                            username,
                            email,
                            phone,
                            address,
                            occupation,
                            gender,
                            age.toString(),
                            kelompokTernak,
                            province.toString(),
                            regency.toString(),
                            district.toString(),
                            village.toString()
                        ).observe(this@UpdateProfileActivity) { result ->
                            when (result) {
                                is Result.Loading -> {
                                    showSavingProfileLoading(true)
                                }

                                is Result.Success -> {
                                    updateProfileViewModel.savePreferences(
                                        username = result.data.payload?.username.toString(),
                                        fullname = result.data.payload?.fullname.toString(),
                                        email = result.data.payload?.email.toString(),
                                        code = result.data.payload?.code.toString(),
                                        phone = result.data.payload?.phone.toString(),
                                        address = result.data.payload?.address.toString(),
                                        occupation = result.data.payload?.occupation.toString(),
                                        gender = result.data.payload?.gender.toString(),
                                        age = result.data.payload?.age!!,
                                        kelompokTernak = result.data.payload.kelompokTernak.toString(),
                                        provinceId = result.data.payload.provinceId!!,
                                        regencyId = result.data.payload.regencyId!!,
                                        districtId = result.data.payload.districtId!!,
                                        villageId = result.data.payload.villageId!!,
                                        isProfileComplete = result.data.payload.isProfileComplete!!
                                    )
                                    showSavingProfileLoading(false)

                                    val builder = AlertDialog.Builder(this@UpdateProfileActivity)
                                    builder.setCancelable(false)

                                    with(builder)
                                    {
                                        setMessage("Sukses Update Profile!")
                                        setPositiveButton("OK") { _, _ ->
                                            if (!isFromAuth) {
                                                finish()
                                            } else {
                                                val iSplash = Intent(
                                                    this@UpdateProfileActivity,
                                                    SplashActivity::class.java
                                                )
//                                            iMain.putExtra(USER_ACCESS_TOKEN, accessToken)
                                                finishAffinity()
                                                startActivity(iSplash)
                                            }
                                        }
                                        show()
                                    }

                                }

                                is Result.Error -> {
                                    showSavingProfileLoading(false)
                                    alertDialogMessage("Gagal : ${result.error}")
                                }
                            }
                        }
                    }
                }
            }

            btnRetry.setOnClickListener {
                updateProfileViewModel.getProfileInfo()
            }
        }
    }

    private fun isValid() = if (binding.edUsername.text.isNullOrEmpty()) {
//        binding.edUsernameLayout.error = "Masukkan Username dengan benar!"
        alertDialogMessage("Masukkan Username dengan benar!")
        false
    } else if (binding.edName.text.isNullOrEmpty()) {
        alertDialogMessage("Masukkan Nama dengan benar!")
//        binding.edNameLayout.error = "Masukkan Nama dengan benar!"
        false
    } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.edEmail.text.toString())
            .matches() || binding.edEmail.text.isNullOrEmpty()
    ) {
        alertDialogMessage("Masukkan Email dengan benar!")
//        binding.edEmailLayout.error = "Masukkan Email dengan benar!"
        false
    } else if (binding.edPhoneNumber.text.isNullOrEmpty()) {
        alertDialogMessage("Masukkan No.Hp dengan benar!")
//        binding.edPhoneLayout.error = "Masukkan No.Hp dengan benar!"
        false
    } else if (binding.edAddress.text.isNullOrEmpty()) {
        alertDialogMessage("Masukkan Alamat dengan benar!")
//        binding.edAddressLayout.error = "Masukkan Alamat dengan benar!"
        false
    } else if (binding.edOccupation.text.isNullOrEmpty()) {
        alertDialogMessage("Masukkan Pekerjaan dengan benar!")
//        binding.edOccupationLayout.error = "Masukkan Pekerjaan dengan benar!"
        false
    } else if (binding.edGender.text.isNullOrEmpty()) {
        alertDialogMessage("Masukkan Jenis Kelamin dengan benar!")
//        binding.edGenderLayout.error = "Masukkan Jenis Kelamin dengan benar!"
        false
    } else if (binding.edAge.text.isNullOrEmpty()) {
        alertDialogMessage("Masukkan Umur dengan benar!")
//        binding.edAgeLayout.error = "Masukkan Umur dengan benar!"
        false
    } else if (binding.edKelompokTernak.text.isNullOrEmpty()) {
        alertDialogMessage("Masukkan Kelompok Ternak dengan benar!")
//        binding.edKelompokTernakLayout.error = "Masukkan Kelompok Ternak dengan benar!"
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
    } else {
        true
    }

    private fun showParentLoading(isParentLoading: Boolean) {
        binding.apply {
            parentProgressbar.isVisible = isParentLoading
            layoutContent.isVisible = !isParentLoading
        }
    }

    private fun showParentError(isError: Boolean) {
        binding.apply {
            layoutError.isVisible = isError
            layoutContent.isVisible = !isError
        }
    }

    private fun alertDialogMessage(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)

        with(builder)
        {
            setMessage(message)
            setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            show()
        }
    }

    private fun setTextChangedListeners() {
        binding.apply {
            edUsername.addTextChangedListener {
                if (binding.edUsername.text.toString().isEmpty()) {
                    updateProfileViewModel.isUsernameFilled.postValue(false)
                } else {
                    updateProfileViewModel.isUsernameFilled.postValue(true)
                }
            }

            edName.addTextChangedListener {
                if (binding.edName.text.toString().isEmpty()) {
                    updateProfileViewModel.isFullnameFilled.postValue(false)
                } else {
                    updateProfileViewModel.isFullnameFilled.postValue(true)
                }
            }

            edEmail.addTextChangedListener {
                if (binding.edEmail.text.toString().isEmpty()) {
                    updateProfileViewModel.isEmailFilled.postValue(false)
                } else {
                    updateProfileViewModel.isEmailFilled.postValue(true)
                }
            }

            edPhoneNumber.addTextChangedListener {
                if (binding.edPhoneNumber.text.toString().isEmpty()) {
                    updateProfileViewModel.isPhoneFilled.postValue(false)
                } else {
                    updateProfileViewModel.isPhoneFilled.postValue(true)
                }
            }

            edAddress.addTextChangedListener {
                if (binding.edAddress.text.toString().isEmpty()) {
                    updateProfileViewModel.isAddressFilled.postValue(false)
                } else {
                    updateProfileViewModel.isAddressFilled.postValue(true)
                }
            }

            edOccupation.addTextChangedListener {
                if (binding.edOccupation.text.toString().isEmpty()) {
                    updateProfileViewModel.isOccupationFilled.postValue(false)
                } else {
                    updateProfileViewModel.isOccupationFilled.postValue(true)
                }
            }

            edGender.addTextChangedListener {
                if (binding.edGender.text.toString().isEmpty()) {
                    updateProfileViewModel.isGenderFilled.postValue(false)
                } else {
                    updateProfileViewModel.isGenderFilled.postValue(true)
                }
            }

            edAge.addTextChangedListener {
                if (binding.edAge.text.toString().isEmpty()) {
                    updateProfileViewModel.isAgeFilled.postValue(false)
                } else {
                    updateProfileViewModel.isAgeFilled.postValue(true)
                }
            }

            edKelompokTernak.addTextChangedListener {
                if (binding.edKelompokTernak.text.toString().isEmpty()) {
                    updateProfileViewModel.isKelompokTernakFilled.postValue(false)
                } else {
                    updateProfileViewModel.isKelompokTernakFilled.postValue(true)
                }
            }

            edProvinsi.addTextChangedListener {
                if (binding.edProvinsi.text.toString().isEmpty()) {
                    updateProfileViewModel.isProvinceFilled.postValue(false)
                } else {
                    updateProfileViewModel.isProvinceFilled.postValue(true)
                }
            }

            edKabupaten.addTextChangedListener {
                if (binding.edKabupaten.text.toString().isEmpty()) {
                    updateProfileViewModel.isRegencyFilled.postValue(false)
                } else {
                    updateProfileViewModel.isRegencyFilled.postValue(true)
                }
            }

            edKecamatan.addTextChangedListener {
                if (binding.edKecamatan.text.toString().isEmpty()) {
                    updateProfileViewModel.isDistrictFilled.postValue(false)
                } else {
                    updateProfileViewModel.isDistrictFilled.postValue(true)
                }
            }

            edDesaKel.addTextChangedListener {
                if (binding.edDesaKel.text.toString().isEmpty()) {
                    updateProfileViewModel.isVillageFilled.postValue(false)
                } else {
                    updateProfileViewModel.isVillageFilled.postValue(true)
                }
            }
        }
    }

    private fun observeTextChanged() {
        updateProfileViewModel.apply {
            isUsernameFilled.observe(this@UpdateProfileActivity) {
                binding.edUsernameLayout.error = null
            }

            isFullnameFilled.observe(this@UpdateProfileActivity) {
                binding.edNameLayout.error = null
            }

            isEmailFilled.observe(this@UpdateProfileActivity) {
                binding.edEmailLayout.error = null
            }

            isPhoneFilled.observe(this@UpdateProfileActivity) {
                binding.edPhoneLayout.error = null
            }

            isAddressFilled.observe(this@UpdateProfileActivity) {
                binding.edAddressLayout.error = null
            }

            isOccupationFilled.observe(this@UpdateProfileActivity) {
                binding.edOccupationLayout.error = null
            }

            isGenderFilled.observe(this@UpdateProfileActivity) {
                binding.edGenderLayout.error = null
            }

            isAgeFilled.observe(this@UpdateProfileActivity) {
                binding.edAgeLayout.error = null
            }

            isKelompokTernakFilled.observe(this@UpdateProfileActivity) {
                binding.edKelompokTernakLayout.error = null
            }

            isProvinceFilled.observe(this@UpdateProfileActivity) {
                binding.edProvinsiLayout.error = null
            }

            isRegencyFilled.observe(this@UpdateProfileActivity) {
                binding.edKabupatenLayout.error = null
            }

            isDistrictFilled.observe(this@UpdateProfileActivity) {
                binding.edKecamatanLayout.error = null
            }

            isVillageFilled.observe(this@UpdateProfileActivity) {
                binding.edDesaKelLayout.error = null
            }
        }
    }

    private fun showSavingProfileLoading(isLoading: Boolean) {
        binding.apply {
            progressbar.isVisible = isLoading
            btnSave.isVisible = !isLoading
            edUsernameLayout.isEnabled = !isLoading
            edNameLayout.isEnabled = !isLoading
            edEmailLayout.isEnabled = !isLoading
            edPhoneLayout.isEnabled = !isLoading
            edAddressLayout.isEnabled = !isLoading
            edOccupationLayout.isEnabled = !isLoading
            edGenderLayout.isEnabled = !isLoading
            edAgeLayout.isEnabled = !isLoading
            edKelompokTernakLayout.isEnabled = !isLoading
            edProvinsiLayout.isEnabled = !isLoading
            edKabupatenLayout.isEnabled = !isLoading
            edKecamatanLayout.isEnabled = !isLoading
            edDesaKelLayout.isEnabled = !isLoading
        }
    }
}
