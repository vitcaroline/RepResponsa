plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.example.represponsa"
    compileSdk = 35

    buildFeatures {
        compose = true
    }

    defaultConfig {
        applicationId = "com.example.represponsa"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation ("androidx.compose.ui:ui:1.5.4")
    implementation ("androidx.compose.material3:material3:1.2.1")
    implementation ("androidx.activity:activity-compose:1.8.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.navigation:navigation-compose:2.7.3")
    implementation("com.google.gms:google-services:4.4.1")
    implementation ("com.google.firebase:firebase-common-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.33.2-alpha")
    implementation("androidx.compose.foundation:foundation:1.5.0")
}