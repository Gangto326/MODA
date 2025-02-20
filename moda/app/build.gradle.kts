plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")  // 이 줄 추가
    id("com.google.gms.google-services")
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
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.androidx.benchmark.common)
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

    // 중복되던 부분 정리 (버전 통일)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")  // 통일 완료
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

    implementation("androidx.navigation:navigation-compose:2.7.5") // 최신 버전 확인 후 사용
    implementation("com.google.accompanist:accompanist-navigation-animation:0.32.0") // AnimatedNavHost 관련
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-messaging-ktx")

    //새로고침 때문에
    implementation ("com.google.accompanist:accompanist-swiperefresh:0.28.0")

    implementation ("com.google.accompanist:accompanist-coil:0.15.0")

    implementation("androidx.compose.foundation:foundation:1.4.0")

//    implementation("com.google.accompanist:accompanist-markdown:0.30.1") // 최신 버전 확인 필요, Markdown 자동 변환 표시
//
//    implementation("io.noties.markwon:core:4.6.2") // 기본 마크다운
//    implementation("io.noties.markwon:ext-strikethrough:4.6.2") // 취소선
//    implementation("io.noties.markwon:ext-tasklist:4.6.2") // 체크리스트
//    implementation("io.noties.markwon:ext-tables:4.6.2") // 테이블 렌더링
//    implementation("io.noties.markwon:html:4.6.2") // HTML 태그 지원

//
//
////    // compose-richtext : Jetpack Compose에서 마크다운을 쉽게 렌더링할 수 있는 라이브러리
//    implementation("io.github.halilozercan:compose-richtext-ui:0.17.0")
//    implementation("io.github.halilozercan:compose-richtext-commonmark:0.17.0")
//    implementation("io.github.halilozercan:compose-richtext-material3:0.17.0")
    implementation("org.commonmark:commonmark:0.21.0")
    implementation ("com.squareup.retrofit2:converter-scalars:2.9.0")

    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")


    implementation ("com.squareup.retrofit2:converter-scalars:2.9.0")

    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")

//    implementation("org.commonmark:commonmark:0.21.0")
//    implementation("com.github.jeziellago:compose-markdown:0.5.6")
    // workmanager 의존성 추가
    implementation("androidx.work:work-runtime-ktx:2.10.0")

    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:4.12.0")  // JavaNetCookieJar 포함

}
