package tk.svsq.newsfeed.data.mappers

import java.util.ArrayList

abstract class Mapper<F, T> {

    abstract fun map(from: F): T
    abstract fun reverse(to: T): F

    fun map(fromList: List<F>): List<T> =
        ArrayList<T>(fromList.size).apply {
            fromList.forEach {
                add(map(it))
            }
        }

    fun reverse(toList: List<T>): List<F> =
        ArrayList<F>(toList.size).apply {
            toList.forEach {
                add(reverse(it))
            }
        }
}