package com.ndorokojo.ui.bottomsheetinputdata.fragments.sensor

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
import androidx.fragment.app.viewModels
import com.ndorokojo.R
import com.ndorokojo.databinding.FragmentKandangBinding
import com.ndorokojo.databinding.FragmentSensorBinding
import com.ndorokojo.di.Injection
import com.ndorokojo.ui.bottomsheetinputdata.BottomSheetInputData
import com.ndorokojo.ui.bottomsheetinputdata.BottomSheetInputData.Companion.storeTernakViewModel
import com.ndorokojo.ui.bottomsheetinputdata.BottomSheetInputDataViewModelFactory
import com.ndorokojo.ui.bottomsheetinputdata.fragments.StoreTernakViewModel
import com.ndorokojo.ui.main.tambahkandang.BottomSheetTambahKandang
import com.ndorokojo.ui.map.PickLocationActivity
import com.ndorokojo.utils.Constants

class SensorFragment : Fragment() {
    private lateinit var binding: FragmentSensorBinding

//    private val storeTernakViewModel by viewModels<StoreTernakViewModel> {
//        BottomSheetInputDataViewModelFactory.getInstance(
//            Injection.provideApiService(requireContext()),
//            Injection.provideUserPreferences(requireContext())
//        )
//    }
private val mapLauncher = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
) { result ->
    if (result.resultCode == AppCompatActivity.RESULT_OK) {
        val lat = result.data?.extras?.getDouble(PickLocationActivity.EXTRA_LAT, 0.0)
        val lon = result.data?.extras?.getDouble(PickLocationActivity.EXTRA_LON, 0.0)

        if (lat != null && lon != null) {
            binding.apply {
                storeTernakViewModel?.isSensorLatFilled?.postValue(true)
                storeTernakViewModel?.isSensorLonFilled?.postValue(true)

                storeTernakViewModel?.sensorLat?.postValue(lat.toString())
                storeTernakViewModel?.sensorLon?.postValue(lon.toString())

                layoutLocation.isVisible = false
                layoutShowLocation.isVisible = true

                tvLocationPicked.text = Constants.getAddress(
                    requireContext(),
                    lat,
                    lon
                )
            }
        } else {
            storeTernakViewModel?.isSensorLatFilled?.postValue(false)
            storeTernakViewModel?.isSensorLonFilled?.postValue(false)
        }
    }
}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSensorBinding.inflate(inflater, container, false)

        enableSensorInput(false)
        observeAll()

        setSensorSpinner()
        setListeners()

        return binding.root
    }

    private fun observeAll() {
        storeTernakViewModel?.apply {
            isSensorStatusTerpasang.observe(viewLifecycleOwner) {
                if (it != null) {
                    enableSensorInput(it)
                }
            }
        }
    }

    private fun setSensorSpinner() {
        binding.edStatus.apply {
            val occupationAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayOf(
                    getString(R.string.sensor_status_connected),
                    getString(R.string.sensor_status_not_connected)
                )
            )
            isEnabled = true
            setAdapter(occupationAdapter)
            setOnItemClickListener { _, _, position, _ ->
                if (position == 0) {
                    storeTernakViewModel?.isSensorStatusTerpasang?.postValue(true)
                } else {
                    storeTernakViewModel?.isSensorStatusTerpasang?.postValue(false)
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
                    setTitle("Apakah anda ingin kembali ke Kandang?")
                    setPositiveButton("Ya") { dialog, _ ->
                        dialog.dismiss()
                        BottomSheetInputData.changeFragmentToIndex(1)
                    }
                    setNegativeButton("Tidak") { dialog, _ ->
                        dialog.dismiss()
                    }
                    show()
                }
            }
            layoutLocation.setOnClickListener {
                mapLauncher.launch(
                    Intent(
                        requireContext(),
                        PickLocationActivity::class.java
                    )
                )
            }

            btnClearLocation.setOnClickListener {
                BottomSheetTambahKandang.storeKandangViewModel?.isSensorLatFilled?.postValue(false)
                BottomSheetTambahKandang.storeKandangViewModel?.isSensorLonFilled?.postValue(false)

                BottomSheetTambahKandang.storeKandangViewModel?.sensorLat?.postValue(null)
                BottomSheetTambahKandang.storeKandangViewModel?.sensorLon?.postValue(null)

                layoutLocation.isVisible = true
                layoutShowLocation.isVisible = false
            }

            layoutLocation.setOnClickListener {
                mapLauncher.launch(
                    Intent(
                        requireContext(),
                        PickLocationActivity::class.java
                    )
                )
            }

            btnClearLocation.setOnClickListener {
                storeTernakViewModel?.isSensorLatFilled?.postValue(false)
                storeTernakViewModel?.isSensorLonFilled?.postValue(false)

                storeTernakViewModel?.sensorLat?.postValue(null)
                storeTernakViewModel?.sensorLon?.postValue(null)

                layoutLocation.isVisible = true
                layoutShowLocation.isVisible = false
            }

            btnSave.setOnClickListener {
                if (isValid()) {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setCancelable(false)

                    storeTernakViewModel?.apply {
                        sensorStatus.postValue(binding.edStatus.text.toString())
                        if (isSensorStatusTerpasang.value!!) {
//                            sensorLat.postValue(if (binding.edLatitude.text.isNullOrEmpty()) null else binding.edLatitude.text.toString())
//                            sensorLon.postValue(if (binding.edLongitude.text.isNullOrEmpty()) null else binding.edLongitude.text.toString())
                            sensorBattery.postValue(
                                if (binding.edBattery.text.isNullOrEmpty()) null else Integer.parseInt(
                                    binding.edBattery.text.toString()
                                )
                            )
                            sensorGPS.postValue(if (binding.edGpsType.text.isNullOrEmpty()) null else binding.edGpsType.text.toString())
                            sensorReport.postValue(if (binding.edReport.text.isNullOrEmpty()) null else binding.edReport.text.toString())
                        }
                    }

                    with(builder)
                    {
                        setTitle("Apakah data Sensor sudah benar?")
                        setMessage("Silahkan cek kembali jika terdapat kesalahan data")
                        setPositiveButton("Benar") { dialog, _ ->
                            dialog.dismiss()
                            BottomSheetInputData.changeFragmentToIndex(3)
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

    private fun isValid() = if (binding.edStatus.text.isNullOrEmpty()) {
        alertDialogMessage("Masukkan Sensor Status dengan benar!")
//        binding.edStatusLayout.error = "Masukkan Sensor Status dengan benar!"
        false
    } else if (storeTernakViewModel?.isSensorStatusTerpasang?.value != null && storeTernakViewModel?.isSensorStatusTerpasang?.value!!) {
//        if (binding.edLatitude.text.isNullOrEmpty()) {
//            alertDialogMessage("Masukkan Sensor Latitude dengan benar!")
////            binding.edLatitudeLayout.error = "Masukkan Sensor Latitude dengan benar!"
//            false
//        } else if (binding.edLongitude.text.isNullOrEmpty()) {
//            alertDialogMessage("Masukkan Sensor Longitude dengan benar!")
////            binding.edLongitudeLayout.error = "Masukkan Sensor Longitude dengan benar!"
//            false
//        }
        if (!binding.layoutShowLocation.isVisible) {
            alertDialogMessage("Masukkan Lokasi dengan benar!")
            false
        } else if (binding.edBattery.text.isNullOrEmpty()) {
            alertDialogMessage("Masukkan Sensor Battery Percent dengan benar!")
//            binding.edBatteryLayout.error = "Masukkan Sensor Battery Percent dengan benar!"
            false
        } else if (binding.edGpsType.text.isNullOrEmpty()) {
            alertDialogMessage("Masukkan Sensor GPS Type benar!")
//            binding.edGpsTypeLayout.error = "Masukkan Sensor GPS Type benar!"
            false
        } else if (binding.edReport.text.isNullOrEmpty()) {
            alertDialogMessage("Masukkan Sensor Report dengan benar!")
//            binding.edReportLayout.error = "Masukkan Sensor Report dengan benar!"
            false
        } else {
            true
        }
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
            isSensorStatusFilled.observe(viewLifecycleOwner) {
                binding.edStatusLayout.error = null
            }

//            isSensorLatFilled.observe(viewLifecycleOwner) {
//                binding.edLatitudeLayout.error = null
//            }
//
//            isSensorLonFilled.observe(viewLifecycleOwner) {
//                binding.edLongitudeLayout.error = null
//            }

            isSensorBatteryFilled.observe(viewLifecycleOwner) {
                binding.edBatteryLayout.error = null
            }

            isSensorGPSFilled.observe(viewLifecycleOwner) {
                binding.edGpsTypeLayout.error = null
            }

            isSensorReportFilled.observe(viewLifecycleOwner) {
                binding.edReportLayout.error = null
            }
        }
    }

//    private fun setTextChangedListeners() {
//        binding.apply {
//            edStatus.addTextChangedListener {
//                if (binding.edStatus.text.toString().isEmpty()) {
//                    storeTernakViewModel?.isSensorStatusFilled?.postValue(false)
//                } else {
//                    storeTernakViewModel?.isSensorStatusFilled.postValue(true)
//                }
//            }
//
//            edLongitude.addTextChangedListener {
//                if (binding.edLongitude.text.toString().isEmpty()) {
//                    storeTernakViewModel?.isSensorLonFilled?.postValue(false)
//                } else {
//                    storeTernakViewModel?.isSensorLonFilled?.postValue(true)
//                }
//            }
//
//            edLatitude.addTextChangedListener {
//                if (binding.edLatitude.text.toString().isEmpty()) {
//                    storeTernakViewModel.isSensorLatFilled.postValue(false)
//                } else {
//                    storeTernakViewModel.isSensorLatFilled.postValue(true)
//                }
//            }
//
//            edBattery.addTextChangedListener {
//                if (binding.edBattery.text.toString().isEmpty()) {
//                    storeTernakViewModel.isSensorBatteryFilled.postValue(false)
//                } else {
//                    storeTernakViewModel.isSensorBatteryFilled.postValue(true)
//                }
//            }
//
//            edGpsType.addTextChangedListener {
//                if (binding.edGpsType.text.toString().isEmpty()) {
//                    storeTernakViewModel.isSensorGPSFilled.postValue(false)
//                } else {
//                    storeTernakViewModel.isSensorGPSFilled.postValue(true)
//                }
//            }
//
//
//            edReport.addTextChangedListener {
//                if (binding.edReport.text.toString().isEmpty()) {
//                    storeTernakViewModel.isSensorReportFilled.postValue(false)
//                } else {
//                    storeTernakViewModel.isSensorReportFilled.postValue(true)
//                }
//            }
//        }
//    }

    private fun enableSensorInput(isTerpasang: Boolean) {
        binding.apply {
            if (!isTerpasang) {
                BottomSheetTambahKandang.storeKandangViewModel?.isSensorLatFilled?.postValue(false)
                BottomSheetTambahKandang.storeKandangViewModel?.isSensorLonFilled?.postValue(false)

                BottomSheetTambahKandang.storeKandangViewModel?.sensorLat?.postValue(null)
                BottomSheetTambahKandang.storeKandangViewModel?.sensorLon?.postValue(null)
//                edLatitude.setText("")
//                edLongitude.setText("")
                edBattery.setText("")
                edGpsType.setText("")
                edReport.setText("")
            }

//            edLatitude.isEnabled = isTerpasang
//            edLatitudeLayout.isEnabled = isTerpasang
//            edLongitude.isEnabled = isTerpasang
//            edLongitudeLayout.isEnabled = isTerpasang
            layoutLocation.isEnabled = isTerpasang

            edBattery.isEnabled = isTerpasang
            edBatteryLayout.isEnabled = isTerpasang
            edGpsType.isEnabled = isTerpasang
            edGpsTypeLayout.isEnabled = isTerpasang
            edReport.isEnabled = isTerpasang
            edReportLayout.isEnabled = isTerpasang
        }
    }
}