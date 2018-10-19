NameSayer
*********
This is a java applet which helps to record audio, playback, and practise names in general. 
Multiple reocrdings of a single name can be quickly and easily viewed, and a small prompt ensures that the user knows when a bad Recording is present.

Target User:
********************
Students

How to run:
***************
java -jar NameSayer.jar
If no ‘audioFiles’ folder is present, then it will automatically be generated on the first run.
PLEASE place all database files here

Author:
*******
Group 38
Dalton Lim 
Brian Zhang

How to use NameSayer:
*********************
(1). There are three options in welcome window: practise, achievements and quit 


(2). Users can choose names to playlist from NameList or using search bar to add name 

(3). Search bar also provides the function to combine multiple names

(4). In player window, users can play the audio for each name added. Original audio is named as date and shown in the first line as defalut. User recording has pre-fix "User".

(5). ON/OFF buttons control volume. When users turns on, all audio files will be played as 10dB.
When its off, all audio files will be played as original volume.

(6). If any recording are reported as bad quality, they may be found in the ‘logs’ folder under badRecordings.txt. Any exported lists may also be found here.

(7). For COMBINED name, it will be played by automatically, so there is no file selection for it.

(8). User can delete any file that is created by user.

(9). It provides a Record window for users who want to record their own version and compare with an original version. 

(10). When click the name shown in recording window, user can hear the original version for the name. 

(11). User can hold on the Record button to record voice and realse to stop recording. The maximum time of the recording is 10 seconds.

(12). User can test microphone by clicking the microphone icon and hide it by clicking again.

User recordings:
*********************
User made recordings may be found in the userFiles folder

Achievement :
*********************
User can get different titles by reaching multiple goals.
