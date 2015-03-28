JAVAC=javac
OUTDIR=out
CLASSPATH=java-cup-11a.jar
sources = $(wildcard src/*.java) \
          $(wildcard src/syntaxtree/*.java) \
	  $(wildcard src/visitor/*.java) \
	  $(wildcard src/SymTable/*.java) \
	  $(wildcard src/IR/*.java)

classes = $(sources:.java=.class)

all: $(classes)

parser:
	java -jar java-cup-11a.jar -expect 50 -destdir src/ LexerAndParserSpec/minijava_parser.cup

lexer:
	jflex LexerAndParserSpec/PrettyLexer.flex -d src

clean:
	rm -f src/*.class \
	rm -f src/syntaxtree/*.class \
	rm -f src/visitor/*.class \
	rm -f src/IR/*.class \
	rm -f src/SymTable/*.class

%.class : %.java
	$(JAVAC) -classpath ${CLASSPATH} $(sources)
