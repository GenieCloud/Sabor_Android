plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.fullsail.android.unittestingstarter"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.fullsail.android.unittestingstarter"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Material Components & AndroidX UI
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.preference:preference-ktx:1.2.1")

    // Unit Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.json:json:20231013") // Fixes JSONObject stubs returning empty/0 in JVM tests
    testImplementation("org.mockito:mockito-core:4.11.0")
    testImplementation("org.mockito:mockito-inline:4.11.0")
    testImplementation("androidx.test:runner:1.5.2")
    testImplementation("androidx.test.ext:junit:1.1.5")
    testImplementation("org.robolectric:robolectric:4.14.1")

    // Instrumented Android Testing
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("org.mockito:mockito-android:4.11.0")
}

configurations.all {
    resolutionStrategy {
        capabilitiesResolution {
            withCapability("com.google.guava:listenablefuture") {
                select("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")
            }
        }
        exclude(mapOf("group" to "org.hamcrest", "module" to "hamcrest-core"))
    }
}