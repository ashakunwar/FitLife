# FitLife - Android Fitness Planner

FitLife is an app designed to help users plan and track their workout routines.

## Core Features

*   **User Accounts:** Full user registration and login system. Passwords are encrypted before being saved.
*   **Create & Manage Routines:** You can create custom workout routines with a name, instructions, and a photo.
*   **Dynamic Exercise List:** Add as many exercises as you want to a routine, each with its own sets and reps.
*   **Weekly Planner:** Assign your saved routines to specific days of the week (Monday to Sunday) to build your schedule.
*   **Mark as Completed:** Check off routines on the main list or on the planner to track your progress.
*   **Delete & Edit:** You can delete or edit any routine you've created through a simple options menu.
*   **SMS Delegation:** Share a routine's equipment list with a friend via SMS directly from the app.

## Desirable Feature Implemented: Geotagging

*   **Save Workout Locations:** You can long-press on the map screen to save your favorite workout spots like your gym, a local park, or a yoga studio.
*   **Link to Routines:** When creating a routine, you can link it to one of your saved locations.
*   **Navigate:** If a routine has a location linked, a "Navigate" button will appear on its card, which opens Google Maps to give you directions.

## How to Run the App

1.  Clone the project into Android Studio.
2.  You will need to get a Google Maps API key.
3.  Create a file named `local.properties` in the root of the project folder.
4.  Add your API key to that file like this: `MAPS_API_KEY=YOUR_API_KEY`
5.  Sync the Gradle project and run the app.

## Tech Used

*   **Language:** Java
*   **Database:** Room for local data storage
*   **UI:** Material Design 3 Components
*   **APIs:** Google Maps SDK, SMSManager
