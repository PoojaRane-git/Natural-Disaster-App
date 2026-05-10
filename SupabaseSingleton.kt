package com.example.naturaldisaster

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseSingleton {
    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = "https://wjgjkjymfcwgmtpthabj.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndqZ2pranltZmN3Z210cHRoYWJqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTc4MzYwNDIsImV4cCI6MjA3MzQxMjA0Mn0.QOzc23LxIFyTklK-aVhAnsyg5Pt8rRj2DJ6BlUNZeKw"
        ) {
            install(Postgrest)
        }
    }
}