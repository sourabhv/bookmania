package com.sourabhv.bookmania.bookmania

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.li_category.view.*

class MainAdapter(
        bookCategories: List<BookCategory> = listOf()
) : RecyclerView.Adapter<MainAdapter.CategoryHolder>() {

    var bookCategories = bookCategories
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = bookCategories.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        return CategoryHolder(parent.inflate(R.layout.li_category))
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) = holder.bind()

    inner class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val adapter = BooksAdapter()

        init {
            itemView.rvBooks.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            itemView.rvBooks.adapter = adapter
        }

        fun bind() {
            val category = bookCategories[adapterPosition]
            itemView.tvLabel.text = category.name
            adapter.books = category.books
        }

    }

}