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

First, import the project as a Gradle Project in Eclipse.

Set up a Gradle build within External Tools Configuration for the wf-desktop project. The only task you need to run the game is 'run'. Use this configuration to run the game. Alternatively, you can launch it from command line with 'gradle desktop:run'.

Testing is easy - just run the Junit tests from within Eclipse. You can also run them from command line with 'gradle clean test'.

Make sure you import wf_formatter.xml as the formatter in Eclipse. Also, import the wf_checkstyle.xml for checkstyle configuration.

There may still be a few checkstyle warnings that need to be fixed - those will be fixed in a future issue.
