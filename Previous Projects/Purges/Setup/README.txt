This file explains how to set up Purges in a Microsoft Windows XP computer system. It does not cover other systems, but the general requirements are to set up a Java Runtime Environment from Sun, make sure it is included in your path and then install the Java 3D plugin from Sun as well. The detailed instructions for Microsoft Windows XP are given below:

The first thing you must do to prepare your computer for running Purges is to install the Java Runtime Environment from Sun. In the same folder as this file, there is a program called: jre-1_5_0_06-windows-i586-p.exe

This is the installer for the Java Runtime Environment. Double click it and follow all the instructions. Generally, accepting the default options is the best way to go. If the program says you already have this version of the JRE installed, then you can move on to setting up the Java3D plug-in. Also, if you already have version 1.5.0 or higher of the Java Runtime Environment, then you do not need to install this one. Just continue and install the Java3D plug-in.

Once the Java Runtime Environment is installed, you will need to install the Java3D plug-in. In the same folder as this file, there is a program called: java3d-1_4_0-windows-i586-1.exe

This is the installer for the Java3D plug-in. Double click it and follow all the instructions. Generally, accepting all the default options is the best way to go.

The last thing that must be done is to make sure that Java is in the Windows Path. To do this, right-click `My Computer' and select `Properties'. Click the `Advanced' tab and then the `Environment Variables' button. In the `System Variables' section there should be a variable called `Path'. Select this by clikcing once with the left mouse button, then click the `Edit' button. This will open a new window. In the section that says, `Variable value:', add the following text to the beginning of the value:

C:\Program Files\Java\jre1.5.0_06\bin;

This is assuming that you installed the Java Runtime Environment to C:\Program Files\Java\jre1.5.0_06. If this is not the case, you will need to replace this value with the appropriate one. Once you have changed the value, click the `Okay' button. Then click the `Okay' button on the Environment Variables window. Then click the `Okay' button on the System Properties window.

Your system should now be fully setup to run Purges. Purges cannot be run directly from the CD. It is best of you create a folder on your hard drive called `Purges', then copy the contents of the `Executable' folder on the Purges CD into this folder. You can then run Purges from the new folder. For more information on running Purges, see the README.txt file in the `Executable' folder on the Purges CD.