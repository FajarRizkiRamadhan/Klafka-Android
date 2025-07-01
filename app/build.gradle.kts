plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")

}

android {
    namespace = "com.example.klafka"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.klafka"
        minSdk = 26
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation("org.apache.poi:poi-ooxml:5.2.2")
    implementation("org.apache.poi:poi:5.2.2")
    implementation("org.apache.commons:commons-collections4:4.4")
    implementation("org.apache.xmlbeans:xmlbeans:5.1.1")


    //modelPDF
    implementation("com.itextpdf:itextg:5.5.10")
    // ViewModel dan LiveData (sudah kamu pakai di ViewModel dan Fragment)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")

    // CardView & RecyclerView (untuk tampilan dan adapter jenis kain & history)
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.1")

    // TensorFlow Lite (jika klasifikasi dilakukan secara offline)
    implementation("org.tensorflow:tensorflow-lite:2.13.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.3.1")

    // OKHttp (jika klasifikasi dilakukan via API)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Picasso (untuk memuat gambar klasifikasi/history)
    implementation("com.squareup.picasso:picasso:2.8")

    // Navigation (sudah ada, tetapi pastikan ini versi terbaru dan konsisten)
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.2")

    // Material Design (untuk komponen MaterialButton dan lainnya)
    implementation("com.google.android.material:material:1.9.0")

}
