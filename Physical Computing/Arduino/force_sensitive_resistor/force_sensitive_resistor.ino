const int ledPin = 5;
const int firstPin = A0;

void setup(){
   Serial.begin(9600);
   pinMode(ledPin, OUTPUT); 
}

void loop(){
  int firstVal = analogRead(firstPin);
  int ledVal = map(firstVal, 0, 1023, 0, 255);
  Serial.println((String) firstVal + " -> " + (String) ledVal);
  analogWrite(ledPin, ledVal);
}
