package im.nfc.nfsee.utils

import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

object DatetimeUtils {
    private val datePattern = DateTimeFormat.forPattern("yyyyMMdd")
    private val timePattern = DateTimeFormat.forPattern("H:mm")
    private val dateTimePattern = DateTimeFormat.forPattern("yyyyMMddHHmmss")

    fun String.shortDate() = datePattern.parseLocalDate(this)!!

    fun String.shortTime() = timePattern.parseLocalTime(this)!!

    fun String.shortDateTime() = dateTimePattern.parseLocalDateTime(this)!!

    fun LocalDateTime.toShortDateTimeString() = this.toString("yyyyMMddHHmmss")!!

    fun LocalDateTime.toFullDateTimeString() = this.toString("yyyy-MM-dd H:mm:ss")!!

    fun LocalDateTime.toLongDate() = this.toString("yyyy-MM-dd")!!

    fun LocalDateTime.toLongTime() = this.toString("H:mm:ss")!!
}