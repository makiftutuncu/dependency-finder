# Dependency Finder

## Table of Contents

1. [Introduction](#introduction)
2. [Development, Running and Testing](#development-running-and-testing)
3. [Contributing](#contributing)
4. [License](#license)

## Introduction

Dependency Finder is an interactive console application for reading a file with dependency declarations and finding transitive dependencies.

Dependency definitions file should be in following format:

```
A B C
B C E
C G
D A F
E F
F H
```

Each line represents a dependency definition. For example:

* `A` is a dependency and it depends on `B` and `C`
* `B` is a dependency and it depends on `C` and `E`

and so on. Based on dependency definitions above, transitive dependencies of `A` would be `B`, `C`, `E`, `F`, `G` and `H`.

## Development, Running and Testing

Application is built with Gradle. So, standard Gradle tasks like `clean`, `compile`,  `run` and `test` can be used. If you don't have Gradle installed, you can use Gradle wrapper by replacing `gradle` command with `./gradlew` in project root directory.

To run the application locally:

```bash
gradle run --console=plain --args '/path/to/dependencies.txt'
```

To run all tests:

```bash
gradle test
```

To run specific test(s):

```bash
gradle test --tests 'patternForTests'
```

## Contributing

All contributions are welcome. Please feel free to send a pull request. Thank you.

## License

Dependency Finder is licensed with [MIT License](LICENSE.md).
