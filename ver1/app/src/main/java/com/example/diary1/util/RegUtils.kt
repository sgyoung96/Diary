package com.example.diary1.util

import java.util.regex.Pattern

object RegUtils {
    // 이름이 한글로 이루어져 있는지 체크
    fun checkName(text: String): Boolean {
        return Pattern.matches("^[가-힣]*$", text)
    }

    // 아이디와 비밀번호가 영소문자 + 숫자로 이루어져 있는지 체크
    // checkLetter, checkNumber
    fun checkLetter(text: String): Boolean {
        return Pattern.matches("^[a-z]*$", text)
    }

    fun checkNumber(text: String): Boolean {
        return Pattern.matches("^[0-9]*$", text)
    }
}