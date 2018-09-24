# se206_ass3
This is a java applet which helps to record audio, playback, and practise names in general. 
Multiple reocrdings of a single name can be quickly and easily viewed, and a small prompt ensures that the user knows when a bad Recording is present.

![](demo.gif)

Please run java -jar se206_ass3.jar on the command line to start the app.

To add a name onto the selected list, please click on it in the available names list.

All recoding have a date prefixed with user and are saved to the same audio files folder for easy access.

If no ‘audioFiles’ folder is present, then it will automatically be generated on the first run.

Please place all database files here.

If any recording are reported as bad quality, they may be found in the ‘logs’ folder under badRecordings.txt

There are three main windows with one Microphone Test.

Users can choose the names they want to practise, then select one of version to play.

It provides a Record window for users who want to record their own version and compare with an original version.
