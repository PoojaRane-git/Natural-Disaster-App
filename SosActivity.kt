package com.example.naturaldisaster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sos) // Make sure you have activity_sos.xml

        // Consider using ViewBinding for safer and easier view access
        val btnEmergencyCall: Button = findViewById(R.id.btn1)
        val btnLocation: Button = findViewById(R.id.btn2)

        btnEmergencyCall.setOnClickListener {
            // Ensure EmergencyCall::class.java exists and is an Activity
            startActivity(Intent(this, EmergencyCall::class.java))
        }

        btnLocation.setOnClickListener {
            // Ensure LocationActivity::class.java exists and is an Activity
            startActivity(Intent(this, LocationActivity::class.java))
        }


    }
}
