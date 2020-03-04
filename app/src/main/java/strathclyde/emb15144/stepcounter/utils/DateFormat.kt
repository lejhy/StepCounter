package strathclyde.emb15144.stepcounter.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateFormat {
    @SuppressLint("SimpleDateFormat")
    private val standard = SimpleDateFormat("yyyy-MM-dd")
    fun standardFormat(date: Date): String { return standard.format(date) }
    fun standardParse(string: String): Date { return standard.parse(string)!! }

    @SuppressLint("SimpleDateFormat")
    private val readable = SimpleDateFormat("EEEE, d MMMM yyyy")
    fun readableFormat(date: Date): String { return readable.format(date) }
    fun readableParse(string: String): Date { return readable.parse(string)!! }
}
