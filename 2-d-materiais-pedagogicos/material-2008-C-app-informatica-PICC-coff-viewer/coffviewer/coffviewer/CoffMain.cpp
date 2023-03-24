#include <stdlib.h>
#include "Section.h"
#include "Coff.h"

void error(const char *msg) {
	printf("Error: %s.\n",msg);
	printf("Use: coff <filename>\n");
	exit(1);
}

void printSyms(char *title, SymIterator *iter ) {
	Symbol * s;
  if(iter != NULL){
    printf("%s\n",title);
	  if ( !(s=iter->next()) ) return;
    do { s->print(); }
	  while( (s=iter->next()) != NULL );
	  delete iter;
  }
}

void main(int argc, char * argv[]) {
	// if (argc!=2) error("Invalid arguments");
	// Coff cf( argv[1] );

  puts(argv[0]);
  Coff cf("coff.obj");
	if (cf.error()) 
    error("Cant open file");
	cf.loadSymbols();
	/* Print symbol table */
	printSyms( "SYMBOL TABLE", cf.SymIterator() );

	/* Print sections information */
  
	for(int secNum=1 ; secNum <= cf.numOfSections() ; ++secNum) {
		Section sec(secNum,&cf);
		printf("\nSECTION #%-2d %-8s size=%-3d align=%2d flags=%s\n", secNum,
		  sec.getName(),sec.getSize(),sec.getAlign(), sec.getTxtFlags());
		printSyms( " Defined Symbols", sec.DefSymIterator());
		printSyms( " Referenced Symbols", sec.RefSymIterator());
	}
  
}