package com.example.modapjt.utils

//fun extractYouTubeVideoId(videoUrl: String): String? {
//    val regex = ".*(?:youtu.be/|youtube.com/(?:watch\\?v=|embed/|v/|.+\\?v=))([^&]+).*".toRegex()
//    val match = regex.find(videoUrl)
//    return match?.groups?.get(1)?.value
//}
fun extractYouTubeVideoId(url: String?): String? {
    if (url.isNullOrEmpty()) return null  // url이 null 또는 빈 값이면 null 반환
    val regex = ".*(?:youtu.be/|youtube.com/(?:watch\\?v=|embed/|v/|.+\\?v=))([^&]+).*".toRegex()
    val match = regex.find(url)
    return match?.groups?.get(1)?.value
}