JAVAC=javac
OUTDIR=out
CLASSPATH=java-cup-11a.jar
sources = $(wildcard src/*.java) \
          $(wildcard src/syntaxtree/*.java) \
	  $(wildcard src/visitor/*.java) \
	  $(wildcard src/SymmTable/*.java) \
	  $(wildcard src/IR/*.java)

classes = $(sources:.java=.class)

all: $(classes)

clean:
	rm -f src/*.class \
	rm -f AST/*.class \
	rm -f Visitors/*.class

%.class : %.java
	$(JAVAC) -classpath ${CLASSPATH} $(sources)

