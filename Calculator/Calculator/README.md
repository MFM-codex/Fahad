# Calculator (Android, Kotlin)

A small native Android calculator app with a dark theme UI, supporting:
- Addition, subtraction, multiplication, division
- Percent (%)
- Sign toggle (+/-)
- Decimal input
- Clear (AC)
- Chained operations (e.g. 2 + 3 × 4 =)

## How to open

1. Open Android Studio → **Open** → select this `Calculator` folder.
2. Let Gradle sync (it will download the wrapper the first time since
   `gradlew`/`gradlew.bat` scripts aren't bundled here — Android Studio
   will offer to regenerate them automatically, or run
   `gradle wrapper` once if you have Gradle installed locally).
3. Run on an emulator or device (▶ button), min SDK 21.

## Project structure

```
Calculator/
├── build.gradle              # root build config
├── settings.gradle
├── gradle.properties
├── gradle/wrapper/           # wrapper properties
└── app/
    ├── build.gradle          # app module config (Kotlin, viewBinding)
    └── src/main/
        ├── AndroidManifest.xml
        ├── java/com/example/calculator/
        │   └── MainActivity.kt   # all calculator logic
        └── res/
            ├── layout/activity_main.xml
            └── values/ (strings, colors, themes, styles)
```

## Notes

- Written in Kotlin, uses `BigDecimal` for accurate arithmetic (avoids
  floating point rounding issues).
- No external dependencies beyond standard AndroidX/Material libraries.
- Divide-by-zero returns 0 instead of crashing (feel free to change this
  to show an "Error" state).
