# ARM Board Image Viewer

Created to allow viewing of images stored in memory simulated by the Keil Simulator, useful during second semester of first year in TCD's Computer Science course.

## IMPORTANT REMINDER

You're going to have to demonstrate on the actual hardware, so make sure you test your code on that too before then.

## Usage

The following instructions assume you're on windows, replace file paths with the relevant ones in WINE if not.

Move the displayImage.jar file (in bin/) to C:/dump/ (create the folder if you need to).

In Keil uVision, open the Command Window (while in Debug Mode, View -> Command Window). When you want to view the state of the image at a particular time, type "SAVE [filepath] [start address], [end address]". (the command "SAVE C:\Dump\dump.hex 0xA10046E0, 0xA1016200" should work fine, but if not make sure to check the addresses)

For example, to save a 1 pixel image to "C:\Dump\dump.hex", type "SAVE C:\dump\dump.hex 0xA1000000, 0xA1000004", then press enter.  You might need to create the folder before you can save to it.  Don't put spaces in the path, uVision doesn't like them.
Remember to account for each pixel being 4 bytes long when calculating the end address.

Open command prompt/terminal in that directory (in Windows, shift + right click, then select "Open Command Window Here"), then type "java -jar displayImage.jar dump.hex \<scale factor>", where scale factor is optional (default is 4) and scales up the image (the original is pretty small).
