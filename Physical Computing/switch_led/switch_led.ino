const int ledPin = 2;
const int switchPin = 3;

void setup(){
  pinMode(ledPin, OUTPUT);
  pinMode(switchPin, INPUT);
}
void loop(){
    digitalWrite(ledPin, HIGH);
  int switchVal = digitalRead(switchPin);
  if(switchVal == HIGH){
    digitalWrite(ledPin, HIGH);
  }
  else {
     digitalWrite(ledPin, LOW); 
  }
  delay(1);
}
