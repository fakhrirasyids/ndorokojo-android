package com.ndorokojo.ui.detailkandang

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.ndorokojo.R
import com.ndorokojo.data.models.LivestocksItem
import com.ndorokojo.databinding.ActivityDetailKandangBinding
import com.ndorokojo.di.Injection
import com.ndorokojo.ui.adapters.ReportTernakAdapter
import com.ndorokojo.ui.main.MainViewModel
import com.ndorokojo.ui.main.MainViewModelFactory
import com.ndorokojo.ui.updateTernak.UpdateTernakActivity
import com.ndorokojo.ui.updateTernak.UpdateTernakActivity.Companion.EXTRA_TERNAK_ID
import com.ndorokojo.ui.updateTernak.UpdateTernakActivity.Companion.EXTRA_TERNAK_NAME
import com.ndorokojo.utils.Constants.alertDialogMessage

class DetailKandangActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailKandangBinding
    private val kandangId by lazy { intent.getIntExtra(EXTRA_KANDANG_ID, -1) }
    private val detailViewModel by viewModels<DetailKandangViewModel> {
        DetailKandangViewModelFactory(
            Injection.provideApiService(this),
            kandangId
        )
    }

    private val availableAdapter = ReportTernakAdapter()
    private val soldAdapter = ReportTernakAdapter()
    private val deadAdapter = ReportTernakAdapter()

    private val activityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            detailViewModel.getDetailKandang(kandangId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailKandangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeLoading()
        observeError()
        observeReportData()
        observeTernakList()

        observeShowableTernak()

        setListeners()
        setRecyclerViews()
    }

    private fun observeLoading() {
        detailViewModel.isLoading.observe(this@DetailKandangActivity) {
            showParentLoading(it)
        }
    }

    private fun observeError() {
        detailViewModel.isErrorFetchingDetailKandang.observe(this@DetailKandangActivity) {
            showParentError(it)
        }
    }

    private fun observeReportData() {
        detailViewModel.detailKandang.observe(this@DetailKandangActivity) { kandangDetail ->
            if (kandangDetail != null) {
                binding.apply {
                    tvKandangName.text = kandangDetail.name
                    tvTernakAvailable.text = kandangDetail.livestocksCount
                    tvTotalTernak.text = kandangDetail.statistic?.total.toString()
                    tvTernakBeli.text = kandangDetail.statistic?.beli.toString()
                    tvTernakDijual.text = kandangDetail.statistic?.jual.toString()
                    tvTernakMati.text = kandangDetail.statistic?.mati.toString()

                    detailViewModel.listTernak.postValue(kandangDetail.livestocks as ArrayList<LivestocksItem>?)
                }
            }
        }
    }

    private fun observeTernakList() {
        detailViewModel.listTernak.observe(this@DetailKandangActivity) { ternakList ->
            if (ternakList != null) {
                if (ternakList.isNotEmpty()) {
                    val ternakAvailable = arrayListOf<LivestocksItem>()
                    val ternakSold = arrayListOf<LivestocksItem>()
                    val ternakDead = arrayListOf<LivestocksItem>()

                    for (ternak in ternakList) {
                        if (ternak.deadMonth != null) {
                            ternak.acquiredStatus = "MATI"
                            ternakDead.add(ternak)
                        } else if (ternak.soldDealPrice != null) {
                            ternak.acquiredStatus = "TERJUAL"
                            ternakSold.add(ternak)
                        } else {
                            if (ternak.soldProposedPrice != null) {
                                ternak.acquiredStatus = "SEDANG DIJUAL"
                            }
                            ternakAvailable.add(ternak)
                        }
                    }

                    detailViewModel.listTernakAvailable.postValue(ternakAvailable)
                    detailViewModel.listTernakSold.postValue(ternakSold)
                    detailViewModel.listTernakDead.postValue(ternakDead)
                }
            }
        }
    }

    private fun observeShowableTernak() {
        detailViewModel.apply {
            listTernakAvailable.observe(this@DetailKandangActivity) {
                availableAdapter.setList(it)

                binding.apply {
                    tvNoAvailableData.isVisible = it.isEmpty()
                    rvTernakAvailable.isVisible = it.isNotEmpty()
                }
            }

            listTernakSold.observe(this@DetailKandangActivity) {
                soldAdapter.setList(it)

                binding.apply {
                    tvNoAvailableSold.isVisible = it.isEmpty()
                    rvTernakSold.isVisible = it.isNotEmpty()
                }
            }

            listTernakDead.observe(this@DetailKandangActivity) {
                deadAdapter.setList(it)

                binding.apply {
                    tvNoAvailableDead.isVisible = it.isEmpty()
                    rvTernakDead.isVisible = it.isNotEmpty()
                }
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            toolbar.setNavigationOnClickListener { onBackPressed() }

            btnRetry.setOnClickListener {
                detailViewModel.getDetailKandang(kandangId)
            }

            availableAdapter.onItemClick = { ternakName, ternakId ->
                val iUpdateTernak =
                    Intent(this@DetailKandangActivity, UpdateTernakActivity::class.java)
                iUpdateTernak.putExtra(EXTRA_TERNAK_ID, ternakId)
                iUpdateTernak.putExtra(EXTRA_TERNAK_NAME, ternakName)
                activityLauncher.launch(iUpdateTernak)
            }

            soldAdapter.onItemClick = { _, _ ->
                alertDialogMessage(
                    this@DetailKandangActivity,
                    "Update Ternak ke Terjual hanya bisa dilakukan pada ternak Tersedia!",
                    "Peringatan"
                )
            }

            deadAdapter.onItemClick = { _, _ ->
                alertDialogMessage(
                    this@DetailKandangActivity,
                    "Update Ternak ke Terjual hanya bisa dilakukan pada ternak Tersedia!",
                    "Peringatan"
                )
            }
        }
    }

    private fun setRecyclerViews() {
        binding.rvTernakAvailable.apply {
            adapter = availableAdapter
            layoutManager = LinearLayoutManager(this@DetailKandangActivity)
        }

        binding.rvTernakSold.apply {
            adapter = soldAdapter
            layoutManager = LinearLayoutManager(this@DetailKandangActivity)
        }

        binding.rvTernakDead.apply {
            adapter = deadAdapter
            layoutManager = LinearLayoutManager(this@DetailKandangActivity)
        }
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


    override fun onBackPressed() {
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
        super.onBackPressed()
    }

    companion object {
        const val EXTRA_KANDANG_ID = "extra_kandang_id"
    }
}