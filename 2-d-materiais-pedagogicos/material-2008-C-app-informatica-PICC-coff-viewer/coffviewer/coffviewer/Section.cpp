#include "Section.h"
#include "Coff.h"
#include <stdlib.h>
#include <string.h>
#include <math.h>

extern char * sectionFlagsMsg[];

Section::Section(int n, Coff *c){
  this->number = n;
  this->coff = c;
  c->loadSection(&this->header, n-1);
  if(this->header.name[0] == '/'){
    word32 offset;
    char * str = header.name + 1;
    sscanf(str, "%u", &offset);
    this->name = c->loadString(offset);
  }else{
    name = new char[9];
    name[8] = '\0';
    memcpy(name,header.name, 8);
  }
}
Section::~Section(){
  delete name;
}
int Section::getAlign(){
  word32 align = 0x00F00000 & header.flags;
  align >>= 20;
  return (int) pow(2.f, (int) align-1);
}
char * Section::getTxtFlags(){
  char * str = "";
  int idx = 0;
  word32 flags = this->header.flags;
  while(flags != 0){
    if( (flags & 1) && (sectionFlagsMsg[idx] != 0)){
      char * aux = new char[strlen(str) + strlen(sectionFlagsMsg[idx]) + 2];
      sprintf(aux, "%s%s,", str, sectionFlagsMsg[idx]);
      str = aux;
    }
    flags >>= 1;
    idx++;
  }
  return str;
}
SymIterator * Section::DefSymIterator(){
  return new Coff::DefSymIterator(this->coff, this->number, this->name);
}
SymIterator * Section::RefSymIterator(){
  return new Coff::RefSymIterator(coff, header.posRelocations, header.nRelocations);
}
