package com.example.bookfinder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.bookfinder.R
import com.example.bookfinder.model.BookItem

class BookAdapter(
    private var books: List<BookItem>,
    private val onBookClick: (BookItem) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val cover: ImageView =
            itemView.findViewById(R.id.bookCover)

        val title: TextView =
            itemView.findViewById(R.id.bookTitle)

        val author: TextView =
            itemView.findViewById(R.id.bookAuthor)

        val date: TextView =
            itemView.findViewById(R.id.bookDate)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)

        return BookViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: BookViewHolder,
        position: Int
    ) {

        val book = books[position]
        val info = book.volumeInfo

        holder.title.text =
            info.title ?: "Unknown title"

        holder.author.text =
            info.authors?.joinToString(", ")
                ?: "Unknown author"

        holder.date.text =
            info.publishedDate ?: "Unknown date"

        val imageUrl =
            info.imageLinks?.thumbnail
                ?.replace("http://", "https://")

        holder.cover.load(imageUrl)

        holder.itemView.setOnClickListener {
            onBookClick(book)
        }
    }

    override fun getItemCount(): Int {
        return books.size
    }

    fun updateBooks(newBooks: List<BookItem>) {
        books = newBooks
        notifyDataSetChanged()
    }
}