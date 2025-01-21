package com.example.kotlinstudy.util.value.func

import jakarta.servlet.http.HttpServletResponse
import java.io.PrintWriter

fun responseData(resp: HttpServletResponse, jsonData: String?) {
    val out: PrintWriter
    println(" 응답데이터 : $jsonData")
    resp.setHeader("Content-Type", "application/json; charset=UTF-8")
    try {
        out = resp.writer
        out.println(jsonData)
        out.flush()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}