plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.services)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kapt)
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
val composeVersion = "1.6.1"

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation(libs.androidx.navigation.common.android)

    // REMOVIDA a dependência foundation-desktop que causava conflito
    // implementation(libs.androidx.foundation.desktop)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Compose (todas versões alinhadas para 1.6.1)
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")

    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.navigation:navigation-compose:2.7.3")
    implementation("com.google.gms:google-services:4.4.1")
    implementation("com.google.firebase:firebase-common-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.33.2-alpha")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.33.2-alpha")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("com.google.accompanist:accompanist-flowlayout:0.32.0")

    debugImplementation(libs.androidx.ui.tooling)
}