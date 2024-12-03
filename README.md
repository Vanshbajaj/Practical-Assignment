## **Rick and Morty Android App**

This is an Android application built using the **MVVM** (Model-View-ViewModel) architecture, following **Clean Architecture** principles. It is designed to fetch data related to characters, episodes, and locations from the Rick and Morty API. The app uses Kotlin Coroutines and Flows for asynchronous programming, and **Dagger 2** for Dependency Injection (DI). Testing is done using **Turbine** and **MockK** for unit and integration tests.

## Features
Displays a list of characters from the Rick and Morty universe.
Fetches and displays episodes and location data.
Implements MVVM architecture with Clean Architecture principles.
Asynchronous programming with Coroutines and Flows.
Dependency Injection with Dagger 2.
Unit and integration tests with MockK and Turbine.

Table of Contents
Architecture
Tech Stack
Dependencies
Setup Instructions
App Structure
Testing

**Presentation Layer (UI):**
Activities/Fragments (UI components).
ViewModels: Handle UI-related data, fetching data from the domain layer and exposing it to the UI.
State Management: LiveData, StateFlow, or other Kotlin Flow-based solutions for UI state.

**Domain Layer**:
UseCases: Contain business logic for each feature, making the app independent of frameworks or UI concerns.
Entities: The core models representing business objects (e.g., Character, Episode, Location).

**Data Layer**:
Repositories: The main point of data fetching, is providing the data to the domain layer.

## Tech Stack
- 🏆 **List of Characters**: View all characters from the Rick and Morty universe.
- 🌍 **Location and Episode Information**: Get details about locations and episodes.
- 🧠 **MVVM Architecture**: Clean separation of concerns using the MVVM pattern.
- 🔄 **Asynchronous with Coroutines**: Efficient background processing.
- ⚡ **Dagger 2 Dependency Injection**: Simplifies and optimizes object management.
- ✅ **Unit and Integration Testing**: Uses **Turbine** and **MockK** for testing.



