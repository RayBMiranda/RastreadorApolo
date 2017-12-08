/*
  Desenvolvido Utilizando a Biblioteca Apolo
*/

#include <UIPEthernet.h>
#include "Apolo.h"

int pinGirarNascente = 23;
int pinGirarPoente = 22;
int pinPotenciometro = A8;
int pinLDRPoente = A9;
int pinLDRNascente = A10;
int limiteDiferenca = 10;
int valorFimCursoNascente = 420; //MenorValor
int valorFimCursoPoente = 870; //MaiorValor

int  pinSensorCorrenteP1 = A0;
int pinSensorTensaoP1 = A1;

unsigned long antesEnvio = 0;
const long intervalo = 1000L * 10L;

byte mac[] = { 0x54, 0x34, 0x41, 0x30, 0x30, 0x31 };                                       
EthernetClient client;
char server[] = "192.168.0.101"; 

Apolo apolo(valorFimCursoNascente, valorFimCursoPoente, limiteDiferenca, pinGirarNascente, pinGirarPoente, pinPotenciometro, pinLDRPoente, pinLDRNascente);

void setup() {
  Serial.begin(9600);
  Serial.println("Inicializando...");
  pinMode(pinGirarNascente, OUTPUT);
  pinMode(pinGirarPoente, OUTPUT);
  digitalWrite(pinGirarNascente, LOW);
  digitalWrite(pinGirarPoente, LOW);
  Ethernet.begin(mac);
  Serial.print(F("IP : "));
  Serial.println(Ethernet.localIP());
  Serial.print(F("Mascara de Sub Rede : "));
  Serial.println(Ethernet.subnetMask());
  Serial.print(F("Gateway Padrão IP: "));
  Serial.println(Ethernet.gatewayIP());
  Serial.print(F("DNS IP : "));
  Serial.println(Ethernet.dnsServerIP());
}

void loop() 
{ 
 long timeEnvio = millis();  
 apolo.run(millis()); 

  unsigned long atual = millis();
Serial.print("tempo");
Serial.println(atual - antesEnvio);
 if(atual - antesEnvio >= intervalo)
 {
  antesEnvio = atual;
  enviar();
 }  
}
void enviar(){
  if (client.connect(server, 44420)) {
   enviarInformacao();
  }
   else 
  {
   Serial.println("--> falha na conexao/n");
   enviar();
  }  
}
void enviarInformacao()
{
  Serial.println("-> Conectado");
  client.print( "GET /WebApp/uploaddata.apolo?");
  client.print("key=");
  client.print(key);
  client.print("&");
  client.print("corrente=");
  float corrente = calcularCorrente(pinSensorCorrenteP1);
  client.print(corrente);
  client.print("&");
  client.print("tensao=");
  float tensao = calcularTensao(pinSensorTensaoP1);
  client.print(tensao);
  client.print("&");
  client.print("posicao=");
  int posicao = analogRead(pinPotenciometro);
  client.print(posicao);
  client.println( " HTTP/1.1");
  client.print( "Host: " );
  client.println(server);
  client.println("User-Agent: arduino-ethernet");
  client.println( "Connection: close" );
  client.println();
  client.println();
  client.stop();  
}
//--------------------------------------------------------------------------
float calcularCorrente(int pinSensorCorrente)
{
  int sensorValue_aux = 0;
  float sensorValue = 0;
  float currentValue = 0;
  float voltsporUnidade = 0.004887586;// 5%1023
  for(int i=100; i>0; i--){
    sensorValue_aux = (analogRead(pinSensorCorrente) -511); // le o sensor na pino analogico A0 e ajusta o valor lido ja que a saída do sensor é (1023)vcc/2 para corrente =0
    sensorValue += pow(sensorValue_aux,2); // somam os quadrados das leituras.
  }
  sensorValue = (sqrt(sensorValue/ 100)) * voltsporUnidade; // finaliza o calculo da méida quadratica e ajusta o valor lido para volts
  currentValue = (sensorValue/0.185); // calcula a corrente considerando a sensibilidade do sernsor (185 mV por amper)
  sensorValue =0;
  return (currentValue);
}
//--------------------------------------------------------------------------
float calcularTensao(int pinSensorTensao)
{
  int leitura = analogRead(pinSensorTensao);
  float volts =((leitura*0.00489)*5);  // Faz a primeira leitura e aguarda 10s
  return (volts); 
}
//--------------------------------------------------------------------------

