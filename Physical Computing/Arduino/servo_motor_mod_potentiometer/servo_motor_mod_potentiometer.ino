#include <Servo.h>

int amount = 0;
int path = 1;

const int buttonPin = 4;
const int servoPin = 5;
const int servoPause = 5;

Servo toyServo;
int button = 0;

void setup(){
 toyServo.attach(servoPin); 
 pinMode(buttonPin, INPUT);
 Serial.begin(9600);
}

void loop(){
  if(digitalRead(buttonPin) == HIGH){
    amount += path;
  }
  if(amount == 179 || amount == 0){
    path = path * -1;
  }
  toyServo.write(amount);
  delay(20);
}
