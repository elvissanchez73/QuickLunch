plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services") // Correct syntax for Kotlin DSL
}

android {
    namespace = "com.example.quicklunch"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.quicklunch"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Firebase BOM (Bill of Materials) for version management
    implementation(platform("com.google.firebase:firebase-bom:32.1.0"))

    // Firebase dependencies
    implementation("com.google.firebase:firebase-auth") // Firebase Authentication
    implementation("com.google.firebase:firebase-database") // Firebase Realtime Database
    implementation("com.google.firebase:firebase-firestore") // Firebase Firestore (if you need it)

    // Additional dependencies
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation ("com.google.android.material:material:1.12.0")

}

