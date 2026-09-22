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
        currentTitle = intent.getStringExtra("title")
            ?: getString(R.string.unknown_title)          // ← CHANGE 1
        currentAuthor = intent.getStringExtra("author")
            ?: getString(R.string.unknown_author)         // ← CHANGE 2

        bookTitle.text = currentTitle
        bookAuthor.text = currentAuthor

        bookPublisher.text = getString(R.string.publisher_label,   // ← CHANGE 3
            intent.getStringExtra("publisher")
                ?: getString(R.string.unknown_publisher))

        bookDate.text = getString(R.string.published_label,        // ← CHANGE 4
            intent.getStringExtra("date")
                ?: getString(R.string.unknown_date))

        bookPages.text = getString(R.string.pages_label,           // ← CHANGE 5
            intent.getIntExtra("pages", 0)
                .takeIf { it > 0 }?.toString()
                ?: getString(R.string.unknown_pages))

        bookRating.text = getString(R.string.rating_label,         // ← CHANGE 6
            intent.getDoubleExtra("rating", 0.0)
                .takeIf { it > 0 }?.toString()
                ?: getString(R.string.rating_unavailable))

        bookDescription.text = intent.getStringExtra("description")
            ?: getString(R.string.no_description)                  // ← CHANGE 7

        val imageUrl = intent.getStringExtra("imageUrl")
        bookCover.load(imageUrl)

        saveBookButton.setOnClickListener {
            saveBook()
        }
    }

    private fun saveBook() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, getString(R.string.must_be_logged_in), Toast.LENGTH_SHORT).show()  // ← CHANGE 8
            return
        }

        if (currentBookId == null) {
            Toast.makeText(this, getString(R.string.could_not_identify_book), Toast.LENGTH_SHORT).show()  // ← CHANGE 9
            return
        }

        saveBookButton.isEnabled = false

        lifecycleScope.launch {
            try {
                val response = MyBackendClient.api.saveBook(
                    SaveBookRequest(
                        userId = userId,
                        bookId = currentBookId!!,
                        title = currentTitle ?: getString(R.string.unknown_title),  // ← CHANGE 10
                        author = currentAuthor
                    )
                )

                if (response.isSuccessful) {
                    saveStatusText.text = getString(R.string.book_saved)  // ← CHANGE 11
                    saveStatusText.visibility = View.VISIBLE
                } else {
                    Toast.makeText(
                        this@BookDetailsActivity,
                        getString(R.string.save_failed),                  // ← CHANGE 12
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
