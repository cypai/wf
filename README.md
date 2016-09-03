# WF: The Wishing Project

Lead Developer: Cheng-Yu Pai

Other Contributors: Jack Guan

Graphics: Cheng-Yu Pai, Ting-Hsuan Pai

This game was intended to be a JRPG with an XCOM-like battle system, centered around the ability to wish for objects (ala Nethack).

Since this project is overly ambitious, I am stopping serious development for it. It's not playable in its current form (if you want something remotely playable use release version 0.0.1). If there is something preventing it from running in the future, I might fix it though.

## Screenshots

<img src="https://raw.githubusercontent.com/cypai/wf/master/images/2016-01-27-tree.png">

## Requirements for Development

- Eclipse
- Buildship or Gradle IDE (Eclipse plugin)
- eclipse-cs (checkstyle)
- sevntu-checkstyle eclipse-cs plugin

## Running

Requirements

- JDK 1.8
- Gradle

You can launch it from the terminal/command line with 'gradle desktop:run'.

## Setup

First, import the project as a Gradle Project in Eclipse.

Set up a Gradle build in the Run Configurations for the wf-desktop project. The only task you need to run the game is 'run'. You will need to set the working directory to wf-core/assets as well. Use this configuration to run the game. Alternatively, you can launch it from command line with 'gradle desktop:run'.

Testing is easy - just run the Junit tests from within Eclipse. You can also run them from command line with 'gradle clean test'.

Unfortunately due to custom gradle scripting, you cannot debug with just a debug config. You'll need to create a separate run config with the JVM arguments '-Ddebug=true', then creating a new Remote Java Application Debug Configuration with port 9009 as the debug socket attachment port.
To run it in debug mode, first run 'gradle desktop:run -Ddebug=true' and then run the debug socket attach config in Eclipse. Breakpoints and other things will then be available.

If you intend to do development on this project, make sure you import wf_formatter.xml as the formatter in Eclipse. Also, import the wf_checkstyle.xml for checkstyle configuration. There may still be a few checkstyle warnings that need to be fixed, which might happen if I come back to this project.

You can probably open it in IntelliJ IDEA or other IDEs as well, but I guarantee nothing.

## License

This software is licensed under the terms in the file named "LICENSE" in this directory.

The tree models are under a different license in the file named "LICENSE-GRAPHICS" in this directory.
