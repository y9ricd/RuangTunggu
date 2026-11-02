# ─── ProGuard Rules for RuangTunggu ───────────────────────────────────────────

# ── General ───────────────────────────────────────────────────────────────────
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# ── Kotlin Serialization ─────────────────────────────────────────────────────
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.ruangtunggu.**$$serializer { *; }
-keepclassmembers class com.ruangtunggu.** {
    *** Companion;
}
-keepclasseswithmembers class com.ruangtunggu.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ── Supabase / Ktor ──────────────────────────────────────────────────────────
-keep class io.github.jan.supabase.** { *; }
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

# ── Hilt ──────────────────────────────────────────────────────────────────────
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel

# ── Compose ───────────────────────────────────────────────────────────────────
-dontwarn androidx.compose.**

# ── ZXing ─────────────────────────────────────────────────────────────────────
-keep class com.google.zxing.** { *; }
-dontwarn com.google.zxing.**

# ── Coil ──────────────────────────────────────────────────────────────────────
-keep class coil3.** { *; }
-dontwarn coil3.**

# ── Firebase ──────────────────────────────────────────────────────────────────
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# ── Google Play Services ─────────────────────────────────────────────────────
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# ── reCAPTCHA ─────────────────────────────────────────────────────────────────
-keep class com.google.android.recaptcha.** { *; }

# ── Data classes (prevent obfuscation of model fields) ────────────────────────
-keepclassmembers class com.ruangtunggu.data.model.** { *; }

# ── Enum preservation ────────────────────────────────────────────────────────
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
