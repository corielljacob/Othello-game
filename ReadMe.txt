----------------------------------- 
   ___  _   _          _ _         |
  / _ \| |_| |__   ___| | | ___    |
 | | | | __| '_ \ / _ \ | |/ _ \   |
 | |_| | |_| | | |  __/ | | (_) |  |
  \___/ \__|_| |_|\___|_|_|\___/   |
                                   |
------------------------------------
by Jacob Coriell. Created for CS-2003-Fundamentals of Algorithms in 2017. Revised in 2019. 

Welcome to my Othello game. You may also know it as reversi. You can find rules to the game online in various 
places such as https://www.ultraboardgames.com/othello/game-rules.php. 

My game is meant to be played in the console. It was built on a Windows machine using Eclipse. To run the game, you can open the project in an IDE or 
navigate to the src package in your console, compile with "javac othelloGamePackage\OthelloRunner.java" and run with "java othelloGamePackage.OthelloRunner" 
Project last run with java version "10.0.1" 2018-04-17 & Java(TM) SE Runtime Environment 18.3 (build 10.0.1+10)

My game has three modes of operation. The modes are:
1) Monte-Carlo Simulation Mode
2) One-Player Mode
3) Two-Player Mode

Upon running my game, you will be prompted for the mode you'd like to play one at a time. Please answer with y for yes and n for no.

I will now describe the function of each mode.

1) Monte-Carlo Simulation Mode
This mode aims to analyze the potential advantage of starting first or second. If you choose to run this mode, it will prompt you for 
the number of simulations you'd like to run. A simulation consists of two AI players who simply pick available moves at random until
one of the players wins. When the match concludes, a spread value is calculated. The spread value is the difference between the first
player's score (black pieces) and the second player's score (white pieces). If this spread value is positive, then the first player won the game. 
If the value is negative, then the second player won the game, and, finally, if the spread is 0, then the game was a tie. This spread is logged in a 
hash-map so that we can keep track of the number of occurrences a spread value has in the simulation. When all simulations are completed
the program outputs each spread-occurrence pair from the map where the keys are separated from the values with a tab character so that this
data can easily be copied and pasted into excel. With this data, you can easily create a bar graph and calculate the average spread. 

I have included a sample excel sheet that has the data from a simulation containing 30,000 games along with the calculation of the average and a
bar graph of the occurrence vs spread results. The graph of the spreads is about what you'd expect and the average of -0.761 concludes that if there 
is a very slight advantage, it would go to the second player (white pieces). 

Note: You will likely experience some wait-time upon running the simulation. This wait-time may vary depending on your machine.

2) One-Player Mode
This mode allows one player to play my game against a computer opponent. The game prompts the user to enter the row and column they'd like 
to place a piece and then calculates the necessary flips to be made and resulting score changes. After the player completes a turn, a computer player 
picks a move to make at random.This continues until the game is over. 

3) Two-Player Mode
This mode allows two players to play by prompting each player where they'd like to place pieces until the game finishes.  
