const int ledPin = 5;
const int tempSensorPin = A0;

void setup(){
  Serial.begin(9600);
  pinMode(ledPin, OUTPUT); 
}

void loop(){
  int tempSensorVal = analogRead(tempSensorPin);
  int ledVal = map(tempSensorVal, 0, 1023, 0, 255);
  Serial.println((String)tempSensorVal + " -> " + (String) ledVal);
  analogWrite(ledPin, ledVal);
}
