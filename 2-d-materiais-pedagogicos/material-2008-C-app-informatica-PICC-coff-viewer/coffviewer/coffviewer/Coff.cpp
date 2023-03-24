#include "Coff.h"
#include <stdlib.h>
#include <string.h>

//********************************************************
//-------------------- CONSTRUCTOR -----------------------
//*******************************************************
Coff::Coff(const char *filename){
  if((file = fopen(filename,"rb")) == NULL) {
    fprintf(stderr,"Erro ao abrir %s\n", filename);
  }
  fread(&header, sizeof(CoffHeader), 1, file);
  
}
//********************************************************
//-------------------- DESTRUCTOR ------------------------
//********************************************************
Coff::~Coff(){  
  fclose(file);
  delete symTable;
}
//********************************************************
//-------------------- loadSymbols -----------------------
//********************************************************
void Coff::loadSymbols(){
  if(header.posSymbols != 0){
    symTable = new Symbol*[this->header.nSymbols];
    for(word16 i = 0; i<header.nSymbols; i++){
      symTable[i] = new Symbol(i, this);
    }
  }
}
void Coff::loadSymbol(SymbolEntry * entry, int idx){
  int offset = header.posSymbols + idx * sizeof(SymbolEntry);
  if(fseek(file, offset, SEEK_SET)) 
    return;
  fread(entry, sizeof(SymbolEntry), 1, file);
}
//********************************************************
//-------------------- loadSection ----------------------
//********************************************************
void Coff::loadSection(SectionHeader * header, int idx){
  word32 offset = sizeof(CoffHeader) + this->header.szOptHeader + idx * sizeof(SectionHeader);
  if(fseek(file, offset, SEEK_SET)) 
    return;
  fread(header, sizeof(SectionHeader), 1, file);
}
//********************************************************
//--------------------- loadString -----------------------
//********************************************************
char * Coff::loadString(word32 offset){
  static int stringTable = header.posSymbols + header.nSymbols * sizeof(SymbolEntry);
  if(fseek(file, stringTable + offset, SEEK_SET)) 
    return NULL;
  char buffer[100];
  fread(buffer, sizeof(buffer), 1, file);
  char * str = new char[strlen(buffer)+1];
  strcpy(str, buffer);
  return str;
}
//********************************************************
//-------------------- SymIterator -----------------------
//********************************************************
SymIterator * Coff::SymIterator(){
  return new Coff::ForwardSymIter(this);
}
//********************************************************
//---------- Nested Class Coff::ForwardSymIter -----------
//********************************************************
Coff::ForwardSymIter::ForwardSymIter(Coff * c){
  this->coff = c;
  this->idx = 0;
}
Symbol * Coff::ForwardSymIter::next(){
  while(idx < coff->header.nSymbols && coff->symTable[idx]->getEntry()->storage == 0) 
    idx++;
  if(idx < coff->header.nSymbols) 
    return coff->symTable[idx++];
  else
    return NULL;
}

//********************************************************
//------ Nested Class Section::DefSymIterator ------------
//********************************************************
Coff::DefSymIterator::DefSymIterator(Coff * c, word32 s, char * n){
  this->coff = c;
  this->section = s;
  this->name = n;
  this->idx = 0;
}
Symbol * Coff::DefSymIterator::next(){
  while(idx < coff->header.nSymbols 
      && (coff->symTable[idx]->getEntry()->section != this->section
      || strcmp(coff->symTable[idx]->getName(), this->name) == 0)) 
    idx++;
  if(idx < coff->header.nSymbols) 
    return coff->symTable[idx++];
  else
    return NULL;
}
//********************************************************
//------ Nested Class Section::RefSymIterator ------------
//********************************************************
Coff::RefSymIterator::RefSymIterator(Coff * c, word32 o, word16 nr){ 
  this->coff = c;
  this->offsetRelocations = o;
  this->nrOfReloc = nr;
}
Symbol * Coff::RefSymIterator::next(){
  if(nrOfReloc > 0){
    if(fseek(this->coff->file, offsetRelocations, SEEK_SET)) 
      return NULL;
    RelocationEntry entry;
    fread(&entry, sizeof(RelocationEntry), 1, coff->file);
    offsetRelocations += sizeof(RelocationEntry);
    nrOfReloc--;
    return coff->getSymbol(entry.symbolIndex);
  }
  else return NULL;
}