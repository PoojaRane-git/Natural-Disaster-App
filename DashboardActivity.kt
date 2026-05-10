package com.example.naturaldisaster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Bottom buttons
        val btnSos: Button = findViewById(R.id.btn_sos_bottom)
        val btnFlood: Button = findViewById(R.id.btn_floodalerts)

        // Bottom button click listeners
        btnSos.setOnClickListener {
            startActivity(Intent(this, SosActivity::class.java))
        }

        btnFlood.setOnClickListener {
            startActivity(Intent(this, flood_alert::class.java))
        }

        // Main dashboard SOS button (optional click)

    }
}
