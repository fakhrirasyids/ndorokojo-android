package com.ndorokojo.ui.detailkandang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ndorokojo.R
import com.ndorokojo.databinding.ActivityDetailKandangBinding

class DetailKandangActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailKandangBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailKandangBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}