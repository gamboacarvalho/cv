Objects with adaptive accessors to avoid STM barriers

https://dl.acm.org/doi/abs/10.1145/2331576.2331589

### Authors
Fernando Miguel Carvalho, João Cachopo
### Publication date
2012/7
### Conference
WTM 2012, EuroTM Workshop on Transactional Memory
### Description
Fernando Carvalho presented an extension of his previous work on adaptive object metadata [Multiprog-2012] that enables
switching between two meta-data representation modes:
a compact meta-data representation mode, optimized for
low contention scenarios, as it minimizes memory overheads but may suffer of aliasing phenomena leading to
unnecessary aborts; an extended meta-data representation mode, which has opposite advantages/drawbacks
when compared to the former meta-data representation
technique, thus resulting tailored for high contention
workloads. Preliminary results, obtained by integrating
this adaptive layout mechanism in the lock-free version
of the JVSTM have show an improvement in the performance and a reduction in the memory overheads for
workloads where the number of objects written is much
lower than the total number of transactional objects.