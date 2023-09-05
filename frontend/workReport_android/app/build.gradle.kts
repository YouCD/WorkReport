plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "online.youcd.workreport"
    compileSdk = 33

    defaultConfig {
        applicationId = "online.youcd.workreport"
        minSdk = 29
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }


}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")

    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.cardview:cardview:1.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("com.kizitonwose.calendar:compose:2.3.0")
    implementation("cafe.adriel.bonsai:bonsai-core:1.2.0")
    implementation("cafe.adriel.bonsai:bonsai-file-system:1.2.0")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material:material:1.4.3")
    // 网络请求
    val okHttpVersion = "4.11.0"
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.3.0")
    // 图表
    // https://mavenlibs.com/maven/dependency/com.himanshoe/charty
    implementation("com.himanshoe:charty:2.0.0-alpha01")
    implementation("androidx.compose.ui:ui-util")
    // 翻页器
    implementation("androidx.paging:paging-compose:3.2.0")
    // 持久化
    implementation("androidx.datastore:datastore-preferences:1.1.0-alpha04")
    implementation("androidx.datastore:datastore-core:1.1.0-alpha04")
    // jwt
    implementation("com.auth0.android:jwtdecode:2.0.2")


//    implementation("androidx.paging:paging-runtime:3.2.0")


}
