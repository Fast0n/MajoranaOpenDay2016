//DICHIARAZIONE PIN
const int motoreorario=13;
const int motoreantiorario=11;
const int pulsantemarcia1=2;
const int pulsantearresto=4;
const int ledverde=5;
const int ledrosso=8;
unsigned long tempoVecchio = 0;  
unsigned long tempoAttuale;
int intervallo; 
int pausa=0;

//DICHIARAZIONE DELLE VARIABILI E DEL LORO VALORE
int statopulsante1=0;
int statopulsantearresto=0;
int statomotoreorario=LOW;
int statomotoreantiorario=LOW;
int statoledrosso=LOW;

//DICHIARAZIONE  INGRESSI/USCITE
void setup() {
pinMode(motoreorario, OUTPUT);
pinMode(motoreantiorario, OUTPUT);
pinMode(pulsantemarcia1, INPUT);
pinMode(pulsantearresto, INPUT);
pinMode(ledverde,OUTPUT);
pinMode(ledrosso,OUTPUT);
digitalWrite(ledverde,HIGH);
}

void ritardo(int dx) {
   tempoVecchio=millis();
   tempoAttuale=millis();
   while (tempoAttuale-tempoVecchio < dx && statopulsantearresto==LOW){
     tempoAttuale = millis();
     statopulsantearresto=digitalRead(pulsantearresto);
    }
  }


//ESECUZIONE DEL PROGRAMMA
void loop() {
  
  // PULSANTE MARCIA 1
   statopulsante1=digitalRead(pulsantemarcia1);
  if(statopulsante1==HIGH && statomotoreantiorario==LOW){
     statomotoreorario=HIGH;
     statoledrosso=HIGH;
     digitalWrite(ledrosso,HIGH);   
     digitalWrite(motoreorario,HIGH); 
     digitalWrite(ledverde,LOW); 
     ritardo(intervallo=10000);
     digitalWrite(motoreorario,LOW);
     digitalWrite(motoreantiorario,LOW);
     digitalWrite(ledrosso,LOW);
     digitalWrite(ledverde,HIGH);
     statoledrosso=LOW;
     statomotoreorario=LOW;
     statomotoreantiorario=LOW;
     pausa = 1;
     ritardo(intervallo=10000);
  }
  
 

//MARCIA ANTIORARIA in AUTOMATICO
    if(statomotoreorario==LOW && pausa == HIGH){    
      statomotoreantiorario=HIGH;
      statoledrosso=HIGH;
    digitalWrite(ledrosso,HIGH);
    digitalWrite(ledverde,LOW);
    digitalWrite(motoreantiorario,HIGH);
    
     ritardo(intervallo=10000);
     
     digitalWrite(motoreorario,LOW);
     digitalWrite(motoreantiorario,LOW);
     digitalWrite(ledrosso,LOW);
     digitalWrite(ledverde,HIGH);
     statoledrosso=LOW;
     statomotoreorario=LOW;
     statomotoreantiorario=LOW;
   pausa = 0;
     
}



//PULSANTE ARRESTO
 statopulsantearresto=digitalRead(pulsantearresto);
 if(statopulsantearresto==HIGH){ 
   digitalWrite(motoreorario,LOW);
   digitalWrite(motoreantiorario,LOW);
   digitalWrite(ledrosso,LOW);
   digitalWrite(ledverde,HIGH);
   statoledrosso=LOW;
   statomotoreorario=LOW;
   statomotoreantiorario=LOW;
 }
}

