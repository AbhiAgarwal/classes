const int speakerPin = 5;
const int photoPin = A0;

void setup(){
  Serial.begin(9600);
  pinMode(speakerPin, OUTPUT);
}

void loop(){
   int photoVal = analogRead(photoPin);
   int speakerVal = photoVal * 5;
   Serial.println((String)photoVal + "-> " + (String)speakerVal);
   tone(speakerPin, speakerVal);  
}
