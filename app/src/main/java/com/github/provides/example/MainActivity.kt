package com.github.provides.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.provides.example.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 直接使用 binding 访问布局控件
        binding.textView.text = "Hello Hilt + ViewBinding"
    }
}