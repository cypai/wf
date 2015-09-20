# WF: The Wishing Project

Lead Developer: Pipai

Supporting Developers: Jack Guan

## Requirements

- JDK 1.8
- Eclipse
- Gradle IDE (Eclipse plugin)
- eclipse-cs (checkstyle)
- sevntu-checkstyle eclipse-cs plugin

## Setup

First, import the project as a Gradle Project in Eclipse. Then, set up the run configuration for DesktopLauncher.java by setting the working directory (in tab Arguments) to "${workspace_loc:wf-core}".

To run the game just use that run configuration for DesktopLauncher.java.

Make sure you import wf_formatter.xml as the formatter in Eclipse. Also, import the wf_checkstyle.xml for checkstyle configuration.

There may still be a few checkstyle warnings that need to be fixed - those will be fixed in a future issue.
