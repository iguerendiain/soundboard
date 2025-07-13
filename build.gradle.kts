plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.dagger.hilt.android") version "2.56.2" apply false
    alias(libs.plugins.androidLibrary) apply false
    id("com.google.devtools.ksp") version "2.2.0-2.0.2" apply false
    alias(libs.plugins.kotlinCompose) apply false
}