package com.digian.clean

import com.digian.clean.core.data.exception.Failures
import com.digian.clean.core.data.platform.NetworkHandler
import com.digian.clean.core.domain.exception.Failure
import com.digian.clean.core.domain.ports.UseCaseInputPort
import com.digian.clean.core.domain.ports.UseCaseOutputPort
import com.digian.clean.features.movies.data.repository.ASSET_BASE_PATH
import com.digian.clean.features.movies.data.repository.MoviesRepositoryImpl
import com.digian.clean.features.movies.domain.entities.MovieEntity
import io.mockk.every
import io.mockk.mockk
import java.io.FileInputStream
import java.io.InputStream

/**
 * Created by Alex Forrester on 2019-07-13.
 */

internal object MovieRepositoryFactory {

    private val networkHandlerConnected: NetworkHandler = mockk()

    init {
        every { networkHandlerConnected.isConnected } returns true
    }

    val movieRepository = object : MoviesRepositoryImpl(
        mockk(),
        MoshiFactory.moshi,
        networkHandler = networkHandlerConnected
    ) {
        override suspend fun getInputStreamForJsonFile(fileName: String): InputStream {
            return FileInputStream(ASSET_BASE_PATH + fileName)
        }
    }

    val movieRepositoryFailure = object : MoviesRepositoryImpl(
        mockk(),
        MoshiFactory.moshi,
        networkHandler = networkHandlerConnected
    ) {
        override suspend fun getInputStreamForJsonFile(fileName: String): InputStream {
            return FileInputStream(ASSET_BASE_PATH + fileName)
        }

        override suspend fun getMovies(none: UseCaseInputPort.None): UseCaseOutputPort<Failure, List<MovieEntity>> {
            return UseCaseOutputPort.Error(Failures.ServerException(Exception()))
        }
    }
}