/*Definisce i pin a cui sono collegati un led, una ventola e il reset  automatico*/
#define ledPin 13
#define fanPin 8
#define resetPin 2
#include <LiquidCrystal_I2C.h>
#include <Wire.h>
LiquidCrystal_I2C lcd(0x27,16,2); 

void setup() {
  /*Definisce il tipo di pin*/
  pinMode(ledPin, OUTPUT);
  pinMode(fanPin, OUTPUT);
  pinMode(resetPin, OUTPUT);
  /*Definisce lo stato iniziale dei pin (serve anche per impedire il reset infinito)*/
  digitalWrite (ledPin, LOW);
  digitalWrite (fanPin, LOW);
  digitalWrite (resetPin, HIGH);
  /*Apre la comunicazione seriale*/
 delay(1000); 
  Serial.begin(9600);
  lcd.init();
  lcd.backlight();

}

void loop() {
  /*Definsice una variabile "time" che assumerà il valore dei millisecondi da quando il sitema è acceso*/
  unsigned long time = millis();
  /*Definisce una variabile "c" che assumerà il valore del carattere inviato tramite terminale*/
  char c = Serial.read();
  /*Se il carattere inviato è "L" verrà acceso il led*/
  if (c == 'L') {
    digitalWrite (ledPin, HIGH);
    lcd.setCursor(0,0);
    lcd.print("LED Acceso");
  }
  /*Se il carattere inviato è "l" verrà spendo il led*/
  if (c == 'l') {
    digitalWrite (ledPin, LOW);
    lcd.setCursor(0,0);
    lcd.print("LED Spento");
  }
  /*Se il carattere inviato è "V" verrà accesa la ventola*/
  if (c == 'V') {
    digitalWrite (fanPin, HIGH);
    lcd.setCursor(0,1);
    lcd.print("Ventola Accesa");
  }
  /*Se il carattere inviato è "v" verrà spenta la ventola*/
  if (c == 'v') {
    digitalWrite (fanPin, LOW);
    lcd.setCursor(0,1);
    lcd.print("Ventola Spenta");
  }
  /*Se sono trascorsi più di 2 minuti (120000 millisecondi) da quando il sistema è acceso effettuerà il reset. (Serve per impedire che il modulo BT sia sempre connesso ad uno stesso telefono).*/
  if (time >= 120000) {
    digitalWrite (resetPin, LOW);
  }
}
