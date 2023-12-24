package com.ndorokojo.ui.tambahkandang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ndorokojo.R
import com.ndorokojo.databinding.ActivityTambahKandangBinding

class TambahKandangActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTambahKandangBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahKandangBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}