.SILENT:

GCC=g++
CFLAGS=-Wall -lpthread
OBJ=Application_CIAChat.o SocketException.o socketLib.o libUtils.o
APP=Application_CIAChat
TMP = commentaire de base

#------- 		all
all:
	clear
	clear
	echo all
	make Application_CIAChat


$(APP):$(OBJ)
	echo compilation $@
	$(GCC) -o $@ $(OBJ) $(CFLAGS)


%.o: %.cpp
	echo compilation $@
	$(GCC) -c -o $@ $< $(CFLAGS)


#--------		clean
clean:
	clear
	clear
	echo clean all *.o $(APP) $(OBJ)
	rm -rf  $(APP) $(OBJ) *.o 


#------			Git
git:
	clear
	clear
	echo git
	git add -A
	git commit -m "$(TMP)"
	git push
