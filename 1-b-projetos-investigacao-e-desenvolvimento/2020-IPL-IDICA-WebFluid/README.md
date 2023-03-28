## Project IPL IDICA 2020

**Title**: WebFluid - Reactive Web streams for Big Data

Projeto financiado pelo IPL no âmbito do concurso de Investigação, Desenvolvimento, Inovação e Criação Artística IDI&CA - 2020

### Budget

5 000 € EUR

### Team

* Principal Researcher: Fernando Miguel Gamboa de Carvalho
* Researchers: 
  * António Rito Silva INESC-ID/IST
  * Diogo Poeira, aluno de mestrado número 36238, MEIC, ADEETC

### Research Center

ISEL

### Description

Dealing with big data in web applications is not only concerned by data size, but also by data spread out over different nodes across the entire World Wide Web, which directly impacts on latency. Second, big data is often available in real-time which demands solutions able to asynchronously push data to its clients rather than having consumers synchronously pulling data from the source.

Facing these two challenges we have already achieved a successful proposal of Higher-order Templates (HoT) in our previous work (Carvalho & Duarte 2019). The HoT proposal can deal with large-data sets in modern web applications, providing progressive user-interface behavior and showing better performance than competition in realistic benchmarks that deal with lifelike data, such as Last.fm Web Api.

HoT is the result of an open source project – HtmlFlow – consisting of a Java DSL to write typesafe HTML, which is rated with 65 stars by the community in the biggest open source hosting provider: https://github.com/xmlet/HtmlFlow/. The functional and compositional nature of HtmlFlow distinguishes from the competition for: being data structureless, allowing method chaining calls and providing HTML safety. These key characteristics contribute to the increasing popularity of the HtmlFlow library, in addition to the better performance than state-of-the art template engines in well-known benchmarks, such as the spring-comparing-template-engines.

Now we propose to take a step forward, increasing HoT capabilities with non-blocking support for state-of-the-art data processing such as the reactive streams initiative (https://www.reactive-streams.org/) that provides a standard for asynchronous stream processing across different technological environments such as JVM, Javascript and others. Web template engines (such as JSP or Thymleaf) only support blocking HTML resolution given the text nature of the web templates. HtmlFlow disrupts the conceptual genesis of templates and introduces web templates as first-class functions that take full advantage of their intrinsic composability characteristics.

We will delegate on HtmlFlow infrastructure as the core backbone of web applications with server-side presentation, which still corresponds to the biggest installed base in web applications. In other recent work (Carvalho & Duarte 2019) we have already shown how HoT can be used in a renowned web application use-case (the spring-petclinic) leveraging the technological homogeneity between the backend and frontend around a single host programming language, such as Java. These observations give us promising perspectives about the use of this same approach in other web applications.

To evaluate the results of this work, we plan to test them both on a set of well-known benchmarks and for a selection of real-world applications that span different workload characteristics in big data scenarios.

We have extensive experience in the design, development and lead of complex web applications projects. This experience was accumulated over more than 20 years of research and development of innovative web applications and frameworks through Centro de Calculo of ISEL and as lecturers of Web Applications Development course of Computer Engineering at ISEL. Also, the responsible researcher for this proposal has large experience in the industry as project leader and performing a manager role at Altitude Software and Quatro SI companies. Our co-researcher also has a large experience as a lead of a diversity of projects of INESC-ID. Both have been working together since 2008 in INESC-ID and share common interests in Software Engineering field. We believe that the success of our projects, such as HtmlFlow.org  or https://fenix-framework.github.io/, are the result of the conciliation between our experience in academia and the industry, combining accuracy with pragmatism.
