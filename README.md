# Messenger

A personal Android messenger project. The app currently focuses on account flows and the authenticated users screen. Messaging features are still in progress.

## Current features

- Sign in with email and password using Firebase Authentication.
- Create an account with email and password.
- Request a password reset email.
- Observe authentication state and sign out.

## Tech stack

- Java
- Firebase Authentication

## Requirements

- Android Studio with a compatible JDK.
- Android SDK Platform 37.
- A Firebase project with an Android app registered using the application ID `com.example.messenger`.

## Firebase setup

Download the `google-services.json` file for your own Firebase project and place it in the app module root (`app/google-services.json`). The file is ignored by Git.


## Build

Open the project in Android Studio, allow Gradle to sync, and run the `app` configuration on an emulator or Android device.


## Project status

This project is under active development. The authentication flows are implemented. The messenger and real user-directory functionality remain to be built.
