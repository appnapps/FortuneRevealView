# 🔮 FortuneRevealView

A fun scratch card UI component for Android — now with particle explosion animation!

## ✨ Features
- Scratch-to-reveal interaction
- Auto-reveal after 50% scratched
- Fade-out mask effect
- 🎆 Particle explosion when revealed
- Reset support

## 📦 Installation (via JitPack)

Add the JitPack repository to your root `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
```

Then add this dependency to your app-level build.gradle.kts:
```kotlin
dependencies {
    implementation("com.github.appnapps:FortuneRevealView:1.0.0")

}
```

🛠 Usage
In XML
```kotlin
<com.appnapps.fortunerevealview.FortuneRevealView
android:layout_width="match_parent"
android:layout_height="300dp" />

```



Run it to see the animation in action!

<img src="https://github.com/appnapps/FortuneRevealView/blob/main/docs/FortuneRevealView.gif" width="320"/>