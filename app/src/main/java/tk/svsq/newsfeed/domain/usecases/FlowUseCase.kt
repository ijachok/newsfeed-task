package tk.svsq.newsfeed.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

abstract class FlowUseCase<T, in Params> {

    protected abstract suspend fun buildUseCase(params: Params): Flow<T>

    suspend fun execute(params: Params, onSuccess: (T) -> Unit, onFailure: (Throwable) -> Unit) {
        return buildUseCase(params)
            .catch {
                onFailure(it)
            }.onEach {
                onSuccess(it)
            }.flowOn(Dispatchers.IO)
            .collect()
    }
}