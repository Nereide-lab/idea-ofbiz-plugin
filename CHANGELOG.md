# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased] The completion update: Completion EVERYWHERE
### Added
- [#4] Added Completion in xml for Forms names
- [#4] Added Completion in xml for Screen names
- [#4] Added Completion in xml for Menus names
- [#4] Added Completion in xml for target uris
- Created a dedicated file for completion contributors

### Changed
- Made some changes in the Project structure interface object for more readability
- Switched back to separated Reference objects for screen, menus, and forms to increase readability
### Fixed
- Fixed a small typo with folder references in component based references


## [1.5.9]
### Fixed
- [#2] Fixes references in xml labels in case there is not only a label in the attribute value.

## [1.5.8]
### Changed
- [#3] Returns a multi reference for services in case of multiple implementations for a single service name.

## [1.5.7]
### Added
- Adds 'path' attribute to checked files locations
- Adds form not found inspection with quickfix
- Adds screen not found inspection
### Fixed
- Removes the dynamic elements or elements with dynamic location from patterns

## [1.5.6]
### Added
- Added an option to create a file on file not found detection
- Rollback switching to latest-eap for tests (unstable groovy handling)

## [1.5.5]
### Fixed
- Fixed an NPE in InspectionUtil in non OFBIZ projects

## [1.5.4]

### Added
- Updated Intellij gradle plugin from 15 to 16
- Added inspection on cache call on a never-cache entity

## [1.5.3]

### Added
- Updated Tests to run on latest EAP version
- Updated groovy version to Groovy 4
- Updated to Intellij version 23.3

## [1.5.2]

### Added
- Updated to Intellij version 23.2
- Updated Intellij gradle plugin

## [1.5.1]

### Fixed
- Fixed the recuperation of component dirs that brought back test directories and broke some references

### Added
- Added code inspection in xml for strings in location attribute that checks if there is indeed a file at the end of the path.

## [1.5.0] Entity fields completion update

### Added
- Added entity fields completion in Java, Groovy, and Xml

## [1.4.8]

### Fixed
- Fixed security issues mentioned in https://youtrack.jetbrains.com/issue/IDEA-317841/Upgrading-plugin-from-2022.3-to-2023.1-breaks-language-injection-tests

### Changed
- Updated to 223.1 version
- Commented Groovy injection tests until they are repaired

## [1.4.7]

### Added
- Added a specific pattern for grids instead of using the form one
- Added a control for references singleness in tests

### Fixed
- Fixed a bug when getting reference for a form un compounds

### Changed
- Splitting patterns in extension specific files
- Again, massive refactoring of patterns
- Use of the doTest() syntax in compound tests

## [1.4.6]

### Changed
- Updated Intellij gradle plugin from 1.10 to 1.12
- Upgraded gradle version from 7.2 to 7.3

### Added
- Added reference to datasources in delegator definition
- Added OFBiz specific Groovy injections in xml files

## [1.4.5] The refactor update

### Added
- Added reference from xml service definition with group engine to service definition that are called.

### Changed
- Massive simplification of entity reference
- Massive simplification of service reference
- Some test refactor

### Fixed
- Added fixes for two NPE errors when getting files or script
- Fixed condition syntax and removed some non idiomatic try / catch
- Fixed reference from makeValue() method towards an entity definition in groovy

## [1.4.4]

### Changed
- Changed Groovy version from 3.0.9 to 3.0.13
- Changed Intellig gradle plugin 1.10.0 to 1.10.1
- Tests now run on 22.3 version
- Updated gradle from 7.1 to 7.2

### Fixed
- Fixed java target and source version
- Fixed broken plugin for 2022.3

## [1.4.3]

### Added
- Added 2022.3 support
- Update intellij gradle plugin to 1.10

## [1.4.2]

### Added
- Added reference from xml service definition to groovy methode in script file

### Changed
- Refactor of Java and groovy references

## [1.4.1]

### Added
- Added 3 missing patterns for refs inside a form field

## [1.4.0]

### Added
- Added references and ability to ctrl-click in compound files
- Added compound dom description
- Added tests for most references

### Changed
- Massive refactor of patterns
- Some general cleanup

## [1.3.1]

### Added
- Added some tests for xml references

### Fixed
- Fixed a small typo in CHANGELOG

## [1.3.0]

### Added
- Added beta version of hover documentation for following elements :
  - Services
  - Entities and views
  - Labels
- Added tests for most of previous items

## [1.2.2]

### Added
- Added support for Intellij 2022.2
- Upgraded intellij gradle plugin to 1.9

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
