
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
