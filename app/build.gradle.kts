plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.pucmm.icc451.proyectoandroid"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.pucmm.icc451.proyectoandroid"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.activity.compose)
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.cardview)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.material)
    implementation(libs.material3)
    implementation(libs.recyclerview)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)


    implementation(platform(libs.firebase.bom))

    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)
    androidTestImplementation(libs.ui.test.junit4)


    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    debugImplementation(libs.ui.test.manifest)
    debugImplementation(libs.ui.tooling)
}