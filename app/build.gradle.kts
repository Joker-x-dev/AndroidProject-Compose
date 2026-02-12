plugins {
    // Android 应用构建插件
    alias(libs.plugins.android.application)
    // Compose 编译插件
    alias(libs.plugins.kotlin.compose)
    // Kotlin 序列化插件
    alias(libs.plugins.kotlin.serialization)
    // Hilt 依赖注入插件
    alias(libs.plugins.hilt)
    // KSP 注解处理插件
    alias(libs.plugins.ksp)
}

android {
    // 应用包名（影响最终安装包的 namespace）
    // https://developer.android.com/jetpack/androidx/versions?hl=zh-cn
    namespace = "com.joker.kit"
    // 编译期使用的 SDK 版本
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        // 最终安装包 ID
        applicationId = "com.joker.kit"
        // 支持的最低 Android 版本
        minSdk = 23
        // Play 建议的目标 Android 版本
        targetSdk = 36
        // 递增的内部版本号
        versionCode = 1
        // 显示给用户的版本名称
        versionName = "1.0.0"

        // Instrumentation 测试入口
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // 仅包括中文和英文必要的语言资源
        androidResources {
            // 仅保留常用语种以减小包体
            @Suppress("UnstableApiUsage")
            localeFilters += listOf("zh", "en")
        }
    }

    // ABI 分包配置 - 一次性打包多个架构版本
    splits {
        abi {
            // 启用 ABI 分包
            isEnable = true
            // 重置默认列表
            reset()
            // 包含的架构：32位和64位 ARM
            include("armeabi-v7a", "arm64-v8a")
            // 是否生成通用 APK（包含所有架构）
            // 设置为 true 会额外生成一个包含所有架构的 APK
            isUniversalApk = true
        }
    }

    // 签名配置
    signingConfigs {
        // 通用签名配置
        // 如果你的项目需要获取 MD5 或 SHA-1 签名值来做类似三方集成（如微信/支付宝）
        // 完善以下配置并在 buildTypes 中解除注释后项目中执行命令 ./gradlew signingReport 来获取签名信息
        create("common") {
            // 签名文件路径
            storeFile = file("你的签名文件路径")
            // 密钥别名
            keyAlias = "你的密钥别名"
            // 密钥密码
            keyPassword = "你的密钥密码"
            // 签名文件密码
            storePassword = "你的签名文件密码"

            // 启用所有签名方案以确保最大兼容性
            // JAR 签名 (Android 1.0+)
            enableV1Signing = true
            // APK 签名 v2 (Android 7.0+)
            enableV2Signing = true
            // APK 签名 v3 (Android 9.0+)
            enableV3Signing = true
            // APK 签名 v4 (Android 11.0+)
            enableV4Signing = true
        }
    }

    buildTypes {
        // debug 构建类型
        debug {
            // debug 模式下的签名配置（默认使用 debug 签名）
            // signingConfig = signingConfigs.getByName("common")
            // debug 模式下包名后缀
            applicationIdSuffix = ".debug"
            // debug 模式下的请求 url 地址
            buildConfigField("String", "BASE_URL", "\"https://mall.dusksnow.top/app/\"")
            // 根据当前构建类型是否为 debug 模式来判断是否开启 debug 模式
            buildConfigField("Boolean", "DEBUG", "true")
        }

        // release 构建类型
        release {
            // 是否启用代码压缩
            isMinifyEnabled = true
            // 是否启用资源压缩
            isShrinkResources = true
            // 正式发布模式下的签名配置（配置完 common 签名配置后，取消注释以下行）
            // signingConfig = signingConfigs.getByName("common")
            // 正式发布模式下的请求 url 地址
            buildConfigField("String", "BASE_URL", "\"https://mall.dusksnow.top/app/\"")
            // 根据当前构建类型是否为 debug 模式来判断是否开启 debug 模式
            buildConfigField("Boolean", "DEBUG", "false")
            // 混淆规则文件
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        // Java 源码/字节码兼容级别
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        // 开启 Compose 支持
        compose = true
        // 开启 BuildConfig 支持
        buildConfig = true
    }
}

dependencies {
    // AndroidX Core 基础
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)

    // Jetpack Compose UI
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.coil.compose)

    // 导航组件
    implementation(libs.navigation.compose)

    // 序列化
    implementation(libs.kotlinx.serialization.json)

    // 网络请求 (Retrofit + OkHttp)
    implementation(libs.retrofit)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.no.op)

    // 权限
    implementation(libs.xxpermissions)

    // toast
    implementation(libs.toaster)

    // 数据存储
    implementation(libs.mmkv)

    // 日志
    implementation(libs.timber)

    // 依赖注入 (Hilt + Navigation)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.android.compiler)

    // 数据库 (Room)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)
    kspTest(libs.androidx.room.compiler)
    kspAndroidTest(libs.androidx.room.compiler)
    testImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.androidx.room.testing)

    // 调试工具
    debugImplementation(libs.leakcanary.android)

    // 单元 / UI 测试
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}