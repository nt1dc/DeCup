package com.example.decup

import CoffeeShop
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ApiClient {
    private val httpClient = OkHttpClient()
    private val baseUrl = "https://localhost:8080" // Replace with your API base URL

    suspend fun getCoffeeVarieties(): List<CoffeeVariety> {
        val url = "$baseUrl/coffee-varieties"
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        return suspendCoroutine { continuation ->
            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        continuation.resumeWithException(Exception("Failed to fetch coffee varieties: ${response.code}"))
                        return
                    }

                    val responseBody = response.body?.string()
                    val coffeeVarietiesJson = JSONObject(responseBody)
                    val coffeeVarieties = mutableListOf<CoffeeVariety>()

                    coffeeVarietiesJson.keys().forEach { key ->
                        val coffeeVarietyJson = coffeeVarietiesJson.getJSONObject(key)
                        val name = coffeeVarietyJson.getString("name")
                        val description = coffeeVarietyJson.getString("description")
                        val coffeeVariety = CoffeeVariety(name, description)
                        coffeeVarieties.add(coffeeVariety)
                    }

                    continuation.resume(coffeeVarieties)
                }
            })
        }
    }

    suspend fun getCoffeeShops(coffeeVariety: String): List<CoffeeShop> {
        val url = "$baseUrl/coffee-shops?variety=$coffeeVariety"
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        return suspendCoroutine { continuation ->
            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        continuation.resumeWithException(Exception("Failed to fetch coffee shops: ${response.code}"))
                        return
                    }

                    val responseBody = response.body?.string()
                    val coffeeShopsJson = JSONObject(responseBody)
                    val coffeeShops = mutableListOf<CoffeeShop>()

                    coffeeShopsJson.keys().forEach { key ->
                        val coffeeShopJson = coffeeShopsJson.getJSONObject(key)
                        val name = coffeeShopJson.getString("name")
                        val address = coffeeShopJson.getString("address")
                        val coffeeShop = CoffeeShop(name, address)
                        coffeeShops.add(coffeeShop)
                    }

                    continuation.resume(coffeeShops)
                }
            })
        }
    }

    suspend fun submitRating(user: User, coffeeVariety: CoffeeVariety, coffeeShop: CoffeeShop, rating: Float) {
        val url = "$baseUrl/ratings"
        val requestBodyJson = JSONObject()
        requestBodyJson.put("username", user.username)
        requestBodyJson.put("coffeeVariety", coffeeVariety.name)
        requestBodyJson.put("coffeeShop", coffeeShop.name)
        requestBodyJson.put("rating", rating)

        val requestBody = requestBodyJson.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        return suspendCoroutine { continuation ->
            httpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        continuation.resumeWithException(Exception("Failed to submit rating: ${response.code}"))
                        return
                    }

                    continuation.resume(Unit)
                }
            })
        }
    }
}
