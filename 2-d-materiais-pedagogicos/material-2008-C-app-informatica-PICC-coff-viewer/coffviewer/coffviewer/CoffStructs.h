#define _CRT_SECURE_NO_WARNINGS

/* Dependente da arquitectura da maquina */
typedef unsigned char byte;
typedef unsigned short int word16;
typedef unsigned long int word32;

struct CoffHeader {
	word16 machine;
	word16 nSections;
	word32 time;
	word32 posSymbols;
	word32 nSymbols;
	word16 szOptHeader;  
	word16 flags;
};

struct SectionHeader {
	char name[8];
	word32 virtualSz;
	word32 virtualAddr;
	word32 szData;
	word32 posData;
	word32 posRelocations;
	word32 posLineNumbers;
	word16 nRelocations;
	word16 nLineNumbers;
	word32 flags;
};

union SymbolName {
	char shortName[8];
	struct { word32 zeros; word32 pos; } longName;
};

struct SymbolEntry {
	SymbolName name;
	word32 value;
	word16 section;
	word16 type;
	byte storage;
	byte nAuxSymbols;
};
#define EXTERNAL_STORAGE 2
#define STATIC_STORAGE 3

struct RelocationEntry {
	word32 virtualAddr;
	word32 symbolIndex;
	word16 type;
};
