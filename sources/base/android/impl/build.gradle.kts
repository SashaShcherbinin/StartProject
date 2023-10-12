plugins {
    id(libs.plugins.common.android.library.module)
}

android {
    namespace = "base.android"
    resourcePrefix("base_android")
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.android.compose.get()
    }
}

dependencies {
    implementation(libs.bundles.compose)

    implementation(projects.base.arch.impl)
    implementation(projects.base.theme.impl)

    debugImplementation(libs.compose.tooling)
}
