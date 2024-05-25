package com.example.geetsunam.downloader

interface Downloader {
    fun downloadFile(url: String, name: String): Long
}