#include "Coff.h"
#include <string.h>

Symbol::Symbol(int num, Coff *c){
  number = num;
  c->loadSymbol(&entry, num);
  if(entry.name.longName.zeros == 0){
    name = c->loadString(entry.name.longName.pos);
  }else{
    name = new char[9];
    name[8] = '\0';
    memcpy(name,entry.name.shortName, 8);
  }
}
Symbol::~Symbol(){
  delete name;
}
void Symbol::print(){
  printf("  %2d - %-10s\n",this->number,this->name);
}