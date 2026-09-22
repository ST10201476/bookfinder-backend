package com.example.bookfinder

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import com.example.bookfinder.adapter.BookAdapter
import com.example.bookfinder.network.RetrofitClient
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyText: TextView
    private lateinit var booksRecyclerView: RecyclerView

    private lateinit var bookAdapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        searchEditText =
            findViewById(R.id.searchEditText)

        searchButton =
            findViewById(R.id.searchButton)

        progressBar =
            findViewById(R.id.progressBar)

        emptyText =
            findViewById(R.id.emptyText)

        booksRecyclerView =
            findViewById(R.id.booksRecyclerView)

        findViewById<android.widget.ImageButton>(R.id.settingsButton).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        bookAdapter = BookAdapter(emptyList()) { book ->

            val intent = Intent(
                this,
                BookDetailsActivity::class.java
            )

            val info = book.volumeInfo

            intent.putExtra("bookId", book.id)

            intent.putExtra(
                "title",
                info.title
            )

            intent.putExtra(
                "author",
                info.authors?.joinToString(", ")
            )

            intent.putExtra(
                "publisher",
                info.publisher
            )

            intent.putExtra(
                "date",
                info.publishedDate
            )

            intent.putExtra(
                "pages",
                info.pageCount ?: 0
            )

            intent.putExtra(
                "rating",
                info.averageRating ?: 0.0
            )

            intent.putExtra(
                "description",
                info.description
            )

            intent.putExtra(
                "imageUrl",
                info.imageLinks?.thumbnail
                    ?.replace("http://", "https://")
            )

            startActivity(intent)
        }

        booksRecyclerView.layoutManager =
            LinearLayoutManager(this)

        booksRecyclerView.adapter =
            bookAdapter

        searchButton.setOnClickListener {

            val query =
                searchEditText.text.toString().trim()

            if (query.isEmpty()) {

                Toast.makeText(
                    this,
                    "Please enter a book name",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            searchBooks(query)
        }
    }

    private fun searchBooks(query: String) {

        progressBar.visibility = View.VISIBLE
        emptyText.visibility = View.GONE
        searchButton.isEnabled = false

        lifecycleScope.launch {

            try {

                val response =
                    RetrofitClient.api.searchBooks(query)

                val books =
                    response.items ?: emptyList()

                bookAdapter.updateBooks(books)

                if (books.isEmpty()) {

                    emptyText.text =
                        "No books found."

                    emptyText.visibility =
                        View.VISIBLE
                }

            } catch (e: Exception) {

                emptyText.text =
                    "Could not load books. Check your internet connection."

                emptyText.visibility =
                    View.VISIBLE

                Toast.makeText(
                    this@MainActivity,
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()

            } finally {

                progressBar.visibility =
                    View.GONE

                searchButton.isEnabled =
                    true
            }
        }
    }
}