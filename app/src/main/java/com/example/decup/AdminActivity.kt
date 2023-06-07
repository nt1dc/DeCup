package com.example.decup


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {
    private lateinit var etCoffeeName: EditText
    private lateinit var etCoffeeDescription: EditText
    private lateinit var btnAddCoffee: Button

    private val apiClient = ApiClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        etCoffeeName = findViewById(R.id.etCoffeeName)
        etCoffeeDescription = findViewById(R.id.etCoffeeDescription)
        btnAddCoffee = findViewById(R.id.btnAddCoffee)

        btnAddCoffee.setOnClickListener {
            val coffeeName = etCoffeeName.text.toString()
            val coffeeDescription = etCoffeeDescription.text.toString()

            val newCoffeeVariety = CoffeeVariety(coffeeName, coffeeDescription)

            // Call the API to add the new coffee variety
            apiClient.addCoffeeVariety(newCoffeeVariety)
            Toast.makeText(this, "New coffee variety added successfully!", Toast.LENGTH_SHORT).show()

            etCoffeeName.text.clear()
            etCoffeeDescription.text.clear()
        }
    }
}