# 1622Compiler
Compiler for CS 1622, Team Members: Kevin Ireland, Adnan Khan
Professor: Dr. Misurda
Overview
--------

This repository, and the project contained within is for 1622 Introduction to Compilers taught by Dr. Misurda.

This project is a collaboration between Pitt Computer Science Students Adnan Khan and Kevin Ireland.

This is a compiler for MiniJava, which is a subset of the Java programming language. Many features that java has
are not present in MiniJava. This portion of the project is the "middle portion" of the project, the final step in this
will be to generate an intermediate representation (IR) that will be passed to the final stage of the project to be
transformed into MIPS assembly code.

Implementation Details:
------------
 
 The compiler is built using JFlex as a lexer, JavaCUP as a parser generator. The AST Nodes are provided as part of
 the Modern Compiler Implemmentation in Java textbook. The nodes have been modified to include line and colummn information
 for purposes of error reporting.

Project Progress:
-----------

Parser Errors [ ]
Name Analysis [ ]
Type Checking [ ]

    Symbol Table Generation:

    Errors:
    1.       Status:
    2.       Status:
    3.       Status:
    4.       Status:
    5.       Status:
    6.       Status:
    7.       Status:
    8.       Status:
    9.       Status:
    
  
IR Generation [ ]

Building:
----------
To build this compiler simply run

~~~~
make
~~~~

In the base directory. This will compile all java files. As submitted this projected has the lexer and parser
pre generated in their java file forms, however the makefile included allows easy re-build functionality
for both the lexer and parser:
 
 If it is necessary to re-generate the lexer:

~~~~~~~~~
make lexer
~~~~~~~~~

Note that doing this requires that jflex is installed on the system and on the classpath. If you can type:

~~~~~
jflex 
~~~~~
on the commmand line and it runs, then the system is properly configured.

Likewise to re-generate the parser:

~~~~~~~~~~
make parser
~~~~~~~~~~~
Running:
-----------

To run the compiler