package com.example.decup// com.example.decup.MainActivity.kt

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var apiClient: ApiClient
    private lateinit var coffeeAdapter: CoffeeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiClient = ApiClient()

        // Initialize RecyclerView and its adapter
        val coffeeRecyclerView: RecyclerView = findViewById(R.id.coffeeRecyclerView)
        coffeeAdapter = CoffeeAdapter()
        coffeeRecyclerView.adapter = coffeeAdapter
        coffeeRecyclerView.layoutManager = LinearLayoutManager(this)

        val fetchButton: Button = findViewById(R.id.fetchButton)
        fetchButton.setOnClickListener {
            fetchCoffeeVarieties()
        }
    }

    private fun fetchCoffeeVarieties() {
        CoroutineScope(Dispatchers.Main).launch {
            showProgressBar()
            try {
                val coffeeVarieties = withContext(Dispatchers.IO) { apiClient.getCoffeeVarieties() }
                showCoffeeVarieties(coffeeVarieties)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error fetching coffee varieties", e)
                showToast("Error fetching coffee varieties")
            } finally {
                hideProgressBar()
            }
        }
    }

    private fun showProgressBar() {
        findViewById<View>(R.id.progressBar).visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        findViewById<View>(R.id.progressBar).visibility = View.GONE
    }

    private fun showCoffeeVarieties(coffeeVarieties: List<CoffeeVariety>) {
        coffeeAdapter.setCoffeeVarieties(coffeeVarieties)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
