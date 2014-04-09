const int potPin1 = A0;
const int potPin2 = A1;

void setup(){
  Serial.begin(9600);
}

void loop(){
  int potVal1 = analogRead(potPin1); // read potentiometer value
  potVal1 = map(potVal1, 0, 1023, 127, 0);
  delay(20);
  int potVal2 = analogRead(potPin2); // read potentiometer value
  potVal2 = map(potVal2, 0, 1023, 127, 0);
  // Send the input
  Serial.print(potVal1); //output the value via serial
  Serial.print(",");
  Serial.print(potVal2); //output the value via serial
  Serial.print("\n");
}
