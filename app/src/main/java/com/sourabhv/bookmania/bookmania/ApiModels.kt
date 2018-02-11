package com.sourabhv.bookmania.bookmania

import com.squareup.moshi.Json

class BookResponse(
        @Json(name = "books") val books: List<BookResponseContent>
)

class BookResponseContent(
        @Json(name = "name") val name: String,
        @Json(name = "author") val author: String,
        @Json(name = "category") val category: String
)