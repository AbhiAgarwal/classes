#include <Servo.h>

const int servoPin = 5;
const int servoPause = 5;

Servo toyServo;

void setup(){
 toyServo.attach(servoPin); 
}

void loop(){
 for(int i = 0; i <= 180; i++){
  toyServo.write(i);
  delay(servoPause);
 } 
 for(int i = 180; i >= 0; i--){
  toyServo.write(i);
  delay(servoPause); 
 }
}
