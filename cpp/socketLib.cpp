#include <stdlib.h>
#include <stdio.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <iostream>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <string.h>
#include <netinet/tcp.h>
#include <unistd.h>
#include "SocketException.h"
#include "socketLib.h"
#include "libUtils.h"

using namespace std;


int ServerInit(int pport)
{
	//
	struct hostent *infosHost;
	struct in_addr adresseIP;
	struct sockaddr_in adresseSocket;
	int handleSocket;
	
	//creation socket
	if((handleSocket = socket(AF_INET,SOCK_STREAM,IPPROTO_TCP))==-1)
	{
		// throw
		throw SocketException(SocketException::ERRORINIT);
	}
	
	//acquisition info ordi
	if((infosHost = gethostbyname("localhost"))==0)
	{
		//throw
		throw SocketException(SocketException::ERRORINIT);
	}
	memcpy(&adresseIP, infosHost->h_addr, infosHost->h_length);
	
	
	
	//prepa struct sockaddr_in
	memset(&adresseSocket, 0, sizeof(struct sockaddr_in));
	adresseSocket.sin_family = AF_INET;
	adresseSocket.sin_port = htons(pport);
	memcpy(&adresseSocket.sin_addr, infosHost->h_addr, infosHost->h_length);
	
	cout << inet_ntoa(adresseIP)<<":"<<pport<<endl;
	
	//bind
	if(bind(handleSocket, (struct sockaddr *)&adresseSocket, sizeof(struct sockaddr_in))== -1)
	{
		//throw
		throw SocketException(SocketException::ERRORINIT);
	}
	return handleSocket;
}

int ServerListen(int phandle)
{
	//
	if(listen(phandle, MAX_CONNECTION) == -1)
	{
		//throw
		throw SocketException(SocketException::ERRORLISTEN);
	}
	return 1;
}

int ServerAccept(int phandle, struct sockaddr_in *paddrsock)
{
	//
	int handleService;
	unsigned int taille = sizeof(struct sockaddr_in);
	if((handleService = accept(phandle, (struct sockaddr *)paddrsock, &taille))==-1)
	{
		//throw
		throw SocketException(SocketException::ERRORACCEPT);
	}
	return handleService;
}

int ClientConnect(int phandle, struct sockaddr_in *paddrsock)
{
	//
	unsigned int taille = sizeof(struct sockaddr_in);
	if(connect(phandle, (struct sockaddr *)paddrsock, taille)==-1)
	{
		//throw
		perror("test");
		throw SocketException(SocketException::ERRORCONNECT);
	}
	return 1;
}

int ClientInit(int pport, struct sockaddr_in *adresseSocket)
{
	//
	struct hostent *infosHost;
	struct in_addr adresseIP;
	//struct sockaddr_in adresseSocket;
	int handleSocket;
	
	//creation socket
	if((handleSocket = socket(AF_INET,SOCK_STREAM,IPPROTO_TCP))==-1)
	{
		// throw
		throw SocketException(SocketException::ERRORINIT);
	}
	
	//acquisition info ordi
	if((infosHost = gethostbyname("localhost"))==0)
	{
		//throw
		throw SocketException(SocketException::ERRORINIT);
	}
	memcpy(&adresseIP, infosHost->h_addr, infosHost->h_length);
	
	
	
	//prepa struct sockaddr_in
	memset(adresseSocket, 0, sizeof(struct sockaddr_in));
	adresseSocket->sin_family = AF_INET;
	adresseSocket->sin_port = htons(pport);
	memcpy(&(adresseSocket->sin_addr), infosHost->h_addr, infosHost->h_length);
	
	cout << "clien: "<<inet_ntoa(adresseIP)<<":"<<pport<<endl;
	
	return handleSocket;
}

int ClientInit(int pport, string ip, struct sockaddr_in *adresseSocket)
{
	//
	struct hostent *infosHost;
	struct in_addr adresseIP;
	//struct sockaddr_in adresseSocket;
	int handleSocket;
	
	//creation socket
	if((handleSocket = socket(AF_INET,SOCK_STREAM,IPPROTO_TCP))==-1)
	{
		// throw
		throw SocketException(SocketException::ERRORINIT);
	}
	
	//acquisition info ordi
	if((infosHost = gethostbyname(ip.c_str()))==0)
	{
		//throw
		throw SocketException(SocketException::ERRORINIT);
	}
	memcpy(&adresseIP, infosHost->h_addr, infosHost->h_length);
	
	
	
	//prepa struct sockaddr_in
	memset(adresseSocket, 0, sizeof(struct sockaddr_in));
	adresseSocket->sin_family = AF_INET;
	adresseSocket->sin_port = htons(pport);
	memcpy(&(adresseSocket->sin_addr), infosHost->h_addr, infosHost->h_length);
	
	cout << "clien: "<<inet_ntoa(adresseIP)<<":"<<pport<<endl;
	
	return handleSocket;
}

int ClientInitUDP(int pport, string ip, struct sockaddr_in *adresseSocket)
{
	//
	struct hostent *infosHost;
	struct in_addr adresseIP;
	struct ip_mreq group;
	unsigned char ttl =3;
	unsigned char one =1;
	//struct sockaddr_in adresseSocket;
	int handleSocket;
	
	//creation socket
	if((handleSocket = socket(AF_INET,SOCK_DGRAM,0))==-1)
	{
		// throw
		throw SocketException("erreur socket",SocketException::ERRORINIT);
	}
	
	int yes = 1;
	if (setsockopt(handleSocket,SOL_SOCKET,SO_REUSEADDR,&yes,sizeof(yes)) < 0) {
    perror("Reusing ADDR failed");
    exit(1);
    }
	
	
	
	//prepa struct sockaddr_in
	memset(adresseSocket, 0, sizeof(struct sockaddr_in));
	memset(&adresseIP, 0, sizeof(struct in_addr));
	adresseSocket->sin_family = AF_INET;
	adresseSocket->sin_port = htons(pport);
	adresseSocket->sin_addr.s_addr = htonl(INADDR_ANY);
	 if(bind(handleSocket, (struct sockaddr*)adresseSocket, sizeof(*adresseSocket)) < 0)
	{
		perror("bind");
		throw SocketException("erreur bind",SocketException::ERRORINIT);
	}
	adresseIP.s_addr = INADDR_ANY;
	
	setsockopt(handleSocket, IPPROTO_IP, IP_MULTICAST_IF, &adresseIP, sizeof(struct in_addr));
	setsockopt(handleSocket, IPPROTO_IP, IP_MULTICAST_TTL, &ttl,sizeof(unsigned char));
	setsockopt(handleSocket, IPPROTO_IP, IP_MULTICAST_LOOP, &one, sizeof(unsigned char));
	 group.imr_multiaddr.s_addr = inet_addr(ip.c_str());

	group.imr_interface.s_addr = htonl(INADDR_ANY);

	if(setsockopt(handleSocket, IPPROTO_IP, IP_ADD_MEMBERSHIP, (char *)&group, sizeof(group)) < 0)
	{
		perror("Adding multicast group error");
		exit(1);
	}
	
	cout << "clien: "<<inet_ntoa(adresseIP)<<":"<<pport<<endl;
	
	return handleSocket;
}

int SendMsg(int phandle )
{
	//
	return 1;
}

int sendSize(int phandle, char *pbuf, int psize)
{

	//
	int tmpLec=0;

	tmpLec = send(phandle, pbuf, psize, 0);
	if(tmpLec == -1)
		throw SocketException("Error send unknow");
	if(tmpLec != psize)
		throw SocketException("Error send size too short");

	//memcpy(pbuf, buf, sizeof(psize));
	//free(buf);
	return tmpLec;
}

char *receiveSize(int phandle, int psize)
{
	//
	int tmpLec=0;
	int i;
	char *buf = (char *)malloc(psize*sizeof(char));
	for(i=0; i<psize; i += tmpLec)
	{
		tmpLec = recv(phandle, &(buf[i]), psize-i, 0);
		if(tmpLec == -1)
			throw SocketException("Error receive size too short");
	}
	//buf[psize*sizeof(char)]='\0';
	if(i != psize)
		throw SocketException("Error receive size too short");
	//memcpy(pbuf, buf, sizeof(psize));
	//free(buf);
	return buf;
}

char *receiveSep(int phandle, char *psep, int *psize)
{
	//
	int tmpLec=0;
	int i, isep=0;

	char tmpBuf[5500] = {'\0'};
	bool fini = false;
	int tailleS, tailleO;
	tailleO = sizeof(int);
	int tailleMsg=0;
	
	getsockopt(phandle, IPPROTO_TCP, TCP_MAXSEG, &tailleS, (socklen_t *)&tailleO);
	
	char *buf = (char *)malloc(tailleS*sizeof(char));
	for(i=0; fini == false; i++)
	{
		memset(tmpBuf, '\0', 5500);
		tmpLec = recv(phandle, &(tmpBuf[0]), tailleS, 0);
		if(tmpLec == -1)
			throw SocketException("Erreur receive sep");
		if((isep = checkSep(&(tmpBuf[0]), tmpLec, psep)) >= 0)
		{
			fini = true;
		}
		else
		{
			buf = (char *) realloc(buf, tailleS * (i+2) * sizeof(char));
		}
		
		memcpy(&(buf[tailleMsg]), &tmpBuf, tmpLec);
		tailleMsg += tmpLec;
	}
	*psize = tailleMsg;
	return buf;
}

int CloseSocket(int pi)
{
	//
	return close(pi);
}





























