
package com.example.naturaldisaster
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val latitude: Double,       // float8 → Double
    val longitude: Double,      // float8 → Double
    val place_name: String,     // text → String
    val timestamp: String? = null // timestamptz → ISO8601 string (optional)
)
