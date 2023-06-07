// CoffeeShop.kt

data class CoffeeShop(val name: String, val address: String) {
    override fun toString(): String {
        return "$name\n$address"
    }
}
