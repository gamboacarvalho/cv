## Projeto PTDC/EIA-EIA/108240/2008

**Title**: RuLAM: Running Legacy Applications on Multicores

### Budget

125 256,00€ EUR

### Team

* Principal Researcher: João Cachopo
* Researchers: 
  * João Manuel Pinheiro Cachopo
  * Antonio Paulo Teles de Menezes Leitão
  * Fernando Miguel Carvalho
  * João Carlos Serrenho Dias Pereira
  * Luís Manuel Antunes Veiga
  * Paolo Romano
  * Sérgio Miguel Martinho Fernandes

### Research Center

INESC ID

### Description

A common saying of late is that "The Future is Parallel." With multicore architectures becoming commonplace, it seems clear that it will in fact be; at least, from an hardware point of view. As to the software, however, things are not that clear. Most of the software that we run on our computers today is sequential and it will not become parallel overnight. Even if concurrent programming was easy, which it is not, rewriting or refactoring a significant fraction of that software into highly parallel programs is not feasible. Thus, if we want to use the newly available hardware to make our programs run faster, there is only one alternative: to parallelize automatically those programs.

But neither parallel computing is a recent invention, nor the idea of automatic parallelization is new. Parallel computers exist for at least 40 years, and there is a considerable amount of research on parallelizing compilers and other alternatives of extracting parallelism from sequential programs. What is new now is that parallel computing is no longer confined to the realms of high-performance computing for solving scientific problems. The programs that we want to parallelize today are of a different breed than those of the past. They require new approaches and techniques [von Praun 07].

In this project we propose to build on the work done in the area of automatic parallelization, and extend it with the new and promising advances made in the area of Software Transactional Memory (STM).

The state of the art in the field of automatic parallelization is the use of ThreadLevel Speculation (TLS), which relies on hardware support at the processor level for speculatively executing parts of a program in parallel. We argue that the use of an hardware-only approach for this is a limiting factor that severely constrains the applicability and scalability of these approaches. To solve this problem, we propose to use TLS with an STM-based approach, instead.

We have extensive experience in the design, implementation, and use of STMs [Cachopo 06b, Cachopo 06a, Cachopo 07, Carvalho 08]. This experience was accumulated over the last five years of research and development of an innovative STM that is used since 2005 to support the concurrent execution of the FénixEDU web application. This is a large real-world web application that supports many of the activities in one of the largest universities in Portugal. With this project, we intend to leverage on our expertise in STMs and use it for improving TLS.

But, even though using an STM for TLS may bring many benefits, it also presents some challenges. E.g., how to design and implement an STM that may compete with the performance of an hardware approach? Or, at least, how can we make it sufficiently fast so that its use is beneficial in the general case? Moreover, TLS has specific needs that will push the requirements of an STM. So, with this project, we expect to contribute also to advance the research in the STM area.

The key idea for this project is to develop a runtime system for the automatic parallelization of programs. Instead of following the conventional compiler-based approach of program analysis, we aim at automatically parallelizing programs at the runtime level. The goal of this approach is twofold: (1) to be able to parallelize programs without changing them (or for which we do not have the source code); and (2) to adapt to the hardware where the program executes without recompiling.

Even though we expect that our work will be applicable to other runtime systems, we will target specifically the Java platform on this project. We propose to design and to build a Java Virtual Machine (JVM) that is suited for speculatively executing sequential programs on massively parallel hardware. We choose Java, not only to build on our previous work, but also because the JVM is widely used in the software industry. This means that there is a large body of programs that may benefit from the project’s results.

To achieve our goals, we propose to work on the following main topics:
- A method for "transactifying" a program so that it may be TLS-executed under the control of an STM.
- An implementation of our previous work on STMs at the JVM level, to improve the STM’s performance.
- New garbage collection algorithms that cooperate with the STM and that are better suited for massively parallel hardware.
- A new TLS approach that (recursively) splits a sequential program into tasks and then speculatively executes them in parallel under the control of an STM.