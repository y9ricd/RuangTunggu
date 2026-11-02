package com.ruangtunggu.util

/**
 * Represents the state of a UI operation.
 *
 * @param T The type of data held in [Success].
 */
sealed class UiState<out T> {

    /** No operation has been initiated yet. */
    data object Idle : UiState<Nothing>()

    /** An operation is in progress. */
    data object Loading : UiState<Nothing>()

    /** The operation completed successfully with [data]. */
    data class Success<T>(val data: T) : UiState<T>()

    /** The operation failed with an [message]. */
    data class Error(val message: String) : UiState<Nothing>()
}
