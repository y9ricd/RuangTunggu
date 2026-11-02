package com.ruangtunggu.util

import android.content.Context
import android.widget.Toast
import java.util.Calendar

/**
 * Returns `true` if the string is a valid email format.
 */
fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

/**
 * Returns `true` if the string is a valid NIK (16 digits).
 */
fun String.isValidNik(): Boolean {
    return this.length == 16 && this.all { it.isDigit() }
}

/**
 * Returns `true` if the string is a valid Indonesian phone number.
 * Accepts formats: 08xxx, +628xxx, 628xxx (10-15 digits).
 */
fun String.isValidPhone(): Boolean {
    val cleaned = this.replace("[\\s\\-]".toRegex(), "")
    return cleaned.matches(Regex("^(\\+62|62|0)\\d{9,13}$"))
}

/**
 * Shows a short toast message.
 */
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

/**
 * Returns an Indonesian greeting based on the current hour.
 * - 00:00 – 10:59 → Selamat pagi
 * - 11:00 – 14:59 → Selamat siang
 * - 15:00 – 17:59 → Selamat sore
 * - 18:00 – 23:59 → Selamat malam
 */
fun greeting(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 0..10 -> "Selamat pagi"
        in 11..14 -> "Selamat siang"
        in 15..17 -> "Selamat sore"
        else -> "Selamat malam"
    }
}
