plugins {
    id(libs.plugins.common.android.library.module)
}

android {
    namespace = "base.logger"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.base.logger.api)
    implementation(projects.base.common.impl)
    implementation(libs.koin.core)
    implementation(libs.google.firebase.crashlytics.ktx)
}
