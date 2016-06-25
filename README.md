# SpiderAppDevTaskTwo

##Synopsis
This app comes with a bunch of pictures and soundtracks. The user can select modes to view the slideshow of images and can choose between the soundtracks to be played during slideshow.

##Tests
The user can choose the track to be played in the dropdown menu in the first activity. Then the user can play the soundtrack using the play/pause button. To view the images, the user needs to press the slideshow button, which will start the slideshow along with the music.

There are two buttons at the bottom of the screen in the first activity and each of them toggles the other. The enable button enables the proximity sensor on the phone. As the proximity sensor only gives information about the distance or proximity of the user from the phone, only a binary input can be received - whether the user has come close to the sensor or not. 

By **default, the proximity sensor is enabled** and when the slideshow button is clicked, the user can view the pictures by giving input to the proximity sensor. This will make the images go around in one order, one after the other. 

In order to make the images go **the other way around**, the user has to **touch the screen once** and then continue to give input to the proximity sensor. This is repeatable and this feature makes it for easier viewing.

Only when the *disable button* is clicked, and the toast message pops up saying that the proximity sensor is disabled, is the *automatic animation mode turned on*. This is pretty obvious: the images go about in one order, one after the other. There is no way to control it this time. And a *timer appears at the bottom*, showing the duration of the slideshow. Once all the pictures have been displayed, the app returns to the first activity. But the music keeps playing because abruptly stopping the music just feels bad.
