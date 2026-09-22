const { onRequest } = require("firebase-functions/v2/https");
const { initializeApp } = require("firebase-admin/app");
const { getFirestore } = require("firebase-admin/firestore");

initializeApp();
const db = getFirestore();

// Simple test endpoint to confirm everything works
exports.helloBookFinder = onRequest((req, res) => {
    res.send("BookFinder API is working!");
});

// Save a book to the user's reading list
exports.saveBook = onRequest(async (req, res) => {
    try {
        const { userId, bookId, title, author } = req.body;

        if (!userId || !bookId || !title) {
            res.status(400).send({ error: "Missing required fields" });
            return;
        }

        await db.collection("savedBooks").add({
            userId,
            bookId,
            title,
            author: author || "Unknown",
            savedAt: new Date().toISOString()
        });

        res.status(200).send({ message: "Book saved successfully" });
    } catch (error) {
        res.status(500).send({ error: error.message });
    }
});

// Get all saved books for a user
exports.getSavedBooks = onRequest(async (req, res) => {
    try {
        const userId = req.query.userId;

        if (!userId) {
            res.status(400).send({ error: "Missing userId parameter" });
            return;
        }

        const snapshot = await db.collection("savedBooks")
            .where("userId", "==", userId)
            .get();

        const books = [];
        snapshot.forEach(doc => {
            books.push({ id: doc.id, ...doc.data() });
        });

        res.status(200).send({ books });
    } catch (error) {
        res.status(500).send({ error: error.message });
    }
});