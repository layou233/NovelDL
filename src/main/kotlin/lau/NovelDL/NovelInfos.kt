package lau.NovelDL

/**
 * NovelDL, the best application for Chinese readers
 *
 * @author layou233
 */

fun getUrlFromHrefValue(s: String, value: String): String {
    var i: Int = s.substring(0, s.indexOf(value)).lastIndexOf("href=") + 6
    return s.substring(i, s.indexOf('"', i + 1))
}

fun getChapterTitle(s: String): String {
    val re: String = Regex("<h1>.*</h1>").find(s)!!.value
    return re.substring(4, re.length - 5).trim()
}

fun getListIdFromUrl(url: String): String = url.substring(0, url.lastIndexOf("/") + 1)

fun getCharset(s: String): String {
    var i: Int = s.indexOf("charset=") + 8
    return s.substring(i, s.indexOf('"', i + 1)).toUpperCase()
}