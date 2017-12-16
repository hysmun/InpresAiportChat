#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <signal.h>
#include <iostream>
#include <fstream>

#include <sys/socket.h>
#include <sys/types.h>

#include <netinet/in.h>
#include <arpa/inet.h>
#include <netinet/tcp.h>

#include "SocketException.h"
#include "socketLib.h"
#include "libUtils.h"

using namespace std;

void HandlerSIGINT(int s);
void menuCIAChat();
int Connection();

char *msgSend;
char *msgRecv;
TypeRequete typeCli, typeSer;
int sizeCli, sizeSer;

int handleSocket;
	
int portServer;
string ip;
char sepTrame='#';
int conOK = 0;
string login;
string mdp;

int main(int argc, char *argv[])
{
	// Variables
	char choix;
	struct sockaddr_in *adresseSocket = (sockaddr_in *) malloc(sizeof(struct sockaddr_in));

	// Init et ouverture des sockets
	srand(time(NULL));
	
	//sig int handler
	struct sigaction sigAct;

  	sigAct.sa_handler = HandlerSIGINT;
  	sigAct.sa_flags = 0;
  	sigemptyset(&sigAct.sa_mask);
	sigaction(SIGINT,&sigAct,NULL); 
	
	try
	{
		//fichier config
		fstream fichierconf;
		try
		{
			string strbuf;
			fichierconf.open("client.conf",fstream::in);
			fichierconf.ignore(1000, '=');
			fichierconf >> ip;
			fichierconf.ignore(1000, '=');
			fichierconf >> portServer;
			fichierconf.ignore(1000, '=');
			fichierconf >> sepTrame;
		}
		catch(...)
		{
			//
		}
		cout << "ip : "<<ip<<" port : "<<portServer<<" sepTrame : "<<sepTrame<<endl;
		
		cout << "client socket init"<<endl;
		handleSocket = ClientInit(portServer, ip, adresseSocket);
		
		cout << "client connect"<<endl;
		ClientConnect(handleSocket, adresseSocket);
		
		cout <<"client connecter !!!!!!"<<endl;	
		
		while(1)
		{
			cout <<"1-connection"<<endl;
			cout <<"autre fin programme"<<endl;
			cin >> choix;
			switch(choix)
			{
				case '1':
					//
					conOK = Connection();
					if(conOK)
					{
						cout <<"connection OK"<<endl;
						menuCIAChat();
					}
					break;
				default:
					CloseSocket(handleSocket);
					handleSocket=0;
					exit(0);			
			}
		}
	}
	catch(SocketException &e)
	{
		cout <<"Erreur socket : " << e.getMsg() << " n° : " << e.getNbrErr()<<endl;
		perror(" ");
	}
	catch(...)
	{
		cout << "Erreur inconnue "<< endl;
		perror(" t");
	}
	
	
	// Fermeture socket
	if(handleSocket!=0)
		CloseSocket(handleSocket);
	handleSocket=0;
	if(adresseSocket!=NULL)
		free(adresseSocket);
	return 1;
}

void menuCIAChat()
{
	char choix='i';
	while(choix != 'q')
	{
		//affichage menu
		cout<<"menu :"<<endl;
		
		//choix
		cin >> choix;
		
		//execution 
		
	}
	return;
}

int Connection()
{
	//
	cout<<endl<<"login :";
	cin >> login;
	cout<<endl<<"mot de passe :";
	cin >> mdp;
	
	//
	char *msgToSend;
	
	
	return 1;
}


void HandlerSIGINT(int s)
{
	if(handleSocket != 0)
	{
		close(handleSocket);
		handleSocket=0;
	}
	exit(0);
	
}

