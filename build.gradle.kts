buildscript {
//    ext {
//        val hilt_version = "2.44"
//        val kotlin_version = "1.8.0"
//    }
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.4.1")
        classpath("com.google.gms:google-services:4.4.1")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.1")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}