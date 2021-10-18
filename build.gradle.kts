buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven { url = uri("https://www.jitpack.io" ) }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.1.1")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.2.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven { url = uri("https://www.jitpack.io" ) }
    }
}

