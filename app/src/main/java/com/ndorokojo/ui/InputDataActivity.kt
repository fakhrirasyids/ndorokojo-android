package com.ndorokojo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ndorokojo.R
import com.ndorokojo.databinding.ActivityInputDataBinding

class InputDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}