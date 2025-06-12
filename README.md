# Handwritten Number Recognizer – Perceptron Neural Network 

Number recognition AI (based on a neural network) built in the Processing IDE with Java.  
Users can teach the computer to recognize numbers.  
Watch the video demo: https://youtu.be/uyvJBrU3ftQ

![Number Recognition AI DEMO animation](ai-number-recog-720p-demo.gif)

---

## Installation Guide

### Download Processing IDE
Processing IDE lets you code 2D graphics. It only provides basic tools like drawing circles, rectangles, triangles, and pixels in any size and color. From these, you can create any graphic, including 3D if desired.

Download the IDE here: https://processing.org/download

### Running the Code
After installing, open `recog-ai.pde` with Processing IDE, or open the IDE and paste the code from `recog-ai.java`.

---

## Usage Guide
At first, the program can’t recognize numbers-you need to teach it.

To teach the program:
1. Press **L** to enter _Learning Mode_.
2. Draw a number, then press the corresponding number key on the keyboard.
3. To erase the screen, press **E**.
4. Repeat this for many samples of all numbers you want to teach. More data means better accuracy.
5. While still in _Learning Mode_, press **Enter** to let the computer learn from your data.
6. Once learning is complete, press **L** again to exit _Learning Mode_.
7. Erase the screen if needed with **E**.
8. Draw a number and press **Enter**—the program will try to recognize it.

---

## Controls
- **E** : Erase screen
- **L** : Enter or exit _Learning Mode_
- **Enter** :
  - In _Learning Mode_: Train the model with provided data
  - Outside _Learning Mode_: Recognize the currently drawn number
- **0-9** : Specify which number you drew when in _Learning Mode_
