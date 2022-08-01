# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## [1.2.2]

### Added
- Added support for Intellij 2022.2
- Upgraded intellij gradle plugin to 1.7

## [1.2.1]

### Fixed
- Fixed NPE error in groovy completion when Inferred type is not found

## [1.2.0]

### Added
- Added completion for a Generic Value to a certain extend (still heavily beta)
- Added some tests, still a lot of tests to add
- Added reference in groovy towards services in run instruction
  - Ex : `run service: "createProductPromo"`

### Changed
- Updated test architecture
- Updated Intellij and groovy dependencies

### Fixed
- Fixed a broken reference to View Entities in groovy

## [1.1.1]

### Added
- Added compatibility with Intellij 2022

### Changed
- Updated intellij Gradle plugin version
- Updated Groovy lib Version
- Updated Junit version

### Fixed

## [1.1.0]

### Added
- Added (pretty) completion for service and entity name in groovy, java, and xml files
- Added tests for completion
- Added some tests for entity reference and screen reference (not extensive though)
- Added a plugin main logo
- Added some custom icons for some files as a test, more to come later.

### Changed

### Fixed
- Wrong screen reference returned in case of two screens that wear the same name in different components inside a controller file
- Fixed a bug that couldn't find a file when the path contained the component name

## [1.0.1] 2021-10-20

### Added
- Added support fort Intellij version 2021-3

### Changed

### Fixed

## [1.0.0] - 2021-10-20

### Added
- references from xml towards entity, view-entity, and services definitions, as well as labels, screens, forms and menus.
- references from java towards entity, view-entity, and services definitions as well as labels.
- [WIP] references from groovy towards entity, view-entity, and services definitions.

### Changed

### Fixed