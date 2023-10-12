plugins {
    id(libs.plugins.common.android.library.module)
}

android {
    namespace = "base.theme"
    resourcePrefix = "base_theme"
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.android.compose.get()
    }
}

dependencies {
    implementation(libs.compose.material)
    implementation(libs.compose.systemuicontroller)
}
