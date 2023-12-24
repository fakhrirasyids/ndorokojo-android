package com.ndorokojo.ui.changepassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ndorokojo.R
import com.ndorokojo.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            toolbar.setNavigationOnClickListener { finish() }

            btnSave.setOnClickListener {
                if (isValid()) {
                    finish()
                }
            }
        }
    }

    private fun isValid() = if (binding.edOldPassword.text.toString().isEmpty()) {
        false
    } else if (binding.edNewPassword.text.toString().isEmpty()) {
        false
    } else if (binding.edNewPasswordConfirmation.text.toString().isEmpty()) {
        false
    } else if (binding.edNewPassword.text.toString() != binding.edNewPasswordConfirmation.text.toString()) {
        false
    } else {
        true
    }

}