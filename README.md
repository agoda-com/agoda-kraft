# Agoda Kraft

Welcome to Agoda Kraft, where we kraftily craft Kotlin code quality tools! 🛠️

## What is Agoda Kraft?

Agoda Kraft is a collection of opinionated Kotlin code quality tools, born from Agoda's experience working with Kotlin at scale. It includes custom rules for both Ktlint and Detekt, ensuring your codebase stays as robust as a fortress.

## Features

- Custom Ktlint rules to keep your code style consistent
- Detekt rules that detect code smells
- Battle-tested at Agoda, because what doesn't break production makes the code stronger

## Getting Started

### Prerequisites

- JDK 11 or higher
- Gradle 7.0 or higher
- An undying love for clean code (or at least a strong tolerance for it)

### Installation

Add the following to your project's `build.gradle.kts`:

```kotlin
dependencies {
    detektPlugins("io.agodadev:agoda-kraft:0.1.X")
}
```

[Type Resolution](https://detekt.dev/docs/gettingstarted/type-resolution/) needs to be enabled for some of these rules to work

As noted in docs about JVM/Gradle projects

- detekt - Runs detekt `WITHOUT` type resolution
- detektMain - Runs detekt with type resolution on the `main` source set
- detektTest - Runs detekt with type resolution on the `test` source set

So the best way to run these is to run two gradle jobs in CI, one for test and one for main, this will 
also allow you to parallelize in CI, so not necessarily a bad thing imo. 

### Installation (Optional for intelij)

In your project root, create a folder called `.idea`

Create a file called `externalDependencies.xml` and add/merge the following.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="ExternalDependencies">
    <plugin id="detekt" />
  </component>
</project>
```

This will prompt peple to isntall the detekt Intelij plugin, when they open your project for the first time.

Then create a second file called `detekt.xml`, and add/merge this content.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="DetektPluginSettings">
    <option name="enableDetekt" value="true" />
    <option name="enableForProjectResult" value="Accepted" />
    <option name="enableFormatting" value="true" />
    <option name="redirectChannels" value="true" />
    <option name="treatAsErrors" value="true" />
  </component>
</project>
```

This will set the corect setting in the IDE to run detekt in teh background and prompt you if 
there is problems from the IDE itself, isntead of awaiting for a CLI or CI run.

Note: detekt rules that work with Type Resolution do not work in IDE, only in CLI, RE: [this issue](https://github.com/detekt/detekt-intellij-plugin/issues/499)

### Usage

Configure Ktlint and Detekt in your project to use Agoda Kraft rules. Don't worry, they don't bite.

## Development Setup

1. Clone the repository:

```bash
git clone https://github.com/agoda-com/agoda-kraft.git
```

2. Open the project in IntelliJ IDEA.

What's that? You prefer VS Code? Well, we suppose you could use it, but let's just say JetB... Just kidding, use whatever you like – we're not the IDE police.

3. Build the project:

```bash
./gradlew build
```

4. Run tests:

```bash
./gradlew test
```

## Contributing

We welcome contributions! If you've found a bug or have a feature request, please open an issue. If you'd like to contribute code, please fork the repository and submit a pull request.

Remember, static code analysis isn't about enforcing, its about educating people, there's a reason we write code in a particular way to keep it consistent and have less bugs, we all should be brought in and understand why we are writing code in a particular way. Let code analysis tools be a teacher, not a police officers, and it'll make for better engineering culture.

## License

This project is licensed under the Apache 2.0 license - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- The Kotlin community, for creating a language that's a joy to write (and lint)
- Coffee, for powering our late-night coding sessions
- You, for reading this far. You're the real MVP!

Remember, in the world of code quality, you're either Krafty or you're not. Choose wisely!
