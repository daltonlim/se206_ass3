 NameSayer
*********
This is a java applet which helps to record audio, playback, and practise names in general. 
Multiple reocrdings of a single name can be quickly and easily viewed, and a small prompt ensures that the user knows when a bad Recording is present.

Target User:
********************
Students

How to run:
***************
Please run the following command in the shell 
'''
java -jar NameSayer.jar
'''
If no ‘Database’ folder is present, then it will automatically be generated on the first run.
PLEASE place all database files here

Author:
*******
Group 38
Dalton Lim 
Brian Zhang

How to use NameSayer:
*********************
(1). There are four options to choose from within the welcome window: practise, achievements, help and quit .

(2). Users can either  choose a name from the Available Name List or use the search bar to add a name to practice.

(3). Search bar also provides the abitlity to concatenate multiple names together.

(4). In the player window, users can play the audio for every name added. Original audio is named as date and shown in the first line as defalut. User recordings have a pre-fix "User".

(5). ON/OFF buttons  on this windiw control volume. When users turn this on, all audio files will be played as 10dB.
When it is turned off, all audio files will be played at their original volume.

(6). Recordings reported as bad quality may be found in the ‘Logs’ folder under badRecordings.txt. Any exported lists may also be found here.

(7). The recordings for concatenated names will be generated on the fly by the program. There is no need to choose a database recording in this instance.

(8). User have the ability to delete any recording that is created by user.

(9). A Recording window is provoded for users who wish to record their own version and compare with an original version. 

(10) Clicking on the red name in the recording window, plays the original database version for the given name. 

(11). Users may hold the Record button to record voice and realse to stop the recording. The maximum time of the recording is 10 seconds.

(12). Users may test the microphone level by clicking the microphone icon to revaeal a level meter. Clicking it again hides the level meter.

User recordings:
*********************
User made recordings may be found in the userDatabase folder

Achievements:
*********************
User can get different titles by reaching multiple goals.
These may be tracked on the achievements page.
