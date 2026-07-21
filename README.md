# QuickLunch

QuickLunch is an Android application designed to help teachers and cafeteria staff coordinate lunch attendance more efficiently. The app uses Firebase for authentication and real-time data synchronization, making it easier for school staff to manage student lunch counts and meal participation from mobile devices.

## Overview

QuickLunch was built for a school lunch workflow where teachers and cafeteria staff need fast, shared visibility into lunch attendance. Instead of relying on paper counts or delayed communication, the app provides role-based screens for hosts and guests so lunch data can be entered, viewed, and updated from an Android device.

## Features

- User sign up and login flows
- Host and guest account paths
- Firebase Authentication integration
- Firebase Realtime Database integration
- Separate host and guest screens
- Existing-user and new-user flows
- Forgot password screen
- Android native UI built with Java and XML layouts
- Real-time synchronization for shared lunch attendance data

## Tech Stack

- Java
- Android SDK
- Android Studio
- Gradle Kotlin DSL
- Firebase Authentication
- Firebase Realtime Database
- Material Components
- AppCompat
- ConstraintLayout

## Project Structure

```text
app/src/main/java/com/example/quicklunch/
- SignUpActivity.java          # Entry/sign-up screen
- HostSignUpActivity.java      # Host registration flow
- GuestSignUpActivity.java     # Guest registration flow
- NewHostActivity.java         # New host workflow
- NewGuestActivity.java        # New guest workflow
- ExistingHostActivity.java    # Existing host workflow
- ExistingGuestActivity.java   # Existing guest workflow
- MainHostActivity.java        # Main host dashboard
- MainGuestActivity.java       # Main guest dashboard
- ForgotPassword.java          # Password reset flow
- FirebaseHelper.java          # Firebase-related helper logic
- User.java                    # User model

app/src/main/res/layout/
- XML layouts for the Android screens
```

## Requirements

- Android Studio
- JDK 8 or newer
- Android SDK 34
- Firebase project
- Android device or emulator

## Firebase Setup

This project uses Firebase services. To run the app locally, create a Firebase project and add the Android app package:

```text
com.example.quicklunch
```

Then download your Firebase configuration file:

```text
google-services.json
```

Place it here:

```text
app/google-services.json
```

Do not commit real Firebase credentials or private project configuration files to a public repository.

## How To Run

1. Clone the repository:

```bash
git clone https://github.com/elvissanchez73/QuickLunch.git
```

2. Open the project in Android Studio.

3. Add your `google-services.json` file to the `app/` folder.

4. Let Gradle sync the project.

5. Run the app on an Android emulator or physical device.

## Main Workflows

### Host Flow

Hosts can sign up or continue as existing users, then access host-specific screens for managing lunch-related data.

### Guest Flow

Guests can sign up or continue as existing users, then access guest-specific screens for submitting or viewing lunch-related information.

### Shared Firebase Data

Firebase keeps the app data synchronized so changes made by one user type can be reflected for the other user type.

## What I Learned

- Building a native Android app with Java
- Creating multi-screen Android navigation
- Using Firebase Authentication
- Reading and writing data with Firebase Realtime Database
- Structuring an Android project with activities, models, helpers, and XML layouts
- Managing role-based user flows

## Future Improvements

- Improve validation and error messages
- Add stronger role-based authorization rules in Firebase
- Add automated tests
- Improve UI responsiveness on different screen sizes
- Add reports or summaries for cafeteria staff
- Add offline support for unreliable school network conditions

## Author

Built by [Elvis J. Sanchez Robles](https://github.com/elvissanchez73).
