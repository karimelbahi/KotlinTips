package main.callbacks.d_Higher_Order_Functions

/**
 * https://agrawalsuneet.github.io/blogs/higher-order-functions-in-kotlin/
 * */
fun main(args: Array<String>) {

    var list = arrayListOf<Int>()
    for (number in 1..10){
        list.add(number)
    }
    var resultList = list.filterOnCondition {it,it2->
        println("start of calling from filterOnCondition")
        isMultipleOf(it, it2,1)
    }

}

fun isMultipleOf (number: Int, multipleOf : Int, numForTest:Int): Boolean{
    println("calling from isMultipleOf $number  $multipleOf")
    return number % multipleOf == 0
}


fun <T> ArrayList<T>.filterOnCondition(condition: (T,T) -> Boolean): ArrayList<T>{
    println("filterOnCondition ${this}")
    val result = arrayListOf<T>()
    for (item in this){
        println("calling from inside for")
        if (condition(item,5 as T)){
            println("calling from inside if")
            result.add(item)
        }
    }
    println("filtered data ${result}")
    return result
}

