plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.rajender.adminordereats"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.rajender.adminordereats"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    // You can combine buildFeatures blocks
    buildFeatures {
        viewBinding = true
        dataBinding = true // dataBinding also enables viewBinding, so the above line is redundant but harmless
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
    // Keep standard Android and Jetpack libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // KEEP THIS - This is the correct graphics library for Android Compose
    implementation(libs.androidx.ui.graphics.android)

    // REMOVE these as they are for desktop or cause duplication
    // implementation(libs.androidx.ui.graphics.desktop) // Removed (was listed twice)
    // implementation(libs.androidx.ui.graphics.jvmstubs) // Removed

    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
