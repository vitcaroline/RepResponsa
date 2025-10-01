package com.example.represponsa.di

import android.content.Context
import com.example.represponsa.data.repository.AssignmentRepository
import com.example.represponsa.data.repository.AuthRepository
import com.example.represponsa.data.repository.MinutesRepository
import com.example.represponsa.data.repository.RepublicRepository
import com.example.represponsa.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        @ApplicationContext context: Context
    ): AuthRepository = AuthRepository(firebaseAuth, firestore, context)

    @Provides
    @Singleton
    fun provideUserRepository(
        firestore: FirebaseFirestore
    ): UserRepository = UserRepository(firestore)

    @Provides
    @Singleton
    fun provideRepublicRepository(
        authRepository: AuthRepository,
        firestore: FirebaseFirestore
    ): RepublicRepository = RepublicRepository(authRepository, firestore)

    @Provides
    @Singleton
    fun provideAssignmentRepository(
        authRepository: AuthRepository,
        firestore: FirebaseFirestore
    ): AssignmentRepository = AssignmentRepository(authRepository, firestore)

    @Provides
    @Singleton
    fun provideMinuteRepository(
        authRepository: AuthRepository,
        firestore: FirebaseFirestore
    ): MinutesRepository = MinutesRepository(authRepository, firestore)

}