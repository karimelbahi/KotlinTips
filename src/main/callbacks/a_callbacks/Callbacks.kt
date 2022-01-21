package main.callbacks.a_callbacks


import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors


/**
 * Setup the orders here
 */
fun main() {
    val fred = Barista("Fred")
    val sam = Barista("Sam")

    fred.acceptOrder(CoffeeType.LATTE)
    sam.acceptOrder(CoffeeType.AMERICANO)
}

/**
 * A Barista can accept Coffee Type orders
 *
 * @param name: The name of the Barista
 */
class Barista(val name: String) : CoffeeMaker.OnCoffeeBrewedListener {
    private val coffeeMaker = CoffeeMaker()

    fun acceptOrder(type: CoffeeType) {
        coffeeMaker.brewCoffee(type, this)
    }

    override fun onCoffeeBrewed(coffee: Coffee) {
        println("$name finished brewing ${coffee.type}")

    }
}

/**
 * The Roast of the Coffee
 */
enum class CoffeeRoast {
    LIGHT,
    MEDIUM,
    DARK
}

/**
 * The Carrier Object for the finished order
 */
data class Coffee(val type: CoffeeType)

/**
 * The device that will emulate time passing
 */
class CoffeeMaker {
    fun brewCoffee(type: CoffeeType, callback: OnCoffeeBrewedListener) {
        /**
         * this prints
         * Sam finished brewing AMERICANO
         * Fred finished brewing LATTE
         * */
//        delay(type.brewTime)  // Simulates time to make coffee (SYNC) which delay use one thread
//        val madeCoffee = Coffee(type)
//        callback.onCoffeeBrewed(madeCoffee)

        /**
         * this prints
         * Fred finished brewing LATTE
         * Sam finished brewing AMERICANO
         * */
        delay(type.brewTime) { // Simulates time to make coffee (ASYNC) which delay use more than one thread
            val madeCoffee = Coffee(type)
            callback.onCoffeeBrewed(madeCoffee)
        }
    }

    interface OnCoffeeBrewedListener {
        fun onCoffeeBrewed(coffee: Coffee)
    }
}

/**
 * The Coffee Type
 *
 * @param brewTime: emulates time
 */
enum class CoffeeType(val brewTime: Long) {
    AMERICANO(300L),
    CAPPUCCINO(950L),
    DRIP(100L),
    ESPRESSO(800L),
    LATTE(875L)
}

/**
 * Emulate a delay by blocking the main thread.
 *
 * Warning: Never use this (lol)
 */
fun delay(time: Long) {
    Thread.sleep(time)
}

/**
 * Emulate a delay using threads.
 *
 * Warning: This is really just as an example.
 */
fun delay(time: Long, complete: () -> Unit) {
    val completableFuture = CompletableFuture<String>()
    Executors.newCachedThreadPool().submit<Any?> {
        Thread.sleep(time)
        completableFuture.complete("")
        null
    }
    completableFuture.thenAccept { complete() }
}