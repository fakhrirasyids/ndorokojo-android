package com.ndorokojo.ui.bottomsheetinputdata.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.ndorokojo.R
import com.ndorokojo.databinding.FragmentProfileBinding
import com.ndorokojo.di.Injection
import com.ndorokojo.ui.bottomsheetinputdata.BottomSheetInputData.Companion.changeFragmentToIndex
import com.ndorokojo.ui.bottomsheetinputdata.BottomSheetInputDataViewModelFactory

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel by viewModels<ProfileViewModel> {
        BottomSheetInputDataViewModelFactory.getInstance(
            Injection.provideApiService(requireContext()),
            Injection.provideUserPreferences(requireContext())
        )
    }

    private lateinit var loadingDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        val inflaterLoading: LayoutInflater = layoutInflater
        val loadingAlert = AlertDialog.Builder(requireContext())
            .setView(inflaterLoading.inflate(R.layout.custom_loading_dialog, null))
            .setCancelable(false)
        loadingDialog = loadingAlert.create()

        observeLoading()
        observeSelectedLocation()
        setData()

        setListeners()

        return binding.root
    }

    private fun observeLoading() {
        profileViewModel.isLoadingAddress.observe(viewLifecycleOwner) {
            if (it) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }
    }

    private fun observeSelectedLocation() {
        profileViewModel.apply {
            listProvince.observe(viewLifecycleOwner) { listProvince ->
                if (listProvince != null) {
                    if (selectedProvinceId.value != null) {
                        for (item in listProvince) {
                            if (selectedProvinceId.value == item.id) {
                                binding.edProvinsi.setText(item.name)
                            }
                        }
                    }
                }
            }

            listRegency.observe(viewLifecycleOwner) { listRegency ->
                if (listRegency != null) {
                    if (selectedRegencyId.value != null) {
                        for (item in listRegency) {
                            if (selectedRegencyId.value == item.id) {
                                binding.edKabupaten.setText(item.name)
                            }
                        }
                    }
                }
            }

            listDistrict.observe(viewLifecycleOwner) { listDistrict ->
                if (listDistrict != null) {
                    if (selectedDistrictId.value != null) {
                        for (item in listDistrict) {
                            if (selectedDistrictId.value == item.id) {
                                binding.edKecamatan.setText(item.name)
                            }
                        }
                    }
                }
            }

            listVillage.observe(viewLifecycleOwner) { listVillage ->
                if (listVillage != null) {
                    if (selectedVillageId.value != null) {
                        for (item in listVillage) {
                            if (selectedVillageId.value == item.id?.toLong()) {
                                binding.edDesaKel.setText(item.name)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setData() {
        binding.apply {
            edId.setText(profileViewModel.getUserCode())
            edName.setText(profileViewModel.getFullname())
            edOccupation.setText(profileViewModel.getUserOccupation())
            edGender.setText(profileViewModel.getUserGender())
            edAge.setText(profileViewModel.getUserAge().toString())
            edKelompokTernak.setText(profileViewModel.getUserKelompokTernak())
        }
    }

    private fun setListeners() {
        binding.btnSave.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setCancelable(false)

            with(builder)
            {
                setTitle("Apakah data peternak sudah benar?")
                setMessage("Jika belum silahkan kembali dan ganti pada menu Edit Profil")
                setPositiveButton("Benar") { dialog, _ ->
                    dialog.dismiss()
                    changeFragmentToIndex(1)
                }
                setNegativeButton("Salah") { dialog, _ ->
                    dialog.dismiss()
                }
                show()
            }
        }
    }
}