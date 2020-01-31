#include <stdio.h>
#include <string.h>
#include <stdlib.h>

void not_vulnerable(char* s) {
	char buf[1024];
	int size = sizeof(s);

	if (size > 1024) {
		exit(1);
	}
	
	strcpy(buf, s);
	
}


void vulnerable(char *s) {
	char buf[1024];
	int size = sizeof(s);

	strcpy(buf, s);
}

int main(void){
	vulnerable("hoho");
	not_vulnerable("hihi");
	
	
	return 1;
}