# processing_ai
Number recognition AI (based on neural network) done in Processing IDE with Java.
Users can teach computer to recognize number.

# Installation Guide
## Download Processing IDE
Processing IDE is a program that provides ability to code 2D graphics. Only available tools that this IDE provides is drawing circles, rectangles, triangles, and individual pixels of any size and color. From there, any graphics can be coded, even 3D if one really wants.
You can download IDE on official website: https://processing.org/download

## Running the code
After downloading, open Recog.pde file with Processing IDE or open the IDE and paste the code from Recognition.java file.

# Use Guide
At the beginning, code can not recognize numbers. User has to first teach the program.
To do so:
1. Press 'L' button to enter "Learning Mode"
2. Draw a number and press corresponding number on the keyboard. If you need to erase screen, press 'E'.
3. Repeat multiple numbers for all numbers. More data you provide, more accurate program will be.
4. Still in "Learning Mode" press 'Enter' let computer learn from your data.
4. After its done learning, exit "Learning Mode" by pressing 'L' again.
5. Erase screen by pressing 'E' if necessary.
6. Draw a number and press 'Enter' for program to show you what it recognizes the number to be.

## Controls
- E : Erase screen
- L : Enter or exit "Learning Mode"
- 'Enter' : In "Learning Mode" it will use data provided to learn. In Non-"Learning Mode" it will try to recognize the number currently on drawn on the screen.
- '0-9' : Tells computer which number you drew on the screen when in "Learning Mode"
