package com.sourabhv.bookmania.bookmania

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.li_book.view.*

class BooksAdapter(
        books: List<Book> = listOf()
) : RecyclerView.Adapter<BooksAdapter.BookHolder>() {

    var books = books
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = books.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        return BookHolder(parent.inflate(R.layout.li_book))
    }

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        holder.bind()
    }

    inner class BookHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind() {
            val book = books[adapterPosition]
            itemView.tvTitle.text = book.name
            itemView.tvAuthor.text = book.author
        }

    }
}