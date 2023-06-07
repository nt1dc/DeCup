package com.example.decup// com.example.decup.CoffeeVariety.kt

data class CoffeeVariety(val name: String, val description: String) {
    override fun toString(): String {
        return name
    }
}
