# GuessTheNumberGame
An university project for Mobile Applications created in Kotlin.

In this project I use Shared Preferences and SQLite DB.

To get the result board the application connects to an URL which returns best 10 results in JSON format.
Users are stored in SQLite DB. 
If some user doesn't exist, a new one is created. Entered data such as username and password is inserted into table.
When user exists, the password is checked.

After logging an welcome toast is showed and we can start to "play" the game.
The app draws a number from 0 to 20. Our task is to guess within 10 times what the drawn number is. If we don't we lose.

## Screenshoots

| | | | | |
|:---:|:---:|:---:|:---:|:---:|
![](https://github.com/Naylet/GuessTheNumberGame/blob/master/screenshoots/1.jpg) | ![](https://github.com/Naylet/GuessTheNumberGame/blob/master/screenshoots/2.jpg) | ![](https://github.com/Naylet/GuessTheNumberGame/blob/master/screenshoots/3.jpg) | ![](https://github.com/Naylet/GuessTheNumberGame/blob/master/screenshoots/4.jpg)| ![](https://github.com/Naylet/GuessTheNumberGame/blob/master/screenshoots/5.jpg)|
|:---:|:---:|:---:|:---:|:---:|
