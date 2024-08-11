plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
    id("kotlinx-serialization")
    id("com.google.gms.google-services")
}

android {
    namespace = "project.app.notewise"
    compileSdk = 35

    defaultConfig {
        applicationId = "project.app.notewise"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.activity:activity-compose:1.9.1")
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Material Design Icons
    implementation("androidx.compose.material:material-icons-extended:1.6.8")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.0-beta07")

    //Lottie-compose
    implementation("com.airbnb.android:lottie-compose:6.4.0")

    //Ktor-Client
    implementation("io.ktor:ktor-client-core:2.3.5")
    implementation("io.ktor:ktor-client-cio:2.3.2")
    implementation("io.ktor:ktor-client-serialization:2.3.2")
    implementation("io.ktor:ktor-client-cio-jvm:2.3.2")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.2")
    implementation("io.ktor:ktor-serialization-gson:2.3.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.2")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("io.ktor:ktor-client-logging:2.3.2")
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("io.ktor:ktor-client-android:1.6.4")
    implementation("io.ktor:ktor-client-serialization:2.3.2")
    implementation("io.ktor:ktor-client-logging-jvm:2.3.2")
    implementation("io.ktor:ktor-client-auth:2.3.2")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("io.ktor:ktor-client-gson:2.3.2")

    //Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.51")
    kapt("com.google.dagger:hilt-compiler:2.51")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // Coil
    implementation("io.coil-kt:coil-compose:2.2.2")

    // Material Icons Extended
    implementation("androidx.compose.material:material-icons-extended:1.6.8")

    // DataStore
    implementation("androidx.datastore:datastore-core:1.1.1")

    // Swipe
    implementation("me.saket.swipe:swipe:1.2.0")

    implementation("com.mohamedrejeb.richeditor:richeditor-compose:1.0.0-rc05")

    // Collapsing Toolbar
    implementation("me.onebone:toolbar-compose:2.3.5")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-firestore-ktx:25.0.0")


    // JetFiresStore
    implementation("com.github.raipankaj:JetFireStore:1.0.2")

}