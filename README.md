# My Favorite Movies

An Android application that lets users browse popular and top-rated movies, watch trailers, read reviews, and save favorites locally. Powered by [The Movie Database (TMDb)](https://www.themoviedb.org/) API.

## Tech Stack

- **Java** / **Android SDK**
- **Room** persistence library for local favorites database
- **RecyclerView** with grid layout for movie posters
- **ViewModel** / **LiveData** (Architecture Components)
- **Gradle**

## Features

- Browse popular and top-rated movies
- View movie details (poster, rating, overview, release date)
- Watch movie trailers (opens in YouTube)
- Read user reviews
- Save and manage favorite movies (stored locally with Room)
- Sort movies by popularity, rating, or favorites

## Project Structure

```
app/src/main/java/com/e/myfavoritemovies/
├── MainActivity.java                    # Movie grid with sort menu
├── DetailActivity.java                  # Movie detail screen
├── ReviewsActivity.java                 # Reviews list
├── MainViewModel.java                   # ViewModel for favorites
├── Database/
│   ├── AppDatabase.java                 # Room database
│   ├── FavoriteMovieDao.java            # Data access object
│   └── FavoriteMovieEntry.java          # Entity
├── model/
│   ├── Movie.java
│   ├── Trailer.java
│   └── Review.java
└── utils/
    ├── NetworkUtils.java                # TMDb API calls
    ├── JsonUtils.java                   # JSON parsing
    └── DateUtils.java
```

## Prerequisites

- Android Studio
- A TMDb API key — get one at https://www.themoviedb.org/settings/api

## Setup

1. Clone the repository
2. Open in Android Studio
3. Add your TMDb API key in `NetworkUtils.java`
4. Sync Gradle files
5. Run on an emulator or connected device (API 21+)
