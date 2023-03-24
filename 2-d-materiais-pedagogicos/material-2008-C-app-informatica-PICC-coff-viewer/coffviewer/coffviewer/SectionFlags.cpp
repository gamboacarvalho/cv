
// To implement    Section::getTxtFlags()

char * sectionFlagsMsg[32] = {
0,0,0,0,0, 
/* 0x00000020 */ "Code", 
/* 0x00000040 */ "Init", 
/* 0x00000080 */ "Uninit Data", 
/* 0x00000100 */ 0,
/* 0x00000200 */ "Info", 
/* 0x00000400 */ 0,
/* 0x00000800 */ "Remove", 
/* 0x00001000 */ "Data",
0,0,
/* 0x00008000 */ "Referenced through GP",
0,0,0,0,
/* 0x00100000-0x00800000 */ 0,0,0,0, /* Align data bits */
/* 0x01000000 */ "Relocations",
/* 0x02000000 */ "Discardable", 
/* 0x04000000 */ "Cannot be cached",
/* 0x08000000 */ "Not pageable",
/* 0x10000000 */ "Shared", 
/* 0x20000000 */ "Exec",
/* 0x40000000 */ "Read",
/* 0x80000000 */ "Write",
};
