// These libraries are needed for the Splash Window
import java.awt.*;
import java.awt.event.*;
import javax.swing.*; 

/**
* This class is used to produce a simple splash screen
*
* @author John Urquhart Ferguson
* @version 0.01
*/
/*
* Last Modified: 12th March, 2006: 17:30 GMT
*
* Status: Has worked correctly for all test input.
*
* Notes: There is almost no error checking in this class.
* It is assumed that everything is used correctly and with
* sensible parameters. It's not thread safe at the moment.
*/


public class SplashWindow extends JWindow
{
	
	/**
	* Dispose of this Splash Window
	*/
	public void remove()
	{
		setVisible(false);
		dispose();
	}
	
	
	// ************ Constructors ************
	
	/**
	* Constructor if specified arguments are supplied.
	*
	* @param filename  The name of the image file to use for the Splash Window.
	* @param f  The Frame to draw Splash Window to.
	* @param waitTime  The amount of time to wait (in milliseconds) before automatically removing Splash Window.
	* @param loa  Set this to true to bypass the automated removal of Splash Window, false otherwise.
	*/
    public SplashWindow(String filename, Frame f, int waitTime, boolean loa)
    {
		// Call the constructor for JWindow
        super(f);
		
		// Set flag for bypassing auto removal
		final boolean loader;
		loader = loa;
		
		// Get current classloader to be used when loading image file. Doing it
		// this way allows the loading of the file to work from both Jar files and
		// directories.
		ClassLoader cl = this.getClass().getClassLoader();
		
		// Create JLabel to put in JWindow from passed filename
		JLabel l = new JLabel(new ImageIcon(cl.getResource(filename)));
		
        // Set up output window
        getContentPane().add(l, BorderLayout.CENTER);
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension labelSize = l.getPreferredSize();
        setLocation(screenSize.width/2 - (labelSize.width/2),
                    screenSize.height/2 - (labelSize.height/2));
		
		// Set up the default remove after the specified amount of
		// milliseconds
        final int pause = waitTime;
        final Runnable closerRunner = new Runnable()
		{
			public void run()
			{
				// Test for bypass flag
				if (!loader) {
					setVisible(false);
					dispose();
				}
			}
		};
		
		// Test for time to decide whether to remove splash screen
        Runnable waitRunner = new Runnable()
		{
			public void run()
			{
				try
				{
					Thread.sleep(pause);
					SwingUtilities.invokeAndWait(closerRunner);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					// can catch InvocationTargetException
					// can catch InterruptedException
				}
			}
		};
		
		// Display splash screen and start running timer for when to remove.
        setVisible(true);
        Thread splashThread = new Thread(waitRunner, "SplashThread");
        splashThread.start();
    }
	
	// ************ end Constructors ************
	
	
	/**
	* Just for testing purposes
	*
	* @param args  The command line arguements.
	*/
	/*public static void main(String args[])
	{
		// Make a test splash screen that uses the "splash.jpg" file and displays for 11000 milliseconds.
		// The loader boolean changes whether a timer removes the splash window automatically. If
		// true, timer does not remove splash window (but a user click still will).
		boolean loader = true;
		SplashWindow foo = new SplashWindow("splash.jpg", new Frame(), 11000, loader);
	}//end main()*/
	
}//end class Graph