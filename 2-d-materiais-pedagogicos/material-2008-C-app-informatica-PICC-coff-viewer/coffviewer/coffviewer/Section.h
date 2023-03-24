#include "Symbol.h"

class Section {
	int number;				// Section number
	Coff *coff;				// Coff to use
	SectionHeader header;	// readed header from file
	char *name;				// Dinamic allocated name
public:
	Section(int n, Coff *c);
	~Section();
	int getSize()			{ return header.szData; }
	char *getName()			{ return name; }
	int getAlign();
	char * getTxtFlags();	
	SymIterator * DefSymIterator();
	SymIterator * RefSymIterator();
	// ... ADD more methods
};
