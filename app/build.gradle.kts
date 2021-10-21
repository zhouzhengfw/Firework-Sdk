plugins {
    id("com.android.application")
    id("kotlin-android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.apollographql.apollo").version("2.5.9")
}

android {
    compileSdkVersion(31)
    buildToolsVersion("29.0.3")

    defaultConfig {
        applicationId = "com.loopnow.fireworkdemo"
        minSdkVersion(23)
        targetSdkVersion(31)
        versionCode = 1
        versionName = "1.0"
    }

//    buildTypes {
//        getByName("release") {
//            isMinifyEnabled = false
//            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
//        }
//
//        getByName("debug") {
//            isDebuggable = true
//        }
//
//    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("com.google.code.gson:gson:2.8.8")
    implementation("androidx.core:core-ktx:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("com.apollographql.apollo:apollo-runtime:2.5.9")
    implementation("com.apollographql.apollo:apollo-coroutines-support:2.5.9")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("io.coil-kt:coil:0.11.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.0-rc01")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.0-rc01")
    implementation("androidx.paging:paging-runtime-ktx:2.1.2")
    implementation("com.google.android.material:material:1.1.0")
    implementation("androidx.security:security-crypto:1.0.0-rc02")
    implementation("com.github.gzu-liyujiang:Android_CN_OAID:4.2.3")

//    debugImplementation ("com.facebook.flipper:flipper:0.115.0")
//    debugImplementation ("com.facebook.soloader:soloader:0.10.1")
//    debugImplementation ("com.facebook.flipper:flipper-network-plugin:0.115.0")
//    releaseImplementation ("com.facebook.flipper:flipper-noop:0.115.0")

    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}

apollo {
    generateKotlinModels.set(true)
}
