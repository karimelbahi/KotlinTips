package main.callbacks.c_lambdas


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
class Barista(val name: String) {

    private val coffeeMaker = CoffeeMaker()

    /** first way*/
    fun acceptOrder(type: CoffeeType) {
        coffeeMaker.brewCoffee(type) {
            println("$name finished brewing ${this.type}")
        }
    }


    /** this is a second way which save lambda as a variable * */
    fun acceptOrder2(type: CoffeeType) {

        val onFinish = { coffee: Coffee ->
            println("$name finished brewing ${coffee.type}")
        }

        val onFinish1: (Coffee) -> Unit = {
            println("$name finished brewing ${it.type}")
        }

        val onFinish2: Coffee.() -> Unit = {
            println("$name finished brewing ${this.type}")
        }

        coffeeMaker.brewCoffee(type, onFinish) // onFinish one, two or three

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

        /**
         * lambda_name: Data_type -> Body
         * fun brewCoffee(type: CoffeeType, onBrewed: (Coffee) -> Unit) {
         */
        fun brewCoffee(type: CoffeeType, onBrewed: Coffee.() -> Unit) {
            delay(type.brewTime) { // Simulates time to make coffee (ASYNC)
                val madeCoffee = Coffee(type)
                onBrewed(madeCoffee)
            }
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