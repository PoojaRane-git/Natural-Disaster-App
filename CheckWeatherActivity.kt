package com.example.naturaldisaster


import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import android.util.Log
class CheckWeatherActivity : AppCompatActivity() {
    private lateinit var cityNameText: TextView
    private lateinit var temperatureText: TextView
    private lateinit var humidityText: TextView
    private lateinit var windText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var weatherIcon: ImageView
    private lateinit var fetchButton: Button
    private val apiKey = "b8218aa1d63491be0f4434c22a2e9790"
    private val httpClient = OkHttpClient()
    private lateinit var districtname: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_weather)
        cityNameText = findViewById(R.id.cityNameText)
        temperatureText = findViewById(R.id.temperatureText)
        humidityText = findViewById(R.id.humidityText)
        windText = findViewById(R.id.windText)
        descriptionText = findViewById(R.id.descriptionText)
        weatherIcon = findViewById<ImageView>(R.id.weatherIcon)
        fetchButton = findViewById(R.id.fetchWeatherButton)
        districtname = findViewById(R.id.cityNameInput)

        fetchButton.setOnClickListener {
            val cityName = districtname.text.toString()
            if (cityName.isNotEmpty()) {
                fetchWeatherData(cityName)
            } else {
                districtname.error = "Please enter a district name"
            }
        }

    }

    private fun fetchWeatherData(cityName: String) {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$cityName&appid=$apiKey&units=metric"
        Log.d("WeatherApp", "Fetching weather for: $cityName")
        CoroutineScope(Dispatchers.IO).launch {
            val request = Request.Builder().url(url).build()

            try {
                val response = httpClient.newCall(request).execute()
                val result = response.body?.string()
                Log.d("WeatherApp", "Response: $result")
                withContext(Dispatchers.Main) {
                    result?.let {
                        updateUI(it)
                    }
                }
            } catch (e: IOException) {
                Log.e("WeatherApp", "Network error: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun updateUI(response: String) {
        try {
            val json = JSONObject(response)
            val main = json.getJSONObject("main")
            val temperature = main.getDouble("temp")
            val humidity = main.getDouble("humidity")
            val windSpeed = json.getJSONObject("wind").getDouble("speed")
            val weatherInfo = json.getJSONArray("weather").getJSONObject(0)
            val condition = weatherInfo.getString("main")      // e.g., Clear, Rain, Clouds
            val description = weatherInfo.getString("description") // more specific info
            Log.d("WeatherApp", "Condition: $condition, Temp: $temperature, Humidity: $humidity, Wind: $windSpeed, Description: $description")
            when (condition) {
                "Clear" -> weatherIcon.setImageResource(R.drawable.sunny)
                "Rain", "Drizzle", "Thunderstorm" -> weatherIcon.setImageResource(R.drawable.group_5)
                "Clouds", "Mist", "Haze", "Fog", "Smoke" -> weatherIcon.setImageResource(R.drawable.group_3)
                else -> weatherIcon.setImageResource(R.drawable.group_3) // fallback icon
            }
            cityNameText.text = "Weather in ${json.getString("name")}"
            temperatureText.text = String.format("%.0f°", temperature)
            humidityText.text = String.format("%.0f%%", humidity)
            windText.text = String.format("%.0f km/h", windSpeed)
            descriptionText.text = description.replaceFirstChar { it.uppercase() }

            Log.d("WeatherApp", "UI updated successfully")

        } catch (e: Exception) {
            Log.e("WeatherApp", "JSON Parsing or UI update failed: ${e.message}")
            e.printStackTrace()
        }
    }
}
