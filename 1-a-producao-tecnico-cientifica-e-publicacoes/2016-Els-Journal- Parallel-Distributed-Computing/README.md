Optimizing memory transactions for large-scale programs

https://scholar.google.com/citations?view_op=view_citation&hl=en&user=HldfYegAAAAJ&citation_for_view=HldfYegAAAAJ:Y0pCki6q_DkC

### Authors
Fernando Miguel Carvalho, João Cachopo
### Publication date
2016/3/1
### Journal
Elsevier - Journal of Parallel and Distributed Computing
### Volume
89
### Pages
13-24
### Description
Even though Software Transactional Memory (STM) is one of the most promising approaches to simplify concurrent programming, current STM implementations incur significant overheads that render them impractical for many real-sized programs. The key insight of this work is that we do not need to use the same costly barriers for all the memory managed by a real-sized application, if only a small fraction of the memory is under contention—lightweight barriers may be used in this case. In this work, we propose a new solution based on an approach of adaptive object metadata (AOM) to promote the use of a fast path to access objects that are not under contention. We show that this approach is able to make the performance of an STM competitive with the best fine-grained lock-based approaches in some of the more challenging benchmarks.
### Total citations
Cited by 3