


# BookFinder

BookFinder is an Android app that lets users search for books via the
Google Books API, view book details, and save titles to a personal
reading list.

## Features
- 🔍 Search books using the Google Books API
- 🌐 Multilingual UI — English, Afrikaans, and isiZulu
- 🌙 Dark mode toggle
- 👤 User login (Firebase)
- 🔖 Save books to a personal reading list, synced via a Node.js/Firestore
  backend hosted on Railway

## Backend
The companion backend lives here:
https://github.com/ST10201476/bookfinder-backend

and is deployed at:
https://bookfinder-backend-production-a2a4.up.railway.app

## Tech Stack
- Kotlin, XML layouts (View system)
- Firebase Authentication
- Custom REST backend: Node.js + Express + Cloud Firestore (Railway)
