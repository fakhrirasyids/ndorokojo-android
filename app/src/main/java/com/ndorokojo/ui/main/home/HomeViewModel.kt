package com.ndorokojo.ui.main.home

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel() : ViewModel() {

    val isClickedPet = MutableLiveData<Boolean>(false)
    val clickedPetButton = MutableLiveData<String>()
}