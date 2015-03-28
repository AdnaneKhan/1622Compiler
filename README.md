# 1622Compiler
Compiler for CS 1622, Team Members: Kevin Ireland, Adnan Khan

Overview
--------

This repository, and the project contained within is for 1622 Introduction to Compilers taught by Dr. Misurda.

This project is a collaboration between Pitt Computer Science Students Adnan Khan and Kevin Ireland.

Implementation Details:
------------
 
 
 The compiler is built using JFlex as a lexer, JavaCUP as a parser generator. The AST Nodes are provided as part of
 the Modern Compiler Implemmentation in Java textbook. The nodes have been modified to include line and colummn information
 for purposes of error reporting.
 

Building:
----------
To build this compiler simply run

~~~~
make
~~~~

In the base directory. This will compile all java files. If it is necessary to re-generate the lexer:


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




