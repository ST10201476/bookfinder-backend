const express = require("express");
const cors = require("cors");
const { initializeApp, cert } = require("firebase-admin/app");
const { getFirestore } = require("firebase-admin/firestore");

const serviceAccount = process.env.FIREBASE_SERVICE_ACCOUNT
    ? JSON.parse(process.env.FIREBASE_SERVICE_ACCOUNT)
    : require("./serviceAccountKey.json");

initializeApp({
    credential: cert(serviceAccount)
});

const db = getFirestore();
const app = express();

app.use(cors());
app.use(express.json());

// Test endpoint
app.get("/", (req, res) => {
    res.send("BookFinder API is running!");
});

// Save a book to the user's reading list
app.post("/saveBook", async (req, res) => {
    try {
        const { userId, bookId, title, author } = req.body;

        if (!userId || !bookId || !title) {
            return res.status(400).send({ error: "Missing required fields" });
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
app.get("/getSavedBooks", async (req, res) => {
    try {
        const userId = req.query.userId;

        if (!userId) {
            return res.status(400).send({ error: "Missing userId parameter" });
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

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server running on port ${PORT}`);
});