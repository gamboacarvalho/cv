Lightweight identification of captured memory for software transactional memory

https://scholar.google.com/citations?view_op=view_citation&hl=en&user=HldfYegAAAAJ&citation_for_view=HldfYegAAAAJ:2osOgNQ5qMEC

### Authors
Fernando Miguel Carvalho, Joao Cachopo
### Publication date
2013
### Conference
Algorithms and Architectures for Parallel Processing: 13th International Conference, ICA3PP 2013, Vietri sul Mare, Italy, December 18-20, 2013, Proceedings, Part I 13
### Pages
15-29
### Publisher
Springer International Publishing
### Description
Software Transactional Memory (STM) implementations typically instrument each memory access within transactions with a call to an STM barrier to ensure the correctness of the transactions. Compared to simple memory accesses, STM barriers are complex operations that add significant overhead to transactions doing many memory accesses. Thus, whereas STMs have shown good results for micro-benchmarks, where transactions are small, they often show poor performance on real-world–sized benchmarks, where transactions are more coarse-grained and, therefore, encompass more memory accesses.
In this paper, we propose a new runtime technique for lightweight identification of captured memory—LICM—for which no STM barriers are needed. Our technique is independent of the specific STM design and can be used by any STM implemented in a managed environment. We implemented …
### Total citations
Cited by 4