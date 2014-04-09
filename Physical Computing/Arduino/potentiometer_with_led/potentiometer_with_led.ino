const int ledPin = 5;
const int potPin = A0;

void setup(){
  Serial.begin(9600);
  pinMode(ledPin, OUTPUT);
}

void loop(){
  int potVal = analogRead(potPin);
  int ledVal = potVal * 0.25;
  Serial.println((String)potVal + " -> " + (String)ledVal);
  analogWrite(ledPin, ledVal);
}
