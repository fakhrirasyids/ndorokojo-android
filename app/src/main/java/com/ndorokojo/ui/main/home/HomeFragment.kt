package com.ndorokojo.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.ndorokojo.R
import com.ndorokojo.databinding.FragmentHomeBinding
import com.ndorokojo.ui.adapters.NewsAdapter
import com.ndorokojo.ui.tambahkandang.TambahKandangActivity
import com.ndorokojo.utils.Constants

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        observeClickedHewan()

        setListeners()
        setHewanListeners()
        setRecyclerViews()
        handleOptionMenuButton()

        return binding.root
    }

    private fun observeClickedHewan() {
        homeViewModel.clickedPetButton.observe(viewLifecycleOwner) { clickedPet ->
            if (clickedPet != null) {
                TransitionManager.beginDelayedTransition(binding.layoutBtnAction, AutoTransition())
                binding.layoutBtnAction.visibility = View.VISIBLE
            } else {
                TransitionManager.beginDelayedTransition(binding.layoutBtnAction, AutoTransition())
                binding.layoutBtnAction.visibility = View.GONE
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            iconExpandable.setOnClickListener {
                homeViewModel.clickedPetButton.postValue(null)

                val flagContentLayout =
                    if (layoutExpandableContent.isVisible) View.GONE else View.VISIBLE

                clearAllPetButton(null)
                iconExpandable.visibility = View.GONE
                btnSapiPerah.visibility = View.VISIBLE

                TransitionManager.beginDelayedTransition(layoutExpandable, AutoTransition())
                layoutExpandableContent.visibility = flagContentLayout
            }


            iconUnexpandable.setOnClickListener {
                homeViewModel.clickedPetButton.postValue(null)

                val flagContentLayout =
                    if (layoutExpandableContent.isVisible) View.GONE else View.VISIBLE

                clearAllPetButton(null)
                iconExpandable.visibility = View.VISIBLE
                btnSapiPerah.visibility = View.GONE

                TransitionManager.beginDelayedTransition(layoutExpandable, AutoTransition())
                layoutExpandableContent.visibility = flagContentLayout
            }
        }
    }

    private fun setHewanListeners() {
        binding.apply {
            btnDomba.setOnClickListener {
                homeViewModel.clickedPetButton.postValue(Constants.Ternak.DOMBA.name)
                clearAllPetButton(it)
            }

            btnKambing.setOnClickListener {
                homeViewModel.clickedPetButton.postValue(Constants.Ternak.KAMBING.name)
                clearAllPetButton(it)
            }

            btnSapiPotong.setOnClickListener {
                homeViewModel.clickedPetButton.postValue(Constants.Ternak.SAPI_POTONG.name)
                clearAllPetButton(it)
            }

            btnSapiPerah.setOnClickListener {
                homeViewModel.clickedPetButton.postValue(Constants.Ternak.SAPI_PERAH.name)
                clearAllPetButton(it)
            }

            btnKerbau.setOnClickListener {
                homeViewModel.clickedPetButton.postValue(Constants.Ternak.KERBAU.name)
                clearAllPetButton(it)
            }

            btnKelinci.setOnClickListener {
                homeViewModel.clickedPetButton.postValue(Constants.Ternak.KELINCI.name)
                clearAllPetButton(it)
            }

            btnItik.setOnClickListener {
                homeViewModel.clickedPetButton.postValue(Constants.Ternak.ITIK.name)
                clearAllPetButton(it)
            }

            btnAyamPetelur.setOnClickListener {
                homeViewModel.clickedPetButton.postValue(Constants.Ternak.AYAM_PETELUR.name)
                clearAllPetButton(it)
            }

            btnEnthok.setOnClickListener {
                homeViewModel.clickedPetButton.postValue(Constants.Ternak.ENTHOK.name)
                clearAllPetButton(it)
            }

            btnAyamPotong.setOnClickListener {
                homeViewModel.clickedPetButton.postValue(Constants.Ternak.AYAM_POTONG.name)
                clearAllPetButton(it)
            }

            btnAyamKampung.setOnClickListener {
                homeViewModel.clickedPetButton.postValue(Constants.Ternak.AYAM_KAMPUNG.name)
                clearAllPetButton(it)
            }

            btnBabi.setOnClickListener {
                homeViewModel.clickedPetButton.postValue(Constants.Ternak.BABI.name)
                clearAllPetButton(it)
            }

            btnPuyuh.setOnClickListener {
                homeViewModel.clickedPetButton.postValue(Constants.Ternak.PUYUH.name)
                clearAllPetButton(it)
            }

            btnBurungKicau.setOnClickListener {
                homeViewModel.clickedPetButton.postValue(Constants.Ternak.BURUNG_KICAU.name)
                clearAllPetButton(it)
            }

            btnMerpati.setOnClickListener {
                homeViewModel.clickedPetButton.postValue(Constants.Ternak.MERPATI.name)
                clearAllPetButton(it)
            }

            btnReptile.setOnClickListener {
                homeViewModel.clickedPetButton.postValue(Constants.Ternak.REPTILE.name)
                clearAllPetButton(it)
            }
        }
    }

    private fun setRecyclerViews() {
        binding.apply {
            val eventAdapter = NewsAdapter()
            rvEvent.adapter = eventAdapter
            rvEvent.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            val todayAdapter = NewsAdapter()
            rvToday.adapter = todayAdapter
            rvToday.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            val digitalFinanceAdapter = NewsAdapter()
            rvDigitalFinance.adapter = digitalFinanceAdapter
            rvDigitalFinance.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun handleOptionMenuButton() {
        binding.apply {
            btnTambahKandang.setOnClickListener {
                val iTambahKandang = Intent(requireContext(), TambahKandangActivity::class.java)
                startActivity(iTambahKandang)
                requireActivity().overridePendingTransition(
                    R.anim.slide_in_up_activity,
                    R.anim.slide_out_up_activity
                );
            }
        }
    }

    private fun clearAllPetButton(view: View?) {
        val color = resources.getColor(android.R.color.transparent)
        binding.apply {
            btnDomba.setBackgroundColor(color)
            btnKambing.setBackgroundColor(color)
            btnSapiPotong.setBackgroundColor(color)
            btnSapiPerah.setBackgroundColor(color)
            btnKerbau.setBackgroundColor(color)
            btnKelinci.setBackgroundColor(color)
            btnItik.setBackgroundColor(color)
            btnAyamPetelur.setBackgroundColor(color)
            btnEnthok.setBackgroundColor(color)
            btnAyamPotong.setBackgroundColor(color)
            btnAyamKampung.setBackgroundColor(color)
            btnBabi.setBackgroundColor(color)
            btnPuyuh.setBackgroundColor(color)
            btnBurungKicau.setBackgroundColor(color)
            btnMerpati.setBackgroundColor(color)
            btnReptile.setBackgroundColor(color)
        }

        view?.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
    }
}