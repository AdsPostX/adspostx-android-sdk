package com.adspostx.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.adspostx.AdpostxDialogFragment
import com.adspostx.demo.databinding.ActivityMainBinding
import com.adspostx.util.AdpostxDialogDismissListener
import com.adspostx.util.DialogParams

class MainActivity : AppCompatActivity(),AdpostxDialogDismissListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adpostxDialogFragment: AdpostxDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adpostxDialogFragment = AdpostxDialogFragment.newInstance(DialogParams("Order is complete"),this)
        binding.buttonShowDialog.setOnClickListener {
            adpostxDialogFragment.show(supportFragmentManager, AdpostxDialogFragment::class.java.canonicalName)
        }
    }

    override fun dismissListener() {
        Toast.makeText(this, "Dialog Dismissed", Toast.LENGTH_SHORT).show()
    }


}