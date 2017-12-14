#ifndef __LIBUTILS_H__
#define __LIBUTILS_H__

/*
public static int LOGIN_GROUP       =   1;
 public static int POST_QUESTION     =   2;
 public static int ANSWER_QUESTION   =   3;
 public static int POST_EVENT        =   4;
public static int LOGIN_NOK = 301;
*/
typedef enum TypeRequete 
{
   LOGIN_GROUP =1,
	POST_QUESTION,
	ANSWER_QUESTION,
	POST_EVENT,
	LOGIN_NOK=301,
}TypeRequete;

int waitTime(int sec, long nsec);

int checkSep(char *pbuf, int psize, char *psep);

int CheckLoginPassword(char* login,char* password);

int sendMsgRequest(int phandle, TypeRequete pt, char *pmsg, int psize);

char *receiveMsgRequest(int handle, TypeRequete *pt, int *psize);

int sendMsgRequest(int phandle, TypeRequete pt, char *pmsg, int psize, char finTrame);

char *receiveMsgRequest(int handle, TypeRequete *pt, int *psize, char finTrame);

int random(int min, int max);















#endif
