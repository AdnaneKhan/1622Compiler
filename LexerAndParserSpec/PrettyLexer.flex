/**
 * Author: Adnan Khan
 * Project 1 CS 1622
 *
 * Using JFlex example as skeleton to start from
 *
 *
 *
 */
 import java_cup.runtime.*;
 import SyntaxTree.*;
%%
%cup
%public
%class MiniJavaLexer
%line
%column
%unicode
%{

  private Symbol symbol(int type, Object value) {
      //  System.out.println("We are outputting: " + type + " and " + value.toString());
        return new Symbol(type,yyline,yycolumn ,value);
  }

  private Symbol symbol(int type) {
       // System.out.println("We are outputting: " + type);
        return new Symbol(type,yyline,yycolumn);
  }
%}
LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

/* comments */
Comment =   "//" {InputCharacter}* {LineTerminator}?
BlockComment = "/*"~"*/"

// [] and () with possible whitespace within matched
CurlyClose           = \}
CurlyOpen            = \{
ParenClose           = \)
ParenOpen            = \(
BrackOpen            = \[
BrackClose           = \]
SemiColon            = ;
Comma                = \,
Dot                  = \.
Not                  = \!
Identifier = [:jletter:] [:jletterdigit:]*
BoolLiteral = true | false
IntegerLiteral = 0 | [1-9][0-9]*

%%

   <YYINITIAL> {
       /* Keywords */
       "System.out.println"      { return symbol( sym.PRINT, "System.out.println");}
       "extends"                 { return symbol( sym.EXTENDS," "+   yytext() + " ");}
       "public"                  { return symbol( sym.PUBLIC, yytext()); }
       "length"                  { return symbol( sym.LENGTH, yytext()); }
       "public"                  { return symbol( sym.PUBLIC, yytext()); }
       "static"                  { return symbol (sym.STATIC,yytext()); }
       // Note that for this we catch void main together because
       // main can possibly be used as an identifier but minijava requires a main class
       // to be specified.
       "void"{WhiteSpace}+"main" { return symbol( sym.VOID_MAIN); }
       "class"                   { return symbol( sym.CLASS, yytext()); }
       "new"                     { return symbol( sym.NEW, yytext()); }
       "int"                     { return symbol( sym.INT, yytext()); }
       "boolean"                 { return symbol( sym.BOOLEAN, yytext()); }
       "String"                  { return symbol( sym.STRING); }
       "if"                      { return symbol( sym.IF, yytext()); }
       "else"                    { return symbol( sym.ELSE, "else");}
       "return"                  { return symbol( sym.RETURN); }
       "true"                    { return symbol( sym.TRUE); }
       "false"                   { return symbol( sym.FALSE); }
       "while"                   { return symbol( sym.WHILE, yytext());}
      {IntegerLiteral}           { return symbol( sym.INTEGER_LITERAL, Integer.parseInt(yytext())); }
      {Comma}                    { return symbol( sym.COMMA,yytext()+" ");}
      {BrackClose}               { return symbol( sym.RIGHT_B,"]");}
      {BrackOpen}                { return symbol( sym.LEFT_B, "[");}
      {ParenOpen}                { return symbol( sym.LEFT_PAREN, "(");}
      {ParenClose}               { return symbol( sym.RIGHT_PAREN, ")");}
      {Dot}                      { return symbol( sym.DOT, "."); }
      {CurlyOpen}                { return symbol( sym.LEFT_CURLY); }
      {CurlyClose}               { return symbol( sym.RIGHT_CURLY); }
      {Not}                      { return symbol( sym.NOT ); }
      "+"                        { return symbol( sym.PLUS); }
      "-"                        { return symbol( sym.MINUS); }
      "*"                        { return symbol( sym.TIMES); }
      "<"                        { return symbol( sym.LESS); }
      "="                        { return symbol( sym.ASSIGN, yytext());}
      "&&"                       { return symbol( sym.AND, yytext()); }
      {SemiColon}                { return symbol( sym.SEMI, ";");}
      {Identifier}               { return symbol( sym.ID, new Identifier(yytext(),yyline,yycolumn));}
      /* comments */
      {Comment}                  { /* Ignore */ }
      {BlockComment}             { /* Ignore */ }
      /* whitespace */
      {WhiteSpace}               { /* Ignore */ }
      /* error fallback */
      [^]                        { return symbol(sym.UNKNOWN, yytext()); }
   }