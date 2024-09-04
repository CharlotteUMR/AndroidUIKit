plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.mavenPublish)
}
android {
    namespace = "com.jerry.uikit"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
}

val publishVersion: String by extra
val publishGroupId: String by extra
val publishArtifactId: String by extra
publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = publishGroupId
            artifactId = publishArtifactId
            version = publishVersion

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}