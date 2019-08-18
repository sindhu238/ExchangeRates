# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable,*Annotation*, InnerClasses

-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class com.example.exchangeRates.model.**$$serializer { *; }
-keepclassmembers class com.example.exchangeRates.model.** {
    *** Companion;
}
-keepclasseswithmembers class com.example.exchangeRates.model.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-renamesourcefileattribute SourceFile
