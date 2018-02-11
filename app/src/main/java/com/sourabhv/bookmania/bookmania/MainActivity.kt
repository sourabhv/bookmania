package com.sourabhv.bookmania.bookmania

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import io.reactivex.processors.BehaviorProcessor
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var adapter = MainAdapter()
    private lateinit var bookCategories: List<BookCategory>

    private val fileName = "booklist.json"
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val moshiAdapter = moshi.adapter<BookResponse>(BookResponse::class.java)

    private val searchSubject = BehaviorProcessor.create<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvBooks.layoutManager = LinearLayoutManager(this@MainActivity)
        rvBooks.adapter = adapter

        bookCategories = readBookCategories()
        adapter.bookCategories = bookCategories

        etSearch.reactiveSearch({ query ->
            val q = query.toLowerCase().trim()
            adapter.bookCategories = bookCategories.map { category ->
                category.copy(books = category.books.filter {
                    it.name.toLowerCase().contains(q) || it.author.toLowerCase().contains(q)
                })
            }.filter { it.books.isNotEmpty() }
        })

    }

    private fun readBookCategories(): List<BookCategory> {
        val json = application.assets.open(fileName).bufferedReader().use {
            it.readText()
        }
        val bookResponse = moshiAdapter.fromJson(json)
        return if (bookResponse == null) {
            listOf()
        } else {
            val categories = bookResponse.books.groupBy { it.category }
            categories.keys.map {
                BookCategory(it, categories[it]?.map { Book(it.name, it.author) } ?: listOf())
            }
        }
    }

}
