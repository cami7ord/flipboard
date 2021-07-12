package com.camilobaquero.flipboardgame.di

import android.content.Context
import com.camilobaquero.flipboardgame.app.BaseApplication
import com.camilobaquero.flipboardgame.domain.GetLargestRectangleUseCase
import com.camilobaquero.flipboardgame.domain.GetLargestRectangleUseCaseImpl
import com.camilobaquero.flipboardgame.domain.MockLargestRectangleUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainModule {

    @Binds
    @MockUseCase
    abstract fun bindMockUseCase(
        useCase: MockLargestRectangleUseCaseImpl
    ): GetLargestRectangleUseCase

    @Binds
    @RealUseCase
    abstract fun bindRealUseCase(
        useCase: GetLargestRectangleUseCaseImpl
    ): GetLargestRectangleUseCase
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MockUseCase

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RealUseCase
