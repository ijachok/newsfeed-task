package tk.svsq.newsfeed.domain.usecases

import android.util.Log
import androidx.annotation.WorkerThread
import kotlinx.coroutines.*

abstract class UseCase<Params, out Type> where Type: Any {

    private val TAG = this.javaClass.simpleName

    // dispatchers
    private val UI = Dispatchers.Main
    private val BG = Dispatchers.IO

    // type for empty parameter
    class None

    // parameters
    protected var params: Params? = null

    private var job : Deferred<Result<Type>>? = null

    fun withParams(params: Params) {
        this.params = params
    }

    // run

    @WorkerThread
    abstract suspend fun run() : Result<Type>

    @WorkerThread
    suspend fun runWithParams(params: Params) : Result<Type> {
        withParams(params)
        return run()
    }

    // invoke

    operator fun invoke(params: Params, onResult: (Result<Type>) -> Unit = {}) {
        withParams(params)
        job = CoroutineScope(BG).async { run() }
        CoroutineScope(UI).launch {
            val r = job!!.await()
            if (isActive) onResult(r)
        }
    }

    operator fun invoke(onResult: (Result<Type>) -> Unit = {}) {
        job = CoroutineScope(BG).async { run() }
        CoroutineScope(UI).launch {
            val r = job!!.await()
            if (isActive) onResult(r)
        }
    }

    private val errorMessage = "job is null"
    fun isActive() = job?.isActive ?: false.also { Log.w(TAG, errorMessage) }
    fun isCancelled() = job?.isCancelled ?: false.also { Log.w(TAG, errorMessage) }
    fun isCompleted() = job?.isCompleted ?: false.also { Log.w(TAG, errorMessage) }

    // invoke + suspend

    suspend operator fun invoke(params: Params) : Result<Type> {
        withParams(params)
        return run()
    }

    suspend operator fun invoke(): Result<Type> {
        return run()
    }
}