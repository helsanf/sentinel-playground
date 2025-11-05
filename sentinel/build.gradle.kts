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

// Function to retrieve version name
fun getVersionName(): String = "1.0.4" // Replace with version name

// Function to retrieve artifact ID
fun getMyArtifactId(): String = "sentinel" // Replace with library name ID

val baseUrl: String = project.property("api_base_url_sentinel") as String

android {
    namespace = "com.edtslib"
    compileSdk = 36

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}
project.afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("bar") {
                groupId = "io.edts" // Replace with group ID
                artifactId = getMyArtifactId()
                version = getVersionName()
                artifact("$buildDir/outputs/aar/${getMyArtifactId()}-release.aar")
            }
        }

        repositories {
            maven {
                name = "GitHubPackages"
                // Configure the URL of your package repository on GitHub
                url = uri("https://maven.pkg.github.com/helsanf/sentinel-playground") // Replace with GitHub details

                credentials {
                    // Use GitHub properties or environment variables for credentials
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
    implementation(libs.androidx.core.ktx.coreKtxVersion)

    implementation(libs.androidx.security.crypto.ktx)
    implementation(libs.converter.gson)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}