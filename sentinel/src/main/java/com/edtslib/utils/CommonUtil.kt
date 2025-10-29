package com.edtslib.utils

object CommonUtil {

    fun hexToAscii(hex: String): String {
        if (hex.isBlank()) return ""

        val cleanHex = hex.replace("\\s".toRegex(), "")
        if (cleanHex.length % 2 != 0) return ""
        if (!cleanHex.matches("^[0-9a-fA-F]*$".toRegex())) return ""

        val output = StringBuilder(cleanHex.length / 2)
        var i = 0
        while (i < cleanHex.length) {
            try {
                val hexByte = cleanHex.substring(i, i + 2)
                val intValue = hexByte.toInt(16)

                if (intValue < 0 || intValue > 255) return ""

                output.append(intValue.toChar())
                i += 2
            } catch (_: NumberFormatException) {
                return ""
            } catch (_: StringIndexOutOfBoundsException) {
                return ""
            } catch (_: Exception) {
                return ""
            }
        }

        return output.toString()
    }

    fun isValidHex(hex: String): Boolean {
        if (hex.isBlank()) return false
        val cleanHex = hex.replace("\\s".toRegex(), "")
        return cleanHex.length % 2 == 0 && cleanHex.matches("^[0-9a-fA-F]*$".toRegex())
    }

}