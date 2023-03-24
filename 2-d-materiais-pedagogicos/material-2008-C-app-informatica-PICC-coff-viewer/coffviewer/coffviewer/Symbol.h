#ifndef SYMBOL_H
#define SYMBOL_H

#include "CoffStructs.h"

class Coff;

class Symbol {
	SymbolEntry entry;	// Entry readed
	int number;			// Table index
	char *name;			// Dinamic allocated name
public:
	Symbol(int num, Coff *c); // TODO: read entry 
	~Symbol();				  // TODO
	int numberOfAuxs()	{ return entry.nAuxSymbols; }
	char *getName()		{ return name; }
	int getNumber()		{ return number; }
  SymbolEntry * getEntry(){return &entry;}
  void print();
};

class SymIterator {
public:
	virtual Symbol * next() = 0;
	virtual ~SymIterator() {}
};

#endif
