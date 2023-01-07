object Versions {
    const val gradlePlugin = "7.2.2"
    const val kotlin = "1.7.20"

    const val composeBom = "2022.10.00"
    const val activityComposeVersion = "1.6.1"

    const val timber = "4.7.1"
    const val appCompat = "1.3.0"
    const val material = "1.3.0"
    const val constraintLayout = "1.1.3"

    const val jUnit = "4.12"

    const val accompanistVersion = "0.23.0"

    const val androidXVersion = "1.0.0"
    const val androidXTestCoreVersion = "1.4.1"
    const val androidXTestExtKotlinRunnerVersion = "1.1.4"
    const val androidXTestRulesVersion = "1.4.1"
    const val androidXAnnotations = "1.5.0"
    const val archLifecycleVersion = "2.6.0-alpha03"
    const val archTestingVersion = "2.1.0"
    const val composeVersion = "1.3.1"
    const val composeCompilerVersion = "1.3.2"
    const val coroutinesVersion = "1.6.1"
    const val dexMakerVersion = "2.12.1"
    const val espressoVersion = "3.5.0"
    const val hamcrestVersion = "1.3"
    const val hiltAndroidXVersion = "1.0.0"
    const val hilt = "2.42"
    const val junitVersion = "4.13.2"
    const val multiDexVersion = "2.0.1"
    const val navigationVersion = "2.5.3"
    const val robolectricVersion = "4.8.1"
    const val roomVersion = "2.4.3"
    const val rulesVersion = "1.0.1"
    const val timberVersion = "5.0.1"
    const val truthVersion = "1.1.2"

    const val paging = "3.1.1"
    const val pagingCompose = "1.0.0-alpha17"
    const val coil = "2.2.2"
    const val googleServices = "4.3.13"
    const val googleAuth = "20.4.0"
    const val cloudinary = "2.2.0"
    const val mapsCompose = "2.8.0"
    const val gmsMaps = "18.1.0"
}

object Dependencies {
    const val composeBom = "androidx.compose:compose-bom:${Versions.composeBom}"
    const val material = "androidx.compose.material:material"
    const val composePreview = "androidx.compose.ui:ui-tooling-preview"
    const val debugComposePreview = "androidx.compose.ui:ui-tooling"

    const val composeTest = "androidx.compose.ui:ui-test-junit4"
    const val composeManifestTest = "androidx.compose.ui:ui-test-manifest"

    const val activity = "androidx.activity:activity:${Versions.activityComposeVersion}"
    const val composeActivity = "androidx.activity:activity-compose:${Versions.activityComposeVersion}"
    const val composeViewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.archLifecycleVersion}"
    const val composeViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.archLifecycleVersion}"
    const val composeSavedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.archLifecycleVersion}"
    const val composeRuntime = "androidx.lifecycle:lifecycle-runtime-compose:${Versions.activityComposeVersion}"
    const val androidLifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.archLifecycleVersion}"
    const val androidLifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.archLifecycleVersion}"
    const val navigation = "androidx.navigation:navigation-compose:${Versions.navigationVersion}"

    const val coil = "io.coil-kt:coil:${Versions.coil}"
    const val coilCompose = "io.coil-kt:coil-compose:${Versions.coil}"

    const val accompanistTheme = "com.google.accompanist:accompanist-appcompat-theme:${Versions.accompanistVersion}"
    const val accompanistSwipeRefresh = "com.google.accompanist:accompanist-swiperefresh:${Versions.accompanistVersion}"
    const val accompanistViewPager = "com.google.accompanist:accompanist-pager:${Versions.accompanistVersion}"
    const val accompanistSystemUi = "com.google.accompanist:accompanist-systemuicontroller:${Versions.accompanistVersion}"
    const val accompanistPermissions = "com.google.accompanist:accompanist-permissions:${Versions.accompanistVersion}"

    const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"
    const val hiltNavigation = "androidx.hilt:hilt-navigation-compose:${Versions.hiltAndroidXVersion}"

    const val paging = "androidx.paging:paging-runtime:${Versions.paging}"
    const val pagingCompose = "androidx.paging:paging-compose:${Versions.pagingCompose}"

    const val googleAuth = "com.google.android.gms:play-services-auth:${Versions.googleAuth}"

    const val cloudinary = "com.cloudinary:cloudinary-android:${Versions.cloudinary}"

    const val maps = "com.google.maps.android:maps-compose:${Versions.mapsCompose}"
    const val gmsMaps = "com.google.android.gms:play-services-maps:${Versions.gmsMaps}"

}

object Plugins {
    const val gradle = "com.android.tools.build:gradle:${Versions.gradlePlugin}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val navigation = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigationVersion}"
    const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
    const val googleServices = "com.google.gms:google-services:${Versions.googleServices}"
}

object ConfigData {
    const val compileSdkVersion = 33
    const val buildToolsVersion = "30.0.3"
    const val minSdkVersion = 21
    const val targetSdkVersion = 30
    const val versionCode = 1
    const val versionName = "1.0"
}