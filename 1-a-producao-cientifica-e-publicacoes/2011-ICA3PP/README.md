STM with transparent API considered harmful

https://scholar.google.com/citations?view_op=view_citation&hl=en&user=HldfYegAAAAJ&citation_for_view=HldfYegAAAAJ:u-x6o8ySG0sC

### Authors
Fernando Miguel Carvalho, Joao Cachopo
### Publication date
2011
### Conference
Algorithms and Architectures for Parallel Processing: 11th International Conference, ICA3PP, Melbourne, Australia, October 24-26, 2011, Proceedings, Part I 11
### Pages
326-337
### Publisher
Springer Berlin Heidelberg
### Description
One of the key selling points of Software Transactional Memory (STM) systems is that they simplify the development of concurrent programs, because programmers do not have to be concerned with which objects are accessed concurrently. Instead, they just have to say which operations are to be executed atomically. Yet, one of the consequences of making an STM completely transparent to the programmer is that it may incur in large overheads.
In this paper, we describe a port to Java of the WormBench benchmark, and use it to explore the effects on performance of relaxing the transparency of an STM. To that end, we implemented, in a well known STM framework (Deuce), a couple of annotations that allow programmers to specify that certain objects or fields of objects should not be transactified. Our results show that we get an improvement of up to 22-fold in the performance of the benchmark …
### Total citations
Cited by 6