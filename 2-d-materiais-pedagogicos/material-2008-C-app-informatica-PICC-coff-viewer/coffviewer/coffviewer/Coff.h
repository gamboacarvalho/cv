#ifndef COFF_H
#define COFF_H

#include "Symbol.h"
#include <stdio.h>

class Coff {
	Symbol **symTable;	// Dinamic allocated symbol table
	FILE *file;
	CoffHeader header;	// header readed from file
  class ForwardSymIter:public SymIterator{
    private:
      word32 idx;
      Coff * coff;
    public:
      ForwardSymIter(Coff * c);
      Symbol * next();
  };
public:
	Coff(const char *filename); // TODO: open file and read header
	~Coff();					// TODO: close file
	bool error()				{ return file==NULL; }
	Symbol * getSymbol(int num)	{ return symTable[num]; }
	int numOfSections()			{ return header.nSections; }
	void loadSymbols();	
  SymIterator * SymIterator();
  // ...  Add more methods
  void loadSymbol(SymbolEntry * entry, int idx);
  void loadSection(SectionHeader * header, int idx);
  char * loadString(word32 offset);
	
  class DefSymIterator:public SymIterator{
    private:
      word32 idx, section;
      Coff * coff;
      char * name;
    public:
      DefSymIterator(Coff * c, word32 section, char * name);
      Symbol * next();
  };
  class RefSymIterator:public SymIterator{
    private:
      word32 offsetRelocations;
      word16 nrOfReloc;
      Coff * coff;
    public:
      RefSymIterator(Coff * c, word32 offsetRelocations, word16 nr);
      Symbol * next();
  };
};


#endif