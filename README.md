# C Vulnerability Analysis using Joern

#### Still in very very early development :D

## Getting Started:

* Edit main.sh path variables
* Run main
* Outputfiles are named accordingly to their input file names

## Features (coming soon sometime:D)

* It does track the flows from function arguments to potential risky sinks (e.g. strcpy,...)
* When candidates are found it checks whether checks are applied (correctly)
  * Therefore it checks e.g. for sizeof 
  * And also checks whether this is acutally used
  * Additionally considers control flow (strcpy(buf,s) -> if (sizeof(s) > x) would still resolve in a vulnerable flow
  

###Example:

#### Input:

```
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
```

#### Output:

[+] Checking for candidates:

<table>
  <tr>
    <th>tracked</th>
    <th>lineNumber</th>
    <th>method</th>
    <th>file</th>
  </tr>
  <tr>
    <td>vulnerable(char* s)</td>
    <td>18</td>
    <td>vulnerable</td>
    <td>...JoernAnalysis\vulnerable.c</td>
  </tr>
  <tr>
    <td>strcpy(buf, s) </td>
    <td>22</td>
    <td>vulnerable</td>
    <td>...JoernAnalysis\vulnerable.c</td>
  </tr>
</table>

[+] Checking for constraints...

<table>
  <tr>
    <th>tracked</th>
    <th>lineNumber</th>
    <th>method</th>
    <th>file</th>
  </tr>
  <tr>
    <td>vulnerable(char* s)</td>
    <td>18</td>
    <td>vulnerable</td>
    <td>...JoernAnalysis\vulnerable.c</td>
  </tr>
  <tr>
    <td>strcpy(buf, s) </td>
    <td>22</td>
    <td>vulnerable</td>
    <td>...JoernAnalysis\vulnerable.c</td>
  </tr>
    <tr><td></td></tr>
    <tr>
    <td>vulnerable(char* s)</td>
    <td>18</td>
    <td>vulnerable</td>
    <td>...JoernAnalysis\vulnerable.c</td>
  </tr>
  <tr>
    <td>sizeof(s)  </td>
    <td>20</td>
    <td>vulnerable</td>
    <td>...JoernAnalysis\vulnerable.c</td>
  </tr>
    <tr><td></td></tr>
     <tr>
    <td>vulnerable(char* s)</td>
    <td>18</td>
    <td>vulnerable</td>
    <td>...JoernAnalysis\vulnerable.c</td>
  </tr>
  <tr>
    <td>sizeof(s)  </td>
    <td>20</td>
    <td>vulnerable</td>
    <td>...JoernAnalysis\vulnerable.c</td>
  </tr>
  <tr>
    <td>size = sizeof(s) </td>
    <td>20</td>
    <td>vulnerable</td>
    <td>...JoernAnalysis\vulnerable.c</td>
  </tr>
</table>



[+] Vulnerable flow found!

18: char* s<br/>
20: size = sizeof(s)<br/>
20: sizeof(s)<br/>
22: strcpy(buf, s)<br/>

