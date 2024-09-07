plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
    id("androidx.room") version "2.6.1" apply false
    id("com.google.dagger.hilt.android") version "2.51" apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    id("com.google.devtools.ksp") version "2.0.20-1.0.24" apply false
}