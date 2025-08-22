// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Reference the AGP version from libs.versions.toml
    alias(libs.plugins.android.application) apply false
    // If you have library modules, you'd also add:
    // alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}