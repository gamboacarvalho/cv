Deconstructing yield Operator to Enhance Streams Processing

https://www.scitepress.org/PublicationsDetail.aspx?ID=sFFvNu0PTDs=&t=1

### Authors
Fernando Miguel Carvalho, Diogo Poeira
### Publication date
2021/1
### Conference
16th International Conference on Software Technologies - ICSOFT
### Pages
143-150
### Description
Customizing streams pipelines with new user-defined operations is a well-known pattern regarding streams processing. However, programming languages face two challenges when considering streams extensibility: 1) provide a compact and readable way to express new operations, and 2) keep streams’ laziness behavior. From here, we may find a consensus around the adoption of the generator operator, i.e. yield, as a means to fulfil both requirements, since most state-of-the-art programming languages provide this feature. Yet, what is the performance overhead of interleaving a yield-based operation in streams processing? In this work we present a benchmark based on realistic use cases of two different web APIs, namely: Last.fm and world weather online, where custom yield-based operations may degrade the streams performance in twofold. We also propose a purely functional and minimalistic design, named tinyield, that can be easily adopted in any programming language and provides a concise way o f chaining extension operations fluently, with low overhead in the evaluated benchmarks. The tinyield proposal was deployed in three different libraries, namely for Java (jayield), JavaScript (tinyield4ts) and .Net (tinyield4net).