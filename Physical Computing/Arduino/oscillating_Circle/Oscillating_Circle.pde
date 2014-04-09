int diameter;
float x, y;
float speedX, speedY;

void setup(){
  size(640, 480);
  diameter = 200;
  
  x = width/2;
  y = height/2;
  
  speedX = 0;
  speedY = 2;
}

void draw(){
  background(255, 255, 0);
  fill(255, 0, 0);
  ellipse(x, y, diameter, diameter);
  x += speedX;
  y += speedY;
  if(y + (diameter/2) >= height || y - (diameter/2) <= 0){
     speedY = - speedY; 
  }
}
