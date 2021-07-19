package com.projectmalthorn.dishme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.projectmalthorn.dishme.databinding.ActivitySpalshBinding

class SpalshActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh)

        val splashBinding: ActivitySpalshBinding = ActivitySpalshBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)
    }
}