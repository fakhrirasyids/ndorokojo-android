package com.ndorokojo.ui.main.tambahkandang

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.ndorokojo.R
import com.ndorokojo.databinding.FragmentBottomSheetTambahKandangBinding
import com.ndorokojo.di.Injection
import com.ndorokojo.ui.adapters.SectionPagerTambahKandangAdapter
import com.ndorokojo.ui.bottomsheetinputdata.BottomSheetInputDataViewModelFactory
import com.ndorokojo.ui.main.MainActivity

class BottomSheetTambahKandang : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetTambahKandangBinding? = null

    private val binding get() = _binding!!

    private val storeKandangViewModel by viewModels<StoreKandangViewModel> {
        BottomSheetTambahKandangViewModelFactory.getInstance(
            Injection.provideApiService(requireContext()),
            Injection.provideUserPreferences(requireContext())
        )
    }

    override fun onStart() {
        super.onStart()
        val sheetContainer = requireView().parent as? ViewGroup ?: return
        sheetContainer.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = resources.displayMetrics.heightPixels
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBottomSheetTambahKandangBinding.inflate(inflater, container, false)
        val sectionPagerAdapter = SectionPagerTambahKandangAdapter(requireActivity() as AppCompatActivity)

        BottomSheetTambahKandang.storeKandangViewModel = storeKandangViewModel

        binding.viewPager.adapter = sectionPagerAdapter
        binding.viewPager.isUserInputEnabled = false

        setupBackPressListener()

        bottomSheetViewPager = binding.viewPager


        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = resources.getString(tabTitles[position])
            tab.view.isClickable = false
        }.attach()

        binding.toolbar.setNavigationOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setCancelable(false)

            with(builder)
            {
                setTitle("Apakah anda benar-benar ingin keluar?")
                setPositiveButton("Ya") { _, _ ->
                    dismiss()
                }
                setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }
                show()
            }
        }

        return binding.root
    }

    private fun setupBackPressListener() {
        this.view?.isFocusableInTouchMode = true
        this.view?.requestFocus()
        this.view?.setOnKeyListener { _, keyCode, _ ->
            keyCode == KeyEvent.KEYCODE_BACK
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        (requireActivity() as MainActivity).refreshLayoutData()
        super.onDismiss(dialog)
    }

    companion object {
        var bottomSheetViewPager: ViewPager2? = null

        val tabTitles = intArrayOf(
            R.string.kandang,
            R.string.sensor,
        )

        var bottomSheetTambahKandang: BottomSheetTambahKandang? = null
        var storeKandangViewModel: StoreKandangViewModel? = null

        fun changeFragmentToIndex(index: Int) {
            bottomSheetViewPager?.setCurrentItem(index, true)
        }
    }

}