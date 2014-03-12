#include <Servo.h>

const int ledPin = 2;
const int servoPin = 5;
const int servoPause = 45;
const int fstPin = A0;

Servo toy;

void setup(){
   Serial.begin(9600);
   pinMode(ledPin, OUTPUT);
   toy.attach(servoPin);
}

void loop(){
   int fstVal = analogRead(fstPin);
   int ledVal = map(fstVal, 0, 1, 0, 255);
   digitalWrite(ledPin, HIGH);
   toy.write(100);
   Serial.println((String) fstVal);
   delay(servoPause);
}
