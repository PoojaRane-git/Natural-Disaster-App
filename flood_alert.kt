package com.example.naturaldisaster

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import org.json.JSONArray


class flood_alert : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_flood_alert)

        // Match the ID in XML
        val tableLayout: TableLayout = findViewById(R.id.floodTable)
        val checkWeatherButton: Button = findViewById(R.id.btnCheckWeather)

        // Initialize button click listener
        initializeWeatherButton(checkWeatherButton)

        lifecycleScope.launch {
            try {
                val response = SupabaseSingleton.client.postgrest
                    .from("flood_alerts")
                    .select()

                val jsonString = response.data
                Log.d("SupabaseTest", "Data: $jsonString")

                val jsonArray = JSONArray(jsonString)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    val name = obj.getString("area")       // column name in your table
                    val severity = obj.getString("severity")

                    // Create a new row
                    val row = TableRow(this@flood_alert)

                    val placeView = TextView(this@flood_alert).apply {
                        text = name
                        setTextColor(resources.getColor(android.R.color.white))
                        textSize = 16f
                        setPadding(8, 8, 8, 8)
                        gravity = android.view.Gravity.CENTER
                    }

                    val severityView = TextView(this@flood_alert).apply {
                        text = severity
                        setTextColor(resources.getColor(android.R.color.holo_red_light))
                        textSize = 16f
                        setPadding(8, 8, 8, 8)
                        gravity = android.view.Gravity.CENTER
                    }

                    row.addView(placeView)
                    row.addView(severityView)

                    // Add the row to the table
                    tableLayout.addView(row)
                }

            } catch (e: Exception) {
                Log.e("SupabaseTest", "Error: ${e.message}", e)
            }
        }
    }

    /**
     * Initialize the weather button with click listener
     */
    private fun initializeWeatherButton(button: Button) {
        button.setOnClickListener {
            // Option 1: Show a toast message
            Toast.makeText(this, "Opening Weather Information...", Toast.LENGTH_SHORT).show()

             val intent = Intent(this, CheckWeatherActivity::class.java)
             startActivity(intent)



            // Log the button click
            Log.d("FloodAlert", "Weather button clicked")
        }
    }

    /**
     * Alternative method to handle button click (if using android:onClick in XML)
     */
    fun checkWeatherActivity(view: android.view.View) {
        Toast.makeText(this, "Weather button clicked via XML onClick", Toast.LENGTH_SHORT).show()
        Log.d("FloodAlert", "Weather button clicked via XML")
    }
}