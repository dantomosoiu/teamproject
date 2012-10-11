This folder contains a runnable version of the Purges program. It should be noted that you cannot run Purges from a folder without write access. For instance, it cannot be run directly from a CD. This is because Purges writes temporary files during execution. You will probably need to copy the executable folder to your hard drive and then run it from there.

To run purges, you must make sure your computer is fully set up with the required programs. A setup folder should appear elsewhere on your Purges CD. There is also an Environments folder in the same folder as this file. The Environments folder contains examples of Purges environment files. These are necessary for Purges to run simulations on. When you use the open option in Purges, you should select one of the files from the Environments folder. These files have a 'pur' extension.

There are four windows 'bat' files supplied for convenience. They comprise all the command line variations that Purges can be run with.


RunPurges.bat:
--------------
Run the default version of Purges with no command line options.


RunPurgesMouseRotate.bat:
-----------------------
Run Purges with mouse dragging support. This allows the user to have more control over the display of the 3D environment by allowing a click and drag style interface in addition to the rotation slider.


RunPurgesWireFrame.bat:
-----------------------
Run Purges with wireframe environments instead of shaded environments. This may make Purges run faster on slower computer systems.


RunPurgesMouseRotateAndWireFrame.bat:
-------------------------------------
Run Purges with the mouse rotate and wireframe options.


If you are running Purges in a non-windows environment, you can still run it from the command line with the following command:

java -jar Purges.jar <options>

Where options can be any number of the following (including none):

-mouseRotate     : Allows for the mouse rotate as in the windows 'bat' file.

-wireFrame       : Allows for the wireframe option as in the windows 'bat' file.