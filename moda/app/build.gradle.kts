plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")  // 이 줄 추가
}

android {
    namespace = "com.example.modapjt"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.modapjt"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.generativeai)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
// Retrofit 사용 시 필요한 라이브러리
// Retrofit 의존성
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // JSON 변환을 도와줌

// rememberNavController, NavGraph, NavController : androidx.navigation.compose 패키지에서 제공되는 함수
    implementation("androidx.navigation:navigation-compose:2.5.3") // 네비게이션 관련

// clickable : material3에서 제공하는 기능
    implementation("androidx.compose.material3:material3:1.0.1")  // material3 관련

// 추가
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")


    // 추가
    // Lifecycle 관련 의존성
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.7.0")
    implementation("androidx.lifecycle:lifecycle-service:2.7.0")//service용 Life cycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0")

    // 🔴 중복되던 부분 정리 (버전 통일)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")  // ✅ 통일 완료
    // Compose 관련 의존성 추가 (혹시 없다면)
    implementation("androidx.compose.runtime:runtime:1.5.4")
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.savedstate:savedstate:1.2.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0")

    // Room 의존성 추가
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // AppCompatActivity 위한 라이브러리
    implementation("androidx.appcompat:appcompat:1.6.1")

    // coil라이브러리
    implementation("io.coil-kt:coil-compose:2.2.2")

    //애니메이션을 위한 효과 라이브러리
    implementation ("com.google.accompanist:accompanist-navigation-animation:0.31.3-beta")

}
