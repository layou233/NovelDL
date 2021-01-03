package lau.NovelDL

import java.lang.NullPointerException

/**
 * NovelDL, the best application for Chinese readers
 *
 * @author layou233
 */

fun readNovel(plain: String): String {
    // Get the main part of the novel
    val novelStart: Int = plain.indexOf("<divid=\"content\"")
    if (novelStart == -1) throw NullPointerException("Not novel")
    var novel: String = plain.substring(
        plain.indexOf('>', novelStart) + 1,
        plain.indexOf("</div>", novelStart)
    )

    // Process "&nbsp;"s
    novel = Regex("&nbsp;").replace(novel, " ")

    // Process "&quot;"s
    novel = Regex("&quot;").replace(novel, "\"")

    // Process "<br/?>"s
    novel = Regex("<br/?>").replace(novel, "\n")

    // Remove "<a href=url></a>"s
    novel = removePartsFromString(novel, "<ahref", "</a>", "")

    // Remove "<p.*>"s
    novel = removePartsFromString(novel, "<p", ">", "\n")

    // Process "</p>"s
    novel = Regex("</p>").replace(novel, "\n")

    return "\n\n" + novel + "\n\n"
}

fun removePartsFromString(s: String, a: String, b: String, newPart: String): String {
    var result: String = s
    while (result.indexOf(a) > -1) {
        val i: Int = result.indexOf(a)
        result = result.replace(
            result.substring(
                i,
                result.indexOf(b, i) + b.length
            ),
            newPart
        )
    }
    return result
}