package com.example.represponsa

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val result = FirebaseApp.initializeApp(this)
        if (result == null) {
            Log.e("MyApp", "❌ Firebase não inicializado!")
        } else {
            Log.d("MyApp", "✅ Firebase inicializado com sucesso.")
        }
    }
}