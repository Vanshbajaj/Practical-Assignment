package com.practical.presentation.viewmodel

import javax.inject.Qualifier


// IO Dispatcher
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

// Default Dispatcher
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher

// Main Dispatcher (UI Thread)
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher
