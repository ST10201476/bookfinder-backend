package com.example.bookfinder

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.bookfinder.network.MyBackendClient
import com.example.bookfinder.network.SaveBookRequest
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class BookDetailsActivity : AppCompatActivity() {

    private lateinit var bookCover: ImageView
    private lateinit var bookTitle: TextView
    private lateinit var bookAuthor: TextView
    private lateinit var bookPublisher: TextView
    private lateinit var bookDate: TextView
    private lateinit var bookPages: TextView
    private lateinit var bookRating: TextView
    private lateinit var bookDescription: TextView
    private lateinit var saveBookButton: Button
    private lateinit var saveStatusText: TextView

    private var currentBookId: String? = null
    private var currentTitle: String? = null
    private var currentAuthor: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_book_details)

        bookCover = findViewById(R.id.detailBookCover)
        bookTitle = findViewById(R.id.detailBookTitle)
        bookAuthor = findViewById(R.id.detailBookAuthor)
        bookPublisher = findViewById(R.id.detailBookPublisher)
        bookDate = findViewById(R.id.detailBookDate)
        bookPages = findViewById(R.id.detailBookPages)
        bookRating = findViewById(R.id.detailBookRating)
        bookDescription = findViewById(R.id.detailBookDescription)
        saveBookButton = findViewById(R.id.saveBookButton)
        saveStatusText = findViewById(R.id.saveStatusText)

        currentBookId = intent.getStringExtra("bookId")
        currentTitle = intent.getStringExtra("title") ?: "Unknown title"
        currentAuthor = intent.getStringExtra("author") ?: "Unknown author"

        bookTitle.text = currentTitle

        bookAuthor.text = currentAuthor

        bookPublisher.text =
            "Publisher: " +
                    (intent.getStringExtra("publisher")
                        ?: "Unknown publisher")

        bookDate.text =
            "Published: " +
                    (intent.getStringExtra("date")
                        ?: "Unknown date")

        bookPages.text =
            "Pages: " +
                    (intent.getIntExtra("pages", 0)
                        .takeIf { it > 0 } ?: "Unknown")

        bookRating.text =
            "Rating: " +
                    (intent.getDoubleExtra("rating", 0.0)
                        .takeIf { it > 0 } ?: "Not available")

        bookDescription.text =
            intent.getStringExtra("description")
                ?: "No description available."

        val imageUrl =
            intent.getStringExtra("imageUrl")

        bookCover.load(imageUrl)

        saveBookButton.setOnClickListener {
            saveBook()
        }
    }

    private fun saveBook() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "You must be logged in to save books", Toast.LENGTH_SHORT).show()
            return
        }

        if (currentBookId == null) {
            Toast.makeText(this, "Could not identify this book", Toast.LENGTH_SHORT).show()
            return
        }

        saveBookButton.isEnabled = false

        lifecycleScope.launch {
            try {
                val response = MyBackendClient.api.saveBook(
                    SaveBookRequest(
                        userId = userId,
                        bookId = currentBookId!!,
                        title = currentTitle ?: "Unknown title",
                        author = currentAuthor
                    )
                )

                if (response.isSuccessful) {
                    saveStatusText.text = "Book saved to your list!"
                    saveStatusText.visibility = View.VISIBLE
                } else {
                    Toast.makeText(
                        this@BookDetailsActivity,
                        "Failed to save book",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@BookDetailsActivity,
                    "Error: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                saveBookButton.isEnabled = true
            }
        }
    }
}