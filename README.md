# BookFinder Backend

A Node.js (Express) REST API that powers the saved-books / reading list
feature of the **BookFinder** Android app.

## What it does
- Allows users to save books to a personal reading list
- Retrieves all saved books for a given user
- Stores data in **Google Cloud Firestore** via the Firebase Admin SDK
- CORS enabled so the Android app can call it from any origin
- Deployed on **Railway**

## Base URL (production)
https://bookfinder-backend-production-a2a4.up.railway.app

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET  | `/` | Health check — returns "BookFinder API is running!" |
| POST | `/saveBook` | Saves a book. Body: `{ userId, bookId, title, author }` |
| GET  | `/getSavedBooks?userId=...` | Returns all saved books for that user |

## Tech Stack
- Node.js + Express 5
- Firebase Admin SDK (Firestore)
- Railway (hosting)

## Configuration
Firebase credentials are loaded from the `FIREBASE_SERVICE_ACCOUNT`
environment variable (a service-account JSON). For local development,
a `serviceAccountKey.json` file can be placed in the project root —
this file is excluded via `.gitignore` and must never be committed.

## Run locally
```bash
npm install
node index.js
