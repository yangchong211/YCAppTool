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


#ijkplayer
-keep class tv.danmaku.ijk.media.player.** {*;}
-keep class tv.danmaku.ijk.media.player.IjkMediaPlayer{*;}
-keep class tv.danmaku.ijk.media.player.ffmpeg.FFmpegApi{*;}


# 视频播放器
# 视频内核
-keep class com.yc.kernel.**{*;}
# 视频播放器
-keep class com.yc.video.**{*;}
# 视频缓存
-keep class com.yc.videocache.**{*;}
# 视频悬浮view
-keep class com.yc.videoview.**{*;}
# 视频位置记录
-keep class com.yc.videosqllite.**{*;}
# 视频m3u8分片下载和合成
-keep class com.yc.m3u8.**{*;}

