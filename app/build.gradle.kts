plugins { id("com.android.application") }
android {
 namespace = "dev.pixelair.launcher"
 compileSdk { version = release(37) }
 defaultConfig { applicationId = "dev.pixelair.launcher"; minSdk = 33; targetSdk = 36; versionCode = 22; versionName = "0.18.1" }
 compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
}
