JAVAC=javac
OUTDIR=out
CLASSPATH=java-cup-11a.jar
sources = $(wildcard src/*.java) \
          $(wildcard src/SyntaxTree/*.java) \
	  $(wildcard src/Visitor/*.java) \
	  $(wildcard src/SymTable/*.java) \
	  $(wildcard src/IR/*.java) \
	  $(wildcard src/CodeGeneration/*.java)

classes = $(sources:.java=.class)

all: $(classes)

parser:
	java -jar java-cup-11a.jar -expect 50 -destdir src/ LexerAndParserSpec/minijava_parser.cup

lexer:
	jflex LexerAndParserSpec/PrettyLexer.flex -d src

clean:
	rm -f src/*.class \
	rm -f src/SyntaxTree/*.class \
	rm -f src/Visitor/*.class \
	rm -f src/IR/*.class \
	rm -f src/SymTable/*.class \
	rm -f src/CodeGeneration/*.class

%.class : %.java
	$(JAVAC) -classpath ${CLASSPATH} $(sources)
