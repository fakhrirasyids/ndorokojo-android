package com.ndorokojo.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.ndorokojo.R
import com.ndorokojo.data.models.DetailKandang
import com.ndorokojo.data.models.Event
import com.ndorokojo.data.models.Kandang
import com.ndorokojo.data.models.RasTernakItem
import com.ndorokojo.data.models.SearchedEventItem
import com.ndorokojo.data.models.SearchedKandangItem
import com.ndorokojo.data.models.SearchedNewsItem
import com.ndorokojo.data.models.TernakItem
import com.ndorokojo.databinding.ActivityMainBinding
import com.ndorokojo.di.Injection
import com.ndorokojo.ui.adapters.KandangAdapter
import com.ndorokojo.ui.adapters.NewsAdapter
import com.ndorokojo.ui.adapters.SearchKandangAdapter
import com.ndorokojo.ui.adapters.SearchNewsAdapter
import com.ndorokojo.ui.adapters.SearchSlidersAdapter
import com.ndorokojo.ui.adapters.SlidersAdapter
import com.ndorokojo.ui.adapters.TernakSeeLessAdapter
import com.ndorokojo.ui.adapters.TernakSeeMoreAdapter
import com.ndorokojo.ui.birth.BirthActivity
import com.ndorokojo.ui.bottomsheetinputdata.BottomSheetInputData
import com.ndorokojo.ui.buy.BuyActivity
import com.ndorokojo.ui.buy.BuyActivity.Companion.EVENT_BUY
import com.ndorokojo.ui.buy.BuyActivity.Companion.IS_FROM_EXTRA
import com.ndorokojo.ui.buy.BuyActivity.Companion.IS_FROM_INDIVIDUAL_BUY_EXTRA
import com.ndorokojo.ui.buy.BuyActivity.Companion.IS_FROM_SEARCH
import com.ndorokojo.ui.buy.BuyActivity.Companion.TERNAK_BUY_LIST
import com.ndorokojo.ui.detailkandang.DetailKandangActivity
import com.ndorokojo.ui.detailkandang.DetailKandangActivity.Companion.EXTRA_KANDANG_ID
import com.ndorokojo.ui.detailnews.DetailNewsActivity
import com.ndorokojo.ui.detailnews.DetailNewsActivity.Companion.KEY_IS_FROM_BREBES
import com.ndorokojo.ui.detailnews.DetailNewsActivity.Companion.KEY_NEWS_ID
import com.ndorokojo.ui.died.DiedActivity
import com.ndorokojo.ui.main.tambahkandang.BottomSheetTambahKandang
import com.ndorokojo.ui.profilesettings.ProfileSettingsActivity
import com.ndorokojo.ui.sell.SellActivity
import com.ndorokojo.utils.Constants.alertDialogMessage
import com.ndorokojo.utils.Result

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel> {
        MainViewModelFactory.getInstance(
            Injection.provideApiService(this),
            Injection.provideUserPreferences(this)
        )
    }

    private val ternakSeeMoreAdapter = TernakSeeMoreAdapter()
    private val ternakSeeLessAdapter = TernakSeeLessAdapter()

    private val eventList = arrayListOf<Event>()

    private lateinit var loadingDialog: AlertDialog

    private val eventAdapter = NewsAdapter()
    private val brebesTodayAdapter = SlidersAdapter()
    private val digitalFinanceAdapter = SlidersAdapter()
    private val kandangAdapter = KandangAdapter()

    private val searchEventAdapter = SearchNewsAdapter()
    private val searchBrebesTodayAdapter = SearchSlidersAdapter()
    private val searchDigitalFinanceAdapter = SearchSlidersAdapter()
    private val searchKandangAdapter = SearchKandangAdapter()

    private val activityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            TransitionManager.beginDelayedTransition(binding.layoutBtnAction, AutoTransition())
            binding.layoutBtnAction.visibility = View.GONE
            mainViewModel.getAllEventList()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val inflater: LayoutInflater = layoutInflater
        val loadingAlert = AlertDialog.Builder(this@MainActivity)
            .setView(inflater.inflate(R.layout.custom_loading_dialog, null))
            .setCancelable(false)
        loadingDialog = loadingAlert.create()

        observeLoading()
        observeError()
        observeErrorText()

        observeUser()
        observeKandangLoading()

        observeEvents()
        observeTernak()
        observeBrebesToday()
        observeDigitalFinance()
        observeSearch()

        observeClickedTernak()

        setSearchView()
        setRecyclerViews()
        setListeners()
        setSwipeRefresh()
    }

    private fun observeLoading() {
        mainViewModel.isLoading.observe(this@MainActivity) {
//            showLoading(it)
            if (it) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }
    }

    private fun observeError() {
        mainViewModel.isErrorLoadingData.observe(this@MainActivity) {
            showError(it)
        }
    }

    private fun observeErrorText() {
        mainViewModel.responseMessage.observe(this@MainActivity) {
            binding.tvErrorMessage.text = StringBuilder("Error: Gagal terkoneksi dengan internet!")
        }
    }

    private fun observeUser() {
        mainViewModel.getUsername().observe(this@MainActivity) {
            if (it != null) {
                binding.tvUser.text = it.split(' ').firstOrNull()
            }
        }
    }

    private fun observeKandangLoading() {
        mainViewModel.isLoadingKandang.observe(this@MainActivity) {
            if (it) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }
    }

    private fun observeEvents() {
        mainViewModel.eventList.observe(this@MainActivity) {
            eventList.clear()
            eventList.addAll(it)

            binding.tvEmptyEvent.text = resources.getString(R.string.tidak_ada_event_saat_ini)
            showEmptyEvent(it.isEmpty())
        }
    }

    private fun observeBrebesToday() {
        mainViewModel.brebesTodayList.observe(this@MainActivity) {
            binding.rvBrebesToday.apply {
                brebesTodayAdapter.setList(it)
                adapter = brebesTodayAdapter
                layoutManager =
                    LinearLayoutManager(
                        this@MainActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
            }

            binding.tvEmptyBrebesToday.text = resources.getString(R.string.tidak_ada_informasi_brebes_today_saat_ini)
            showEmptyBrebesToday(it.isEmpty())
        }
    }

    private fun observeDigitalFinance() {
        mainViewModel.digitalFinanceList.observe(this@MainActivity) {
            binding.rvDigitalFinance.apply {
                digitalFinanceAdapter.setList(it)
                adapter = digitalFinanceAdapter
                layoutManager =
                    LinearLayoutManager(
                        this@MainActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
            }

            binding.tvEmptyDigitalFinance.text = resources.getString(R.string.tidak_ada_informasi_mengenai_digital_finance_saat_ini)
            showEmptyDigitalFinance(it.isEmpty())
        }
    }

    private fun observeTernak() {
        mainViewModel.ternakList.observe(this@MainActivity) { ternakList ->
            if (ternakList.isNotEmpty()) {
                allTernakItem.addAll(ternakList)

                binding.rvEvent.apply {
                    eventAdapter.setList(eventList)
                    adapter = eventAdapter
                    layoutManager =
                        LinearLayoutManager(
                            this@MainActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                }

                mainViewModel.isTernakExpanded.observe(this@MainActivity) { isTernakExpanded ->
                    if (!isTernakExpanded) {
                        val ternakListTemp = arrayListOf<TernakItem>()
                        ternakListTemp.add(ternakList[0])
                        ternakListTemp.add(ternakList[1])
                        ternakListTemp.add(ternakList[2])
                        ternakSeeMoreAdapter.setList(ternakListTemp)
                        binding.rvBtnTernak.adapter = ternakSeeMoreAdapter
                    } else {
                        ternakSeeLessAdapter.setList(ternakList)
                        binding.rvBtnTernak.adapter = ternakSeeLessAdapter
                    }
                }
            }
        }
    }


    private fun observeClickedTernak() {
        mainViewModel.clickedTernakId.observe(this@MainActivity) { clickedTernakId ->
            if (clickedTernakId != null) {
                mainViewModel.getListKandang(clickedTernakId).observe(this@MainActivity) { result ->
                    when (result) {
                        is Result.Loading -> {
                            kandangList.clear()
                            mainViewModel.isLoadingKandang.postValue(true)
                        }

                        is Result.Success -> {
                            kandangList.clear()
                            kandangList.addAll(result.data.kandang as ArrayList<Kandang>)

                            kandangAdapter.setList(kandangList)

                            mainViewModel.isLoadingKandang.postValue(false)
                            mainViewModel.kandangList.postValue(result.data.kandang as ArrayList<Kandang>?)

                            if (result.data.kandang.isNullOrEmpty()) {
                                TransitionManager.beginDelayedTransition(
                                    binding.layoutBtnAction,
                                    AutoTransition()
                                )
                                binding.layoutBtnAction.visibility = View.GONE

                                val builder = AlertDialog.Builder(this@MainActivity)
                                builder.setCancelable(false)

                                with(builder)
                                {
                                    setMessage("Silahkan isi data terlebih dahulu!")
                                    setPositiveButton("OK") { dialog, _ ->
                                        ternakSeeMoreAdapter.changeAllBackgroundWhite(this@MainActivity)
                                        ternakSeeLessAdapter.changeAllBackgroundWhite(this@MainActivity)

                                        val bottomSheetFragment = BottomSheetInputData()
                                        bottomSheetFragment.isCancelable = false
                                        bottomSheetFragment.show(
                                            supportFragmentManager,
                                            "MainActivityBottomSheet"
                                        )
                                        BottomSheetInputData.bottomSheetInputData =
                                            bottomSheetFragment
                                    }
                                    setNegativeButton("Batal") { dialog, _ ->
                                        ternakSeeMoreAdapter.changeAllBackgroundWhite(this@MainActivity)
                                        ternakSeeLessAdapter.changeAllBackgroundWhite(this@MainActivity)

                                        ternak = null
                                        ternakListSpecies.clear()

                                        dialog.dismiss()
                                    }
                                    show()
                                }
                            } else {
                                binding.rvKandang.apply {
                                    adapter = kandangAdapter
                                    layoutManager = LinearLayoutManager(
                                        this@MainActivity,
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                }

                                TransitionManager.beginDelayedTransition(
                                    binding.layoutBtnAction,
                                    AutoTransition()
                                )
                                binding.layoutBtnAction.visibility = View.VISIBLE
                            }
                        }

                        is Result.Error -> {
                            ternakSeeMoreAdapter.changeAllBackgroundWhite(this@MainActivity)
                            ternakSeeLessAdapter.changeAllBackgroundWhite(this@MainActivity)

                            kandangList.clear()
                            mainViewModel.isLoadingKandang.postValue(false)
                            mainViewModel.responseMessage.postValue(result.error)

                            alertDialogMessage(
                                this@MainActivity,
                                "Gagal terkoneksi dengan internet!",
                                "Gagal mengambil data"
                            )
                        }
                    }
                }
            } else {
                TransitionManager.beginDelayedTransition(binding.layoutBtnAction, AutoTransition())
                binding.layoutBtnAction.visibility = View.GONE
            }
        }
    }

    private fun setRecyclerViews() {
        binding.rvBtnTernak.apply {
            ternakSeeMoreAdapter.onTernakClick = { ternakId, ternak, position ->
                TransitionManager.beginDelayedTransition(binding.layoutBtnAction, AutoTransition())
                binding.layoutBtnAction.visibility = View.GONE
                mainViewModel.clickedTernakId.postValue(ternakId)
                mainViewModel.clickedTernak.postValue(ternak)
                ternakSeeMoreAdapter.changeBackgroundColor(
                    this@MainActivity.applicationContext,
                    position
                )
                MainActivity.ternak = null
                ternakListSpecies.clear()
                MainActivity.ternak = ternak

                if (!ternak.rasTernakItem.isNullOrEmpty()) {
                    MainActivity.ternakListSpecies.addAll(ternak.rasTernakItem as ArrayList<RasTernakItem>)
                } else {
                    ternakListSpecies.clear()
                }

            }

            ternakSeeLessAdapter.onTernakClick = { ternakId, ternak, position ->
                TransitionManager.beginDelayedTransition(binding.layoutBtnAction, AutoTransition())
                binding.layoutBtnAction.visibility = View.GONE
                mainViewModel.clickedTernakId.postValue(ternakId)
                mainViewModel.clickedTernak.postValue(ternak)
                ternakSeeLessAdapter.changeBackgroundColor(
                    this@MainActivity.applicationContext,
                    position
                )
                MainActivity.ternak = null
                ternakListSpecies.clear()

                MainActivity.ternak = ternak

                if (!ternak.rasTernakItem.isNullOrEmpty()) {
                    MainActivity.ternakListSpecies.addAll(ternak.rasTernakItem as ArrayList<RasTernakItem>)
                } else {
                    ternakListSpecies.clear()

                }
            }

            ternakSeeMoreAdapter.onSeeMoreClick = {
                mainViewModel.clickedTernakId.postValue(null)
                mainViewModel.clickedTernak.postValue(null)
                mainViewModel.isTernakExpanded.postValue(true)
            }

            ternakSeeLessAdapter.onSeeLessClick = {
                mainViewModel.clickedTernakId.postValue(null)
                mainViewModel.clickedTernak.postValue(null)
                mainViewModel.isTernakExpanded.postValue(false)
            }

            kandangAdapter.onItemClick = { kandangId ->
                val iDetailKandang = Intent(this@MainActivity, DetailKandangActivity::class.java)
                iDetailKandang.putExtra(EXTRA_KANDANG_ID, kandangId)
                activityLauncher.launch(iDetailKandang)
            }

            layoutManager = GridLayoutManager(this@MainActivity, 4)
        }
    }

    private fun setListeners() {
        binding.apply {
            brebesTodayAdapter.onItemClick = { id ->
                val iNewsDetail = Intent(this@MainActivity, DetailNewsActivity::class.java)
                iNewsDetail.putExtra(KEY_NEWS_ID, id)
                iNewsDetail.putExtra(KEY_IS_FROM_BREBES, true)
                startActivity(iNewsDetail)
            }

            digitalFinanceAdapter.onItemClick = { id ->
                val iNewsDetail = Intent(this@MainActivity, DetailNewsActivity::class.java)
                iNewsDetail.putExtra(KEY_NEWS_ID, id)
                activityLauncher.launch(iNewsDetail)
            }

            eventAdapter.onItemClick = { event ->
                val iBuy = Intent(this@MainActivity, BuyActivity::class.java)
                iBuy.putExtra(EVENT_BUY, event)
                iBuy.putExtra(IS_FROM_EXTRA, true)
                activityLauncher.launch(iBuy)
            }

            btnProfile.setOnClickListener {
                val iProfileSettings =
                    Intent(this@MainActivity, ProfileSettingsActivity::class.java)
                activityLauncher.launch(iProfileSettings)
            }

            btnSell.setOnClickListener {
                activityLauncher.launch(Intent(this@MainActivity, SellActivity::class.java))
            }

            btnBuy.setOnClickListener {
                val iBuy = Intent(this@MainActivity, BuyActivity::class.java)
                val iBuyTernakFiltered = arrayListOf<Event>()

                for (item in eventList) {
//                    if (item.acquiredStatus != "BELI") {
                    if (!item.isMine!!) {
                        if (item.livestockType?.level == "1") {
                            if (item.livestockType!!.id == ternak?.id) {
                                iBuyTernakFiltered.add(item)
                            }
                        } else {
                            if (Integer.parseInt(item.livestockType?.parentTypeId.toString()) == ternak?.id) {
                                iBuyTernakFiltered.add(item)
                            }
                        }
//                        }
                    }
                }

                if (iBuyTernakFiltered.isEmpty()) {
                    alertDialogMessage(
                        this@MainActivity,
                        "Tidak ada ${
                            ternak?.livestockType.toString().lowercase()
                        } yang dijual penjual lain saat ini!",
                        "Gagal Buy Ternak"
                    )
                } else {
                    iBuy.putExtra(IS_FROM_INDIVIDUAL_BUY_EXTRA, true)
                    iBuy.putParcelableArrayListExtra(TERNAK_BUY_LIST, iBuyTernakFiltered)
                    activityLauncher.launch(iBuy)
                }
            }

            btnBirth.setOnClickListener {
                activityLauncher.launch(Intent(this@MainActivity, BirthActivity::class.java))
            }

            btnDied.setOnClickListener {
                activityLauncher.launch(Intent(this@MainActivity, DiedActivity::class.java))
            }

            btnTambahKandang.setOnClickListener {
                val bottomSheetTambahKandang = BottomSheetTambahKandang()
                bottomSheetTambahKandang.isCancelable = false
                bottomSheetTambahKandang.show(
                    supportFragmentManager,
                    "MainActivityBottomSheet"
                )
                BottomSheetTambahKandang.bottomSheetTambahKandang =
                    bottomSheetTambahKandang
            }

            btnRetry.setOnClickListener {
                mainViewModel.getAllEventList()
            }
        }
    }

    private fun setSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            mainViewModel.getAllEventList()
            TransitionManager.beginDelayedTransition(
                binding.layoutBtnAction,
                AutoTransition()
            )
            binding.layoutBtnAction.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefresh.isRefreshing = false
            }, 100)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressbar.isVisible = isLoading
            layoutContent.isVisible = !isLoading
            layoutEvent.isVisible = !isLoading
        }
    }

    private fun showError(isError: Boolean) {
        binding.apply {
            layoutError.isVisible = isError
            layoutContent.isVisible = !isError
            swipeRefresh.isEnabled = !isError
            layoutEvent.isVisible = !isError
        }
    }

    private fun showEmptyEvent(isEmpty: Boolean) {
        binding.apply {
            tvEmptyEvent.isVisible = isEmpty
            rvEvent.isVisible = !isEmpty
        }
    }

    private fun showEmptyBrebesToday(isEmpty: Boolean) {
        binding.apply {
            tvEmptyBrebesToday.isVisible = isEmpty
            rvBrebesToday.isVisible = !isEmpty
        }
    }

    private fun showEmptyDigitalFinance(isEmpty: Boolean) {
        binding.apply {
            tvEmptyDigitalFinance.isVisible = isEmpty
            rvDigitalFinance.isVisible = !isEmpty
        }
    }

    fun refreshLayoutData() {
        TransitionManager.beginDelayedTransition(binding.layoutBtnAction, AutoTransition())
        binding.layoutBtnAction.visibility = View.GONE
        mainViewModel.getAllEventList()
    }

    private fun setSearchView() {
        with(binding.svNdoroKojo) {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(keyword: String): Boolean {
                    mainViewModel.searchAnything(keyword)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrEmpty()) {
                        mainViewModel.searchResponse.postValue(null)
                        mainViewModel.getAllEventList()
                    }
                    return true
                }
            })
        }
    }

    private fun observeSearch() {
        mainViewModel.searchResponse.observe(this@MainActivity) { searched ->
            if (searched != null) {
                TransitionManager.beginDelayedTransition(binding.layoutBtnAction, AutoTransition())
                binding.layoutBtnAction.visibility = View.GONE
                binding.rvBtnTernak.isVisible = false
//                binding.rvKandangSearched.isVisible = true

                searchEventAdapter.setList(searched.event as ArrayList<SearchedEventItem>)
//                searchKandangAdapter.setList(searched.kandang as ArrayList<SearchedKandangItem>)
                searchBrebesTodayAdapter.setList(searched.today as ArrayList<SearchedNewsItem>)
                searchDigitalFinanceAdapter.setList(searched.finance as ArrayList<SearchedNewsItem>)

                binding.apply {
                    if (searched.event.isEmpty()) {
                        tvEmptyEvent.text = resources.getString(R.string.tidak_ada_event_search)
                        showEmptyEvent(true)
                    } else {
                        showEmptyEvent(false)
                        rvEvent.apply {
                            adapter = null
                            searchEventAdapter.onItemClick = { event ->
                                val iBuy = Intent(this@MainActivity, BuyActivity::class.java)
                                iBuy.putExtra(EVENT_BUY, event)
                                iBuy.putExtra(IS_FROM_SEARCH, true)
//                                iBuy.putExtra(IS_FROM_EXTRA, true)
                                activityLauncher.launch(iBuy)
                            }
                            adapter = searchEventAdapter
                            layoutManager =
                                LinearLayoutManager(
                                    this@MainActivity,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                        }
                    }

                    if (searched.today.isEmpty()) {
                        tvEmptyBrebesToday.text = resources.getString(R.string.tidak_ada_informasi_brebes_today_search)
                        showEmptyBrebesToday(true)
                    } else {
                        showEmptyBrebesToday(false)
                        rvBrebesToday.apply {
                            adapter = null
                            searchBrebesTodayAdapter.onItemClick = { id ->
                                val iNewsDetail =
                                    Intent(this@MainActivity, DetailNewsActivity::class.java)
                                iNewsDetail.putExtra(KEY_NEWS_ID, id)
                                iNewsDetail.putExtra(KEY_IS_FROM_BREBES, true)
                                startActivity(iNewsDetail)
                            }
                            adapter = searchBrebesTodayAdapter
                            layoutManager =
                                LinearLayoutManager(
                                    this@MainActivity,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                        }
                    }

                    if (searched.finance.isEmpty()) {
                        tvEmptyDigitalFinance.text = resources.getString(R.string.tidak_ada_informasi_mengenai_digital_finance_search)
                        showEmptyDigitalFinance(true)
                    } else {
                        showEmptyDigitalFinance(false)
                        rvDigitalFinance.apply {
                            adapter = null
                            searchDigitalFinanceAdapter.onItemClick = { id ->
                                val iNewsDetail =
                                    Intent(this@MainActivity, DetailNewsActivity::class.java)
                                iNewsDetail.putExtra(KEY_NEWS_ID, id)
                                activityLauncher.launch(iNewsDetail)
                            }
                            adapter = searchDigitalFinanceAdapter
                            layoutManager =
                                LinearLayoutManager(
                                    this@MainActivity,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                        }
                    }

//                    rvKandangSearched.apply {
//                        searchKandangAdapter.onItemClick = { kandangId ->
//                            val iDetailKandang =
//                                Intent(this@MainActivity, DetailKandangActivity::class.java)
//                            iDetailKandang.putExtra(EXTRA_KANDANG_ID, kandangId)
//                            activityLauncher.launch(iDetailKandang)
//                        }
//                        adapter = searchKandangAdapter
//                        layoutManager =
//                            LinearLayoutManager(
//                                this@MainActivity,
//                                LinearLayoutManager.HORIZONTAL,
//                                false
//                            )
//                    }
                }
            } else {
                binding.rvBtnTernak.isVisible = true
//                binding.rvKandangSearched.isVisible = false
            }
        }
    }

    companion object {
        var ternakListSpecies = arrayListOf<RasTernakItem>()
        var ternak: TernakItem? = null

        var allTernakItem = arrayListOf<TernakItem>()

        val kandangList = arrayListOf<Kandang>()
    }
}