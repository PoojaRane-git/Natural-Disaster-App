package com.example.naturaldisaster

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import org.json.JSONArray

class EmergencyCall : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_emergency_call)

        val listView: ListView = findViewById(R.id.listView)

        lifecycleScope.launch {
            try {
                // Fetch all contacts
                val response = SupabaseSingleton.client.postgrest
                    .from("emergency_contacts")
                    .select()
                val jsonArray = JSONArray(response.data)

                // Prepare two lists
                val names = ArrayList<String>()
                val numbers = ArrayList<String>()

                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    names.add("${obj.getString("name")} - ${obj.getString("contact")}")
                    numbers.add(obj.getString("contact"))
                }

                // Bind adapter
                listView.adapter = ArrayAdapter(
                    this@EmergencyCall,
                    android.R.layout.simple_list_item_1,
                    names
                )

                // On click, dial number
                listView.setOnItemClickListener { _, _, position, _ ->
                    val phoneUri = Uri.parse("tel:${numbers[position]}")
                    startActivity(Intent(Intent.ACTION_DIAL, phoneUri))
                }

            } catch (e: Exception) {
                Log.e("EmergencyCall", "Error loading contacts", e)
            }
        }
    }
}
