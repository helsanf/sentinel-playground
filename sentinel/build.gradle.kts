import java.io.FileInputStream
import java.util.Properties

plugins {
    `maven-publish`
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}
val githubProperties = Properties().apply {
    load(FileInputStream(rootProject.file("github.properties")))
}

fun getVersionName(): String = "1.0.10"
fun getMyArtifactId(): String = "sentinel"

val baseUrl: String = project.property("api_base_url_sentinel") as String

android {
    namespace = "com.edtslib"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            buildConfigField("String","BASE_URL","\"$baseUrl\"")

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

    publishing {
        singleVariant("release") {
            withSourcesJar()      // Users can "Go to Definition" in their IDE
            withJavadocJar()      // Better documentation support
        }
    }
}
project.afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("bar") {
                from(components["release"])
                groupId = "io.edts"
                artifactId = getMyArtifactId()
                version = getVersionName()
            }
        }

        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/helsanf/sentinel-playground")

                credentials {
                    username = githubProperties["GITHUB_USERID"] as String? ?: System.getenv("GPR_USER")
                    password = githubProperties["PERSONAL_ACCESS_TOKEN"] as String? ?: System.getenv("GPR_API_KEY")
                }
            }
        }
    }
}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.preference.ktx)

    //koin
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.stetho)
    implementation(libs.stetho.okhttp3)

    /* coroutines */
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.androidx.security.crypto.ktx)
    implementation(libs.converter.gson)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
}