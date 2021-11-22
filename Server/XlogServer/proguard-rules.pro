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
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile



-dontwarn  com.yc.logging.**
-keepattributes *Annotation*
-keep class com.yc.logging.annotation.KeepClass
-keep @com.yc.logging.annotation.KeepClass class * {*;}
-keepclasseswithmembers class * {
    @com.yc.logging.annotation.KeepClass <methods>;
}
-keepclasseswithmembers class * {
    @com.yc.logging.annotation.KeepClass <fields>;
}
-keepclasseswithmembers class * {
    @com.yc.logging.annotation.KeepClass <init>(...);
}


-keep class com.yc.logging.annotation.KeepSource
-keep @com.yc.logging.annotation.KeepSource class * {*;}
-keepclasseswithmembers class * {
    @com.yc.logging.annotation.KeepSource <methods>;
}
-keepclasseswithmembers class * {
    @com.yc.logging.annotation.KeepSource <fields>;
}
-keepclasseswithmembers class * {
    @com.yc.logging.annotation.KeepSource <init>(...);
}
