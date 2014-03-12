import processing.serial.*;

Serial arduinoPort;
int potVal1 = 0;
int potVal2 = 0;
int[] list = new int[2];
float penX1 = 0;
float penX2 = 0;
float penY1 = 0;
float penY2 = 0;

void setup() {
  size(350, 500);
  println(Serial.list());
  background(255,200,0);
  // Configuring lines
  stroke(0);
  fill(175);
  strokeWeight(2);
  String portName = Serial.list()[12];
  arduinoPort = new Serial(this, portName, 9600);
  arduinoPort.bufferUntil('\n');
}

void draw() {
  line(penX1, penY1, penX2, penY2);
  penX1 = penX2;
  penY1 = penY2;   
}

void serialEvent(Serial myPort) {
  while (arduinoPort.available() > 0) {
    String myString = arduinoPort.readStringUntil('\n');
    if (myString != null) {
      println(myString);
      myString = trim(myString);
      list = int(split(myString, ","));
      // For sake of clarity
      if(list.length >=2){
        penX2 = map(list[1], 20, 100, 0, 255);
        penY2 = map(list[0], 20, 70, 0, 255);
      }
    }
  }
}
