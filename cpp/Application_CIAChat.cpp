#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <signal.h>
#include <iostream>
#include <fstream>
#include <string.h>

#include <sys/socket.h>
#include <sys/types.h>

#include <netinet/in.h>
#include <arpa/inet.h>
#include <netinet/tcp.h>

#include "SocketException.h"
#include "socketLib.h"
#include "libUtils.h"

#define TRUE	1
#define FALSE	0

#define BUFLEN 255

using namespace std;

void HandlerSIGINT(int s);
void menuCIAChat();
int Connection();

pthread_t tidRecv;
void *thReceiv(void*);

char *msgSend;
char *msgRecv;
TypeRequete typeCli, typeSer;
int sizeCli, sizeSer;

int handleSocket;
int udpSocket;
	
int portServer;
string ip;
char sepTrame='#';
int conOK = 0;
int thReceivState=FALSE;
string login;
string mdp;
struct in_addr localInterface;
struct sockaddr_in *adresseSocket;
struct sockaddr_in *adresseUDP;
struct sockaddr_in *adresseUDP2;

int main(int argc, char *argv[])
{
	// Variables
	char choix;
	adresseSocket = (struct sockaddr_in *)malloc(sizeof(struct sockaddr_in));
	adresseUDP = (struct sockaddr_in *)malloc(sizeof(struct sockaddr_in));

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
		
		pthread_create(&tidRecv, NULL, thReceiv, NULL);
		
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
	thReceivState = TRUE;
	char msg[256];
	char buf[256];
	int ret;
	memset(buf,0, 256);
	adresseUDP2 = (struct sockaddr_in *)malloc(sizeof( struct sockaddr_in));
	memcpy(adresseUDP2, adresseUDP, sizeof(struct sockaddr_in));
	adresseUDP2->sin_addr.s_addr = inet_addr("227.0.0.10");
	adresseUDP2->sin_port = htons(50001);
	while(choix != 'q')
	{
		//affichage menu
		cout<<"menu :"<<endl;
		cout << "m pour envoyer un message"<<endl;
		cout <<" l pour une question "<<endl;
		cout << "q pour quitter "<<endl;
		
		//choix
		cin >> choix;
		cout << "choix :"<<choix<<endl;
		//execution 
		switch(choix)
		{
			case 'm':
				cin >> msg;
				sprintf(buf, "%d#%s", POST_EVENT, msg );
			   ret = sendto(udpSocket, buf, 256, 0,(struct sockaddr*)adresseUDP2, sizeof(struct sockaddr));
			   perror("erreur send to ");
				break;
			case 'l':
				cin >> msg;
				sprintf(buf, "%d#%s", POST_QUESTION, msg );
				ret = sendto(udpSocket, buf, 256, 0, (struct sockaddr*)adresseUDP2, sizeof(struct sockaddr));
				break;
			case 'q':
				break;
			
		}
		cout <<ret<< "msg envoyer "<<buf<<endl;
		memset(buf,0, 256);
	}
	return;
}

int Connection()
{
	//
	int taille = 0;
	int type = LOGIN_UNIX;
	int tmp=0;
	
	cout<<endl<<"login :";
	cin >> login;
	cout<<endl<<"mot de passe :";
	cin >> mdp;
	
	//
	char msgToSend[2048];
	char *buf;
	sprintf(msgToSend,"%s|%s",login.c_str(),mdp.c_str());
	taille = strlen(msgToSend);
	cout<<"sizeof int :"<<sizeof(int)<<endl;
	write(handleSocket, &tmp, 1);
	write(handleSocket, &tmp, 1);
	write(handleSocket, &tmp, 1);
	write(handleSocket, &taille, 1);
	write(handleSocket, &tmp, 1);
	write(handleSocket, &tmp, 1);
	write(handleSocket, &tmp, 1);
	write(handleSocket, &type, 1);
	write(handleSocket, &msgToSend, strlen(msgToSend));
	cout <<strlen(msgToSend)<<"tcp send:"<<msgToSend<<endl;
	
	
	read(handleSocket, &taille, 1);
	cout <<"taille : " << taille << endl;
	read(handleSocket, &type, 1);
	cout <<"type : " << type << endl;
	buf = receiveSize(handleSocket,taille);
	
	cout << "message recu du login : "<<buf<<endl;
	
	udpSocket = ClientInitUDP(50001, "227.0.0.10",adresseUDP);
	
	return 1;
}

void *thReceiv(void*)
{
	//
	char buf[256];
	int slen = sizeof(struct sockaddr_in);
	char *msg;
	char *type;
	memset(buf,0, 256);
	while(1)
	{
		//cout << "debut th"<<endl;
		while(thReceivState == TRUE)
		{
			if (read(udpSocket, buf, BUFLEN ) == -1)
			{
				cout <<endl<< "erreur receive !!"<<endl<<endl;
			}
			else
			{
				type = strtok(buf,"#");
				cout<<endl;
				if(atoi(type) == POST_QUESTION)
					cout<<"Question";
				if(atoi(type) == ANSWER_QUESTION)
					cout<<"Answer";
				msg = (char*) strtok(NULL,"#");
				cout<<"Message recu : "<<msg<<endl;
				memset(buf,0, 256);
			}
		}
		//cout << "waitTh"<<endl;
		waitTime(1, 0);
	}
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



























