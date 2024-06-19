plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
}

android {
    namespace = "capstone.tim.aireal"
    compileSdk = 34

    defaultConfig {
        applicationId = "capstone.tim.aireal"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }


    buildFeatures {
        buildConfig = true
        viewBinding = true

    }
}


dependencies {
    // Glide - Image loading library for efficient and flexible image loading in Android apps.
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Activity KTX - Kotlin extensions for Activity class, simplifying common operations.
    implementation("androidx.activity:activity-ktx:1.9.0")

    // Core KTX - Kotlin extensions for core Android framework classes.
    implementation("androidx.core:core-ktx:1.13.1")

    // AppCompat - Support library for backwards compatibility with older Android versions, including core UI components like AppCompatActivity and AppCompatTextView.
    implementation("androidx.appcompat:appcompat:1.7.0")

    // Material Design - Components and theming for building modern, responsive Android apps.
    implementation("com.google.android.material:material:1.12.0")

    // ConstraintLayout - Powerful layout manager for creating complex layouts with ease.
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // JUnit 4 - Testing framework for writing unit tests.
    testImplementation("junit:junit:4.13.2")

    // Espresso - Testing framework for writing UI tests on Android.
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Retrofit - HTTP client library for building RESTful APIs.
    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    // Retrofit Converter Gson - Converter for using Gson with Retrofit for JSON serialization/deserialization.
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // OkHttp Logging Interceptor - Logs HTTP requests and responses for debugging.
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // DataStore - Persisting and retrieving user preferences with DataStore API.
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // ViewModel KTX - Kotlin extensions for ViewModel class, simplifying data management in activities/fragments.
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")

    // LiveData KTX - Kotlin extensions for LiveData class, simplifying reactive data handling.
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.2")

    // CircleImageView - Circular image view library for rounded profile pictures or other circular image needs.
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // SmartImageSlider - Image slider library for implementing visually appealing image carousels.
    implementation("com.github.smarteist:autoimageslider:1.3.9")

    // Navigation Fragment KTX - Kotlin extensions for Navigation component, simplifying navigation setup.
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")

    // Navigation UI KTX - Kotlin extensions for Navigation UI component, providing easier access to navigation components like bottom navigation bars.
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // CameraX - Modern camera API for capturing photos and videos with easier access to device capabilities.
    implementation("androidx.camera:camera-camera2:1.3.3")
    implementation("androidx.camera:camera-lifecycle:1.3.3")
    implementation("androidx.camera:camera-view:1.3.3")

    // ExifInterface - Library for reading and writing EXIF metadata from images (e.g., orientation information).
    implementation("androidx.exifinterface:exifinterface:1.3.7")
}