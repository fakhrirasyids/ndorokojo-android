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
import com.ndorokojo.data.models.ArticlesItem
import com.ndorokojo.data.models.EventsItem
import com.ndorokojo.data.models.Kandang
import com.ndorokojo.data.models.RasTernakItem
import com.ndorokojo.data.models.TernakItem
import com.ndorokojo.databinding.ActivityMainBinding
import com.ndorokojo.di.Injection
import com.ndorokojo.ui.adapters.KandangAdapter
import com.ndorokojo.ui.adapters.NewsAdapter
import com.ndorokojo.ui.adapters.SearchSlidersAdapter
import com.ndorokojo.ui.adapters.SliderCategoriesAdapter
import com.ndorokojo.ui.adapters.TernakSeeLessAdapter
import com.ndorokojo.ui.adapters.TernakSeeMoreAdapter
import com.ndorokojo.ui.birth.BirthActivity
import com.ndorokojo.ui.bottomsheetinputdata.BottomSheetInputData
import com.ndorokojo.ui.buy.BuyActivity.Companion.EVENT_BUY
import com.ndorokojo.ui.buylist.BuyListActivity
import com.ndorokojo.ui.buylist.BuyListActivity.Companion.TERNAK_EVENT_LIST
import com.ndorokojo.ui.detailkandang.DetailKandangActivity
import com.ndorokojo.ui.detailkandang.DetailKandangActivity.Companion.EXTRA_KANDANG_ID
import com.ndorokojo.ui.detailnews.DetailNewsActivity
import com.ndorokojo.ui.died.DiedActivity
import com.ndorokojo.ui.main.tambahkandang.BottomSheetTambahKandang
import com.ndorokojo.ui.newbuy.NewBuyActivity
import com.ndorokojo.ui.notif.NotificationActivity
import com.ndorokojo.ui.profilesettings.ProfileSettingsActivity
import com.ndorokojo.ui.sell.SellActivity
import com.ndorokojo.utils.Constants.alertDialogMessage
import com.ndorokojo.utils.Result

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel> {
        MainViewModelFactory(
            Injection.provideApiService(this),
            Injection.provideUserPreferences(this)
        )
    }

    private val ternakSeeMoreAdapter = TernakSeeMoreAdapter()
    private val ternakSeeLessAdapter = TernakSeeLessAdapter()

    private val eventList = arrayListOf<EventsItem>()

    private lateinit var loadingDialog: AlertDialog

    private val eventAdapter = NewsAdapter()
    private val sliderCategoriesAdapter = SliderCategoriesAdapter()
    private val kandangAdapter = KandangAdapter()

    private val searchEventAdapter = NewsAdapter()
    private val searchSliderItemAdapter = SearchSlidersAdapter()

    private val activityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            TransitionManager.beginDelayedTransition(binding.layoutBtnAction, AutoTransition())
            binding.layoutBtnAction.visibility = View.GONE
            binding.svNdoroKojo.setQuery("", false)
            binding.svNdoroKojo.clearFocus()
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
        observeSliderCategories()
        observeSearch()

        observeClickedTernak()

        setSearchView()
        setRecyclerViews()
        setListeners()
        setSwipeRefresh()
    }

    private fun observeLoading() {
        mainViewModel.isLoading.observe(this@MainActivity) {
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

    private fun observeSliderCategories() {
        mainViewModel.sliderCategoryList.observe(this@MainActivity) {
            sliderCategoriesAdapter.setList(it)
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
        binding.apply {

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

            rvBtnTernak.layoutManager = GridLayoutManager(this@MainActivity, 4)

            rvSliderCategory.adapter = sliderCategoriesAdapter
            rvSliderCategory.layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun setListeners() {
        binding.apply {

            btnNotif.setOnClickListener {
                val iNotif = Intent(this@MainActivity, NotificationActivity::class.java)
                activityLauncher.launch(iNotif)
            }

            eventAdapter.onItemClick = { event ->
                val iNewBuy = Intent(this@MainActivity, NewBuyActivity::class.java)
                iNewBuy.putExtra(EVENT_BUY, event)
                activityLauncher.launch(iNewBuy)
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
                val iBuy = Intent(this@MainActivity, BuyListActivity::class.java)
                iBuy.putParcelableArrayListExtra(TERNAK_EVENT_LIST, eventList)
                activityLauncher.launch(iBuy)
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
                    showSliderCategory(true)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrEmpty()) {
                        showSliderCategory(false)
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

                searchEventAdapter.setList(searched.event as ArrayList<EventsItem>)
                searchSliderItemAdapter.setList(searched.sliders as ArrayList<ArticlesItem>)

                binding.apply {
                    if (searched.event.isEmpty()) {
                        tvEmptyEvent.text = resources.getString(R.string.tidak_ada_event_search)
                        showEmptyEvent(true)
                    } else {
                        showEmptyEvent(false)
                        rvEvent.apply {
                            adapter = null
                            searchEventAdapter.onItemClick = { event ->
                                val iNewBuy = Intent(this@MainActivity, NewBuyActivity::class.java)
                                iNewBuy.putExtra(EVENT_BUY, event)
                                activityLauncher.launch(iNewBuy)
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

                    if (searched.sliders.isEmpty()) {
                        showEmptyBeritaSearch(true)
                    } else {
                        showEmptyBeritaSearch(false)
                        rvSearchSlider.apply {
                            adapter = null
                            searchSliderItemAdapter.onItemClick = { id, slug ->
                                val iNewsDetail =
                                    Intent(this@MainActivity, DetailNewsActivity::class.java)
                                iNewsDetail.putExtra(DetailNewsActivity.KEY_NEWS_ID, id)
                                iNewsDetail.putExtra(DetailNewsActivity.KEY_SLUG, slug)
                                startActivity(iNewsDetail)
                            }
                            adapter = searchSliderItemAdapter
                            layoutManager = GridLayoutManager(this@MainActivity, 2)
                        }
                    }
                }

            } else {
                binding.rvBtnTernak.isVisible = true
            }
        }
    }

    private fun showSliderCategory(isSearched: Boolean) {
        binding.apply {
            layoutBeritaSearch.isVisible = isSearched
            rvSliderCategory.isVisible = !isSearched
        }
    }

    private fun showEmptyBeritaSearch(isEmpty: Boolean) {
        binding.apply {
            tvEmptyNews.isVisible = isEmpty
            rvSearchSlider.isVisible = !isEmpty
        }
    }

    companion object {
        var ternakListSpecies = arrayListOf<RasTernakItem>()
        var ternak: TernakItem? = null

        var allTernakItem = arrayListOf<TernakItem>()

        val kandangList = arrayListOf<Kandang>()
    }
}