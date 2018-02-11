package com.sourabhv.bookmania.bookmania

data class BookCategory(
        val name: String,
        val books: List<Book>
)

data class Book(
        val name: String,
        val author: String
)
