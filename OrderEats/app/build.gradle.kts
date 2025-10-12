@file:Suppress("UNUSED_EXPRESSION")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.rajender.ordereats"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.rajender.ordereats"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation("com.github.denzcoskun:ImageSlideshow:0.1.0")
// Keep standard Android and Jetpack libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.database)

    // For modern sign-in flows with Credential Manager (recommended by Google)
    implementation("androidx.credentials:credentials:1.5.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0")


    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // For modern sign-in flows with Credential Manager (Google's recommendation)
    // Even if using older GoogleSignInClient, these are good to be aware of
    implementation("androidx.credentials:credentials:1.5.0") // Or latest
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0") // Or latest

    // Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.1.2")) // Or latest version
    implementation("com.google.firebase:firebase-auth")

    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:21.2.0") // Or latest version

    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore) // For Realtime datbase
//    implementation("com.google.firebase:firebase-database-ktx:22.0.0")

    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}