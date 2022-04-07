package com.henryudorji.theater.utils

import java.text.DecimalFormat
import kotlin.math.ln
import kotlin.math.pow


object AppUtils {

    fun coolNumberFormat(count: Long?): String {
        if (count == null) return "..."
        if (count < 1000) return "" + count
        val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
        val format = DecimalFormat("0.#")
        val value: String = format.format(count / 1000.0.pow(exp.toDouble()))
        return String.format("%s%c", value, "kMBTPE"[exp - 1])
    }

}