plugins {
    id("org.jetbrains.kotlin.kapt")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.logistica_austral"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.logistica_austral"
        minSdk = 33
        targetSdk = 36
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime.ktx)
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    // periferico
    implementation("io.coil-kt:coil-compose:2.5.0")
    // jetpack compose y material3 2025
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    // retrofit y gson converter
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    // corrutina para asincronicas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    // Kotest - Para pruebas unitarias
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")

    // JUnit5
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")

    // Mock para simular repositorios y clases
    testImplementation("io.mockk:mockk:1.13.8")

    // Corrutinas Test (Necesario en ViewModels)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // Compose UI Test (Para interfaz)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.2")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.2")

    androidTestImplementation("io.mockk:mockk-android:1.13.10")

    // Asegúrate de tener estas también para las pruebas de UI
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.2")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.2")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}