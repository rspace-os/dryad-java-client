# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).


## [0.2.0] - 2022-08-03
- Fixed incorrect http method for file upload
- Added required JSON header to http entity
- Added URL encoding when appending strings to Dryad API endpoints
- Added a few logs for debugging
- Added `requestFactory.setBufferRequestBody(false)` to prevent OOM errors for large file uploads with RestTemplate

## [0.1.3] - 2022-07-21
- Added testConnection method to client to test if connection to dryad is working.
- Removed unused model classes `DryadResponse` and `DryadRequest`.

## [0.1.2] - 2022-07-13
- Added model classes to represent the data returned by the Dryad API.
- Added a `DryadClient` class to provide a simple interface to the Dryad API.