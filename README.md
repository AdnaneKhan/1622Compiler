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
 the Modern Compiler Implementation in Java textbook. The nodes have been modified to include line and column information
 for purposes of error reporting.
 
 In addition to this nodes that represent parse errors have been added. This allows name analysis and type checking to continue
 in cases where pare errors were recovered from.

Considerations:
-----------------

When repoting line numbers, the lexer starts at line 0, we add one to each line because it is standard for text editors and IDEs to start their
line numbers at 1, so for a a programmer looking at errors, it makes more sense to start at 1.

Project Progress:
-----------

Parser Errors [ x ]
Name Analysis [ x ]
Type Checking [ x ]

    Symbol Table Generation:

    Errors:
    1.  Invalid L                       Status:
    2.  Invalid R                       Status:
    3.  Invalid Op                      Status:
    4.  Non Method                      Status:
    5.  Arg Count                       Status:
    6.  Arg Type                        Status:
    7.  Non Integer Op                  Status:
    8.  Boolean on non bool             Status:
    9.  Bad Length call                 Status:
    10. Non Boolean in Condition        Status:
    11. Type Mismatch During Assignment Status:
    12. Illegal use of This             Status:
    

IR Generation [ X ]

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

To run the front end do the following:

~~~~
java -cp java-cup-11a.jar:. FrontEnd FILENAME
~~~~

where filename is the name of the file to be lexed, parsed, and error checked.

If no errors are encountered then the file will be printed out to an intermediate representation in the console.
In addition the file will be printed to the "output" directory as FILENAME.IRs