import java.util.*;
import java.io.*;

// Size of the input grid for the network
int input_size = 16;
// Screen display size
int screen_size = 600;

// Width of the line drawn by the user
int drawRadius = 64;

float[][] input = new float[input_size][input_size];
int backgroundColor = 200;

// Initiation of Network

// A current list is a setting holding amount of neurons per layer.
// [hidden1, hidden2, ..., hiddenN, output] => Last layer is for output
int[] layer_params = {10, 10, 10};

ArrayList<float[][]> network = new ArrayList<float[][]>();

void setup() {
  background(backgroundColor);
  size(600,600);
  strokeWeight(drawRadius);
  println(sketchPath());
  for (int layer_i = 0; layer_i < layer_params.length; layer_i++) {
    
    // Initiation of layer [neuron, parameters]
    // Neuron (parameters) = [weight1, weight2, ..., weightN, bias, delta, activation]
    float[][] layer;
    int weights_count = 0;
    
    // Amount of weights for each neuron depend on amount of neurons in the last layer
    if (layer_i == 0) {
      weights_count = input_size * input_size;
    } else {
      weights_count = layer_params[layer_i - 1];
    }
    
    layer = new float[layer_params[layer_i]][weights_count + 3];
    
    // Assigns random values to each weight and bias
    for (int neuron_i = 0; neuron_i < layer.length; neuron_i++) {
      for (int param_i = 0; param_i < (layer[neuron_i].length - 2); param_i++) {
        layer[neuron_i][param_i] = (float)(1 - Math.random() * 2);
      }
     
    network.add(layer);
  }
}


// Maps number between 0 and 1
float sigmoid(float x) {
  return 1 / (1 + (float)Math.exp(-x));
}


// Derivative of sigmoid function
float sigmoid_dx(float x) {
  return x * (1 - x);
}


// Converts 3D array into 1D array
float[] flatten_array(float[][] array) {
  
  float[] flat = new float[array.length * array[0].length];
  
  for (int x = 0; x < array.length; x++) {
    for (int y = 0; y < array[x].length; y++) {
      flat[x * input_size + y] = array[x][y];
    }
  }
  
  return flat;
}


// Propagates forward through the network
// Feeds input and gets output of the network
void forward_propagate() {
  for (int layer_i = 0; layer_i < network.size(); layer_i++) {
    float[][] layer = network.get(layer_i);
    float[] activations;
    
    // Gets activations from previous layer
    // Or from input layer IF iterating through first network (non-input) layer
    if (layer_i == 0) {
      activations = flatten_array(input);
      
    // Direct activations from first last layer
    } else {
      
      float[][] last_layer = network.get(layer_i - 1);
      
      activations = new float[last_layer.length];
      
      for (int i = 0; i < last_layer.length; i++) {
        activations[i] = last_layer[i][last_layer[i].length - 1];
      }
    }
    
    // Updates activation of each neuron
    for (int neuron_i = 0; neuron_i < layer.length; neuron_i++) {
      float[] neuron = layer[neuron_i];
      float activation = 0.0;
      
      // Sums products of neuron weights and activation of last neuron connected to that weight
      for (int param_i = 0; param_i < (neuron.length - 3); param_i++) {
        activation += activations[param_i] * neuron[param_i];
      }
      
      // Adds bias
      activation += neuron[neuron.length - 3];
      activation = sigmoid(activation);
      
      neuron[neuron.length - 1] = activation;
      
      layer[neuron_i] = neuron;
      
    }
    
    // Saves updated layer into the network
    network.set(layer_i, layer);
    
  }
}


void back_propagate(float[] expected) {
  
  for (int layer_i = network.size() - 1; layer_i >= 0; layer_i--) {
    
    float[][] layer = network.get(layer_i);
    
    if (layer_i == network.size() - 1) {
      
      // Assign deltas for outside layer
      for (int neuron_i = 0; neuron_i < layer.length; neuron_i++) {
        
        float[] neuron = layer[neuron_i];
        float neuron_activation = neuron[neuron.length - 1];
        
        // Calculates how far off neuron activation is from expected value
        float delta = 0.0;
        delta = neuron_activation - expected[neuron_i];
        delta *= sigmoid_dx(neuron_activation);
        
        // Saves delta changes
        neuron[neuron.length - 2] = delta;
        
        // Saves neuron changes
        layer[neuron_i] = neuron;
        
      }
      
    } else {
      
      float[][] next_layer = network.get(layer_i + 1);
      
      for (int neuron_i = 0; neuron_i < layer.length; neuron_i++) {
        
        float[] neuron = layer[neuron_i];
        float neuron_activation = neuron[neuron.length - 1];
        
        float current_sigmoid_dx = sigmoid_dx(neuron_activation);
        float delta = 0.0;
        
        // Calculates how far off neuron's activation is from desired activation of the next_neuron
        for (int next_neuron_i = 0; next_neuron_i < next_layer.length; next_neuron_i++) {
          float[] next_neuron = next_layer[next_neuron_i];
          
          delta += next_neuron[neuron_i] * next_neuron[next_neuron.length - 2] * current_sigmoid_dx;
        }
        
        // Saves delta changes
        neuron[neuron.length - 2] = delta;
        
        // Saves neuron changes
        layer[neuron_i] = neuron;
      }
    }
    
    // Saves layer changes
    network.set(layer_i, layer);
  }
}


void update_params(float[] inputs, float learning_rate) {
  
   for (int layer_i = 0; layer_i < network.size(); layer_i++) {
     float[][] layer = network.get(layer_i);
     float[] current_inputs = inputs;
     
     
     // Gets activations (as inputs) from past layer neurons
     if (layer_i > 0) {
       float[][] past_layer = network.get(layer_i - 1);
       current_inputs = new float[past_layer.length];
       
       for (int neuron_i = 0; neuron_i < past_layer.length; neuron_i++) {
         float[] neuron = past_layer[neuron_i];
         
         current_inputs[neuron_i] = neuron[neuron.length - 1];
       }
     }
     
     
     for (int neuron_i = 0; neuron_i < layer.length; neuron_i++) {
       float[] neuron = layer[neuron_i];
       float delta = neuron[neuron.length - 2];
       
       // Updates weights
       for (int weight_i = 0; weight_i < current_inputs.length; weight_i++) {
         neuron[weight_i] -= learning_rate * delta * current_inputs[weight_i];
       }
       
       // Updates bias
       neuron[neuron.length - 3] -= learning_rate * neuron[neuron.length - 2];
       
       // Saves neuron changes
       layer[neuron_i] = neuron;
     }
     
     
     // Saves layer changes
     network.set(layer_i, layer);
   }
}


// Translates screen input to lower resolution ai vision input
int step_size = (int)(screen_size / input_size);
//
void getScreenInput() {
  
  for (int x = 0; x < input_size; x++) {
    for (int y = 0; y < input_size; y++) {
      input[x][y] = get(x * step_size, y * step_size) == -16777216 ? 1 : 0;
    }
  }
}


// Only gets the neuron activations from the output (last) layer
float[] get_outputs() {
  
  float[] outputs;
  
  // Ensures that network has the output layer
  if (network.size() == 0) {
    return new float[0];
  }
  
  // Gets last layer of the network (output layer)
  float[][] output_layer = network.get(network.size() - 1);
  outputs = new float[output_layer.length];
  
  // Converts activations of each neuron in output layer to activations array
  for (int neuron_i = 0; neuron_i < output_layer.length; neuron_i++) {
    float[] neuron = output_layer[neuron_i];
    
    outputs[neuron_i] = neuron[neuron.length - 1];
  }
  
  return outputs;
}


// Shows activations in network for each neuron
void displayOutputs() {
  float[] outputs = get_outputs();
  
  for (int i = 0; i < outputs.length; i++) {
    System.out.printf("%d = %.5f\n", i, outputs[i]);
  }
}


// Displays the input (for debugging)
void displayInput() {
  strokeWeight(1);
  
  for (int x = 0; x < input_size; x++) {
    for (int y = 0; y < input_size; y++) {
      stroke((1 - input[x][y]) * 256);
      point(x, y);
    }
  }
  
  strokeWeight(drawRadius);
  stroke(0);
}


long tick() {
  return System.currentTimeMillis();
}


long last_tick = 0L;

boolean learning_mode = false;

float[] empty_expected = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};

ArrayList<ArrayList<float[]>> dataset = new ArrayList<ArrayList<float[]>>();


// Calculate how much on average each neuron in output layer is off for the entire dataset
float get_errors() {
 
  float total_error = 0.0;
  
  for (ArrayList<float[]> data: dataset) {
    
    float[] data_input = data.get(0);
    float[] expected = data.get(1);
    
    // Converts 1D array to 2D array
    // Stored input => network input
    for (int i = 0; i < data_input.length; i++) {
      input[(int)(i / input_size)][i % input_size] = data_input[i];
    }
    
    forward_propagate();
    
    float[] outputs = get_outputs();
    
    for (int i = 0; i < expected.length; i++) {
      
      total_error += Math.abs(outputs[i] - expected[i]);
    }
  }
  
  return total_error / (dataset.size() * layer_params[layer_params.length - 1]);
}


// Concatinates array of floats to string
String concat_arr(String separator, float[] array) {
  
  String[] temp = new String[array.length];
  
  for (int i = 0; i < array.length; i++) {
    temp[i] = array[i] + "";
  }
  
  return String.join(separator, temp);
}


String save_file_name = "content";
String save_file_path = String.format("%s\\%s.txt", sketchPath(), save_file_name);
File save_file = new File(save_file_path);

FileWriter writer;
Scanner scan;


// Loads training data from file
void load_data() {
  try {
    if (!save_file.exists()) {
      return;
    }
    
    int count = 0;
    
    scan = new Scanner(save_file);
    while (scan.hasNextLine()) {
        String[] data_line = scan.nextLine().split(":");
        ArrayList<float[]> new_data = new ArrayList<float[]>();
        
        for (int i = 0; i < data_line.length; i++) {
          String[] data_part = data_line[i].split(",");
          float[] converted_data_part = new float[data_part.length];
          
          for (int j = 0; j < data_part.length; j++) {
            converted_data_part[j] = Float.parseFloat(data_part[j]);
          }
          
          new_data.add(converted_data_part);
        }
        
        dataset.add(new_data);
        count++;
      }
    
      println(count + " datasets were loaded");
  } catch (IOException e) {
    
  }
}


void save_data(boolean overwrite) {
  
  try {
    
    // Creates file if does not already exist
    
    save_file.createNewFile();
    print(save_file.getAbsolutePath() + "\n");
    scan = new Scanner(save_file);
    
    String saved_data = "";
    
    // Reads file before writting to it IF overwrite is disabled
    if (!overwrite) {
      while (scan.hasNextLine()) {
        saved_data += scan.nextLine() + "\r\n";
      }
    }
    
    // Encodes training dataset to String
    for (ArrayList<float[]> data: dataset) {
      saved_data += String.format("%s:%s\r\n", concat_arr(",", data.get(0)), concat_arr(",", data.get(1)));
    }
    
    writer = new FileWriter(save_file);
    writer.write(saved_data);
    writer.close();
    scan.close();
    
  } catch (IOException e) {
    
  }
}


void train_network(float learning_rate, int n_generations) {
  
  for (int gen = 0; gen < n_generations; gen++) {
    for (ArrayList<float[]> data: dataset) {
      
      float[] data_input = data.get(0);
      float[] expected = data.get(1);
      
      // Converts 1D array to 2D array
      // Stored input => network input
      for (int i = 0; i < data_input.length; i++) {
        input[(int)(i / input_size)][i % input_size] = data_input[i];
      }
      
      displayInput();
      
      
     // print("a");
      
      forward_propagate();
      back_propagate(expected);
      update_params(data_input, learning_rate);
      
    }
    
    System.out.printf("Generation: %d, Error: %.3f\n", gen, get_errors());
  }
}





void record_data(int expected) {
  
  getScreenInput();
  
  // Storage for single set in training dataset
  ArrayList<float[]> data_case = new ArrayList<float[]>();
  
  // Creates expected output array from number
  float[] new_expected = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
  new_expected[expected] = 1;
  
  // Inserts respective values into the set
  data_case.add(flatten_array(input)); // Inputs
  data_case.add(new_expected); // Expected outputs
  
  // Adds the set to the training dataset
  dataset.add(data_case);
  
  
}




void display_final_output() {
  
  int num = 0;
  float[] outputs = get_outputs();
  
  PFont displayFont = createFont("Arial", (int)(screen_size * 0.7), true);
  textFont(displayFont);
  fill(0);
  
  // Searches for output with the highest activation
  for (int i = 1; i < outputs.length; i++) {
    if (outputs[i] > outputs[num]) {
      num = i;
    }
  }
  
  println(num);
  text("" + num, screen_size * 0.25, screen_size * 0.75);
  
}


void draw() {
  
  // Lets user draw on the canvas
  if (mousePressed) {
    line(pmouseX, pmouseY, mouseX, mouseY);
  }
  
  if (keyPressed && (tick() - last_tick) >= 500) {
    last_tick = tick();
    
    // Lets user populate training dataset
    
    
    switch(key) {
      
      // Display input as seen by AI
      case 'd':
        getScreenInput();
        displayInput();
        break;
       
      // Switch learning mode
      case 'l':
        learning_mode = !learning_mode;
        System.out.printf("Learning Mode: %b\n", learning_mode);
        break;
        
      case 'r':
        if (network.size() > 0) {
          network.remove(network.size() - 1);
        }
        break;
        
      case 's':
        save_data(false);
        break;
        
      case 'a':
        load_data();
        break;
        
      // Erase screen drawing
      case 'e':
        background(backgroundColor);
        break;
        
      case 't':
        dataset = new ArrayList<ArrayList<float[]>>();
        println("Dataset have been removed from memory");
        break;
      
      
      case (char)10: // When ENTER key is pressed
      
        if (learning_mode) {
          train_network(0.1, 1000);
        } else {
          getScreenInput();
          forward_propagate();
          displayOutputs();
          background(backgroundColor);
          display_final_output();
        }
        
        
        
        break;
      default:
      
        if (learning_mode) {
          int num;
          
          // Verifies that key entered is a number
          try {
            num = Integer.parseInt(key + "");
            println("Received: " + num);
          } catch (Exception NumberFormatException) {
            println("Number expected, but character was entered. Try again.");
            break;
          }
          
          
          record_data(num);
          background(backgroundColor);
        }
    }
  }
}