package com.example.task.task

data class Note(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)
