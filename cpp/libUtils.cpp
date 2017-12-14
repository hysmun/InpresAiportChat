#include <stdlib.h>
#include <stdio.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <iostream>
#include <fstream>
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



int waitTime(int sec, long nsec)
{
	if(sec < 0 || nsec < 0 || nsec > 999999999)
	{
		cout << "Erreur nanosleep" << endl;
		return -1; 
	}
	struct timespec time;
	time.tv_sec = sec;
	time.tv_nsec = nsec;
	return nanosleep(&time, NULL);
}


int checkSep(char *pbuf, int psize, char *psep)
{
	int i,j;
	for(i=0; i <psize; i++)
	{
		for(j=0; j<(int)strlen(psep); j++)
		{
			if(pbuf[i] == psep[j])
				return i;
		}
	}
	return -1;
}

int sendMsgRequest(int phandle, TypeRequete pt, char *pmsg, int psize, char finTrame)
{
	int taille = psize+sizeof(int)+sizeof(char);
	char *pbuf = (char*)malloc(taille);
	int tmp = pt;
	//cout << "send : "<<pmsg<<endl;
	
	
	memcpy(pbuf, &tmp, sizeof(int));
	memcpy((pbuf+sizeof(int)), pmsg, psize);
	//cout << "send : "<< pbuf<<endl;
	
	pbuf[taille-1]=finTrame;
	//cout << "send : "<< pbuf<<endl;
	
	int ret = sendSize(phandle, pbuf, taille);
	
	free(pbuf);
	return ret;
}

char *receiveMsgRequest(int handle, TypeRequete *pt, int *psize, char finTrame)
{
	char *pbuf;
	int size;
	char sep[2];
	sprintf(sep, "%c", finTrame);
	pbuf = receiveSep(handle, sep, &size);
	//cout << "recv : "<<pbuf<<endl;
	*psize = size - sizeof(int);
	memcpy(pt,pbuf, sizeof(int));
	
	char *tbuf = (char*)malloc(*psize);
	memcpy(tbuf, pbuf+sizeof(int), *psize);
	tbuf[(*psize)-1]='\0';
	return tbuf;
}

int sendMsgRequest(int phandle, TypeRequete pt, char *pmsg, int psize)
{
	int taille = psize+sizeof(int)+sizeof(char);
	char *pbuf = (char*)malloc(taille);
	int tmp = pt;
	//cout << "send : "<<pmsg<<endl;
	
	
	memcpy(pbuf, &tmp, sizeof(int));
	memcpy((pbuf+sizeof(int)), pmsg, psize);
	//cout << "send : "<< pbuf<<endl;
	
	
	
	pbuf[taille-1]='$';
	//cout << "send : "<< pbuf<<endl;
	
	int ret = sendSize(phandle, pbuf, taille);
	
	free(pbuf);
	return ret;
}

char *receiveMsgRequest(int handle, TypeRequete *pt, int *psize)
{
	char *pbuf;
	int size;
	char sep[2]="$";
	pbuf = receiveSep(handle, sep, &size);
	//cout << "recv : "<<pbuf<<endl;
	*psize = size - sizeof(int);
	memcpy(pt,pbuf, sizeof(int));
	
	char *tbuf = (char*)malloc(*psize);
	memcpy(tbuf, pbuf+sizeof(int), *psize);
	tbuf[(*psize)-1]='\0';
	return tbuf;
}

int random(int min, int max)
{
	return (rand()%(max-min))+min;
}





















