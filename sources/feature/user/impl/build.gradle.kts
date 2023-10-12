plugins {
    id(libs.plugins.common.android.library.module)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "feature.user"
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.android.compose.get()
    }
    resourcePrefix("user")
}

dependencies {
    implementation(libs.bundles.compose)
    implementation(libs.bundles.network)
    implementation(libs.coroutines.core)
    implementation(libs.image.coil)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.kotlinx.serialization.json)

    implementation(projects.base.arch.impl)
    implementation(projects.base.android.impl)
    implementation(projects.base.common.impl)
    implementation(projects.base.logger.api)
    implementation(projects.base.storage.impl)
    implementation(projects.feature.user.api)
    implementation(projects.feature.dashboard.api)
}
