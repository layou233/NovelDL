package lau.NovelDL

/**
 * NovelDL, the best application for Chinese readers
 *
 * @author layou233
 */

fun readln(): String = readLine()!!

fun regexFind(regex: String, string: String): String = Regex(regex).find(string)!!.value