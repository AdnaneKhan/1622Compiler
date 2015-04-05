/* The following code was generated by JFlex 1.6.0 */

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

/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.6.0
 * from the specification file <tt>LexerAndParserSpec/PrettyLexer.flex</tt>
 */
public class MiniJavaLexer implements java_cup.runtime.Scanner {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0, 0
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\21\1\3\1\2\1\0\1\3\1\1\16\21\4\0\1\3\1\17"+
    "\2\0\1\20\1\0\1\57\1\0\1\11\1\10\1\5\1\53\1\15"+
    "\1\54\1\16\1\4\1\32\11\33\1\0\1\14\1\55\1\56\3\0"+
    "\22\20\1\34\7\20\1\12\1\0\1\13\1\0\1\20\1\0\1\27"+
    "\1\45\1\46\1\44\1\25\1\26\1\47\1\50\1\41\2\20\1\30"+
    "\1\36\1\42\1\37\1\40\1\20\1\23\1\31\1\22\1\24\1\51"+
    "\1\52\1\43\1\35\1\20\1\7\1\0\1\6\1\0\41\21\2\0"+
    "\4\20\4\0\1\20\2\0\1\21\7\0\1\20\4\0\1\20\5\0"+
    "\27\20\1\0\37\20\1\0\u01ca\20\4\0\14\20\16\0\5\20\7\0"+
    "\1\20\1\0\1\20\21\0\160\21\5\20\1\0\2\20\2\0\4\20"+
    "\10\0\1\20\1\0\3\20\1\0\1\20\1\0\24\20\1\0\123\20"+
    "\1\0\213\20\1\0\5\21\2\0\236\20\11\0\46\20\2\0\1\20"+
    "\7\0\47\20\7\0\1\20\1\0\55\21\1\0\1\21\1\0\2\21"+
    "\1\0\2\21\1\0\1\21\10\0\33\20\5\0\3\20\15\0\5\21"+
    "\6\0\1\20\4\0\13\21\5\0\53\20\37\21\4\0\2\20\1\21"+
    "\143\20\1\0\1\20\10\21\1\0\6\21\2\20\2\21\1\0\4\21"+
    "\2\20\12\21\3\20\2\0\1\20\17\0\1\21\1\20\1\21\36\20"+
    "\33\21\2\0\131\20\13\21\1\20\16\0\12\21\41\20\11\21\2\20"+
    "\4\0\1\20\5\0\26\20\4\21\1\20\11\21\1\20\3\21\1\20"+
    "\5\21\22\0\31\20\3\21\104\0\1\20\1\0\13\20\67\0\33\21"+
    "\1\0\4\21\66\20\3\21\1\20\22\21\1\20\7\21\12\20\2\21"+
    "\2\0\12\21\1\0\7\20\1\0\7\20\1\0\3\21\1\0\10\20"+
    "\2\0\2\20\2\0\26\20\1\0\7\20\1\0\1\20\3\0\4\20"+
    "\2\0\1\21\1\20\7\21\2\0\2\21\2\0\3\21\1\20\10\0"+
    "\1\21\4\0\2\20\1\0\3\20\2\21\2\0\12\21\4\20\7\0"+
    "\1\20\5\0\3\21\1\0\6\20\4\0\2\20\2\0\26\20\1\0"+
    "\7\20\1\0\2\20\1\0\2\20\1\0\2\20\2\0\1\21\1\0"+
    "\5\21\4\0\2\21\2\0\3\21\3\0\1\21\7\0\4\20\1\0"+
    "\1\20\7\0\14\21\3\20\1\21\13\0\3\21\1\0\11\20\1\0"+
    "\3\20\1\0\26\20\1\0\7\20\1\0\2\20\1\0\5\20\2\0"+
    "\1\21\1\20\10\21\1\0\3\21\1\0\3\21\2\0\1\20\17\0"+
    "\2\20\2\21\2\0\12\21\1\0\1\20\17\0\3\21\1\0\10\20"+
    "\2\0\2\20\2\0\26\20\1\0\7\20\1\0\2\20\1\0\5\20"+
    "\2\0\1\21\1\20\7\21\2\0\2\21\2\0\3\21\10\0\2\21"+
    "\4\0\2\20\1\0\3\20\2\21\2\0\12\21\1\0\1\20\20\0"+
    "\1\21\1\20\1\0\6\20\3\0\3\20\1\0\4\20\3\0\2\20"+
    "\1\0\1\20\1\0\2\20\3\0\2\20\3\0\3\20\3\0\14\20"+
    "\4\0\5\21\3\0\3\21\1\0\4\21\2\0\1\20\6\0\1\21"+
    "\16\0\12\21\11\0\1\20\7\0\3\21\1\0\10\20\1\0\3\20"+
    "\1\0\27\20\1\0\12\20\1\0\5\20\3\0\1\20\7\21\1\0"+
    "\3\21\1\0\4\21\7\0\2\21\1\0\2\20\6\0\2\20\2\21"+
    "\2\0\12\21\22\0\2\21\1\0\10\20\1\0\3\20\1\0\27\20"+
    "\1\0\12\20\1\0\5\20\2\0\1\21\1\20\7\21\1\0\3\21"+
    "\1\0\4\21\7\0\2\21\7\0\1\20\1\0\2\20\2\21\2\0"+
    "\12\21\1\0\2\20\17\0\2\21\1\0\10\20\1\0\3\20\1\0"+
    "\51\20\2\0\1\20\7\21\1\0\3\21\1\0\4\21\1\20\10\0"+
    "\1\21\10\0\2\20\2\21\2\0\12\21\12\0\6\20\2\0\2\21"+
    "\1\0\22\20\3\0\30\20\1\0\11\20\1\0\1\20\2\0\7\20"+
    "\3\0\1\21\4\0\6\21\1\0\1\21\1\0\10\21\22\0\2\21"+
    "\15\0\60\20\1\21\2\20\7\21\4\0\10\20\10\21\1\0\12\21"+
    "\47\0\2\20\1\0\1\20\2\0\2\20\1\0\1\20\2\0\1\20"+
    "\6\0\4\20\1\0\7\20\1\0\3\20\1\0\1\20\1\0\1\20"+
    "\2\0\2\20\1\0\4\20\1\21\2\20\6\21\1\0\2\21\1\20"+
    "\2\0\5\20\1\0\1\20\1\0\6\21\2\0\12\21\2\0\4\20"+
    "\40\0\1\20\27\0\2\21\6\0\12\21\13\0\1\21\1\0\1\21"+
    "\1\0\1\21\4\0\2\21\10\20\1\0\44\20\4\0\24\21\1\0"+
    "\2\21\5\20\13\21\1\0\44\21\11\0\1\21\71\0\53\20\24\21"+
    "\1\20\12\21\6\0\6\20\4\21\4\20\3\21\1\20\3\21\2\20"+
    "\7\21\3\20\4\21\15\20\14\21\1\20\17\21\2\0\46\20\1\0"+
    "\1\20\5\0\1\20\2\0\53\20\1\0\u014d\20\1\0\4\20\2\0"+
    "\7\20\1\0\1\20\1\0\4\20\2\0\51\20\1\0\4\20\2\0"+
    "\41\20\1\0\4\20\2\0\7\20\1\0\1\20\1\0\4\20\2\0"+
    "\17\20\1\0\71\20\1\0\4\20\2\0\103\20\2\0\3\21\40\0"+
    "\20\20\20\0\125\20\14\0\u026c\20\2\0\21\20\1\0\32\20\5\0"+
    "\113\20\3\0\3\20\17\0\15\20\1\0\4\20\3\21\13\0\22\20"+
    "\3\21\13\0\22\20\2\21\14\0\15\20\1\0\3\20\1\0\2\21"+
    "\14\0\64\20\40\21\3\0\1\20\3\0\2\20\1\21\2\0\12\21"+
    "\41\0\3\21\2\0\12\21\6\0\130\20\10\0\51\20\1\21\1\20"+
    "\5\0\106\20\12\0\35\20\3\0\14\21\4\0\14\21\12\0\12\21"+
    "\36\20\2\0\5\20\13\0\54\20\4\0\21\21\7\20\2\21\6\0"+
    "\12\21\46\0\27\20\5\21\4\0\65\20\12\21\1\0\35\21\2\0"+
    "\13\21\6\0\12\21\15\0\1\20\130\0\5\21\57\20\21\21\7\20"+
    "\4\0\12\21\21\0\11\21\14\0\3\21\36\20\15\21\2\20\12\21"+
    "\54\20\16\21\14\0\44\20\24\21\10\0\12\21\3\0\3\20\12\21"+
    "\44\20\122\0\3\21\1\0\25\21\4\20\1\21\4\20\3\21\2\20"+
    "\11\0\300\20\47\21\25\0\4\21\u0116\20\2\0\6\20\2\0\46\20"+
    "\2\0\6\20\2\0\10\20\1\0\1\20\1\0\1\20\1\0\1\20"+
    "\1\0\37\20\2\0\65\20\1\0\7\20\1\0\1\20\3\0\3\20"+
    "\1\0\7\20\3\0\4\20\2\0\6\20\4\0\15\20\5\0\3\20"+
    "\1\0\7\20\16\0\5\21\32\0\5\21\20\0\2\20\23\0\1\20"+
    "\13\0\5\21\5\0\6\21\1\0\1\20\15\0\1\20\20\0\15\20"+
    "\3\0\33\20\25\0\15\21\4\0\1\21\3\0\14\21\21\0\1\20"+
    "\4\0\1\20\2\0\12\20\1\0\1\20\3\0\5\20\6\0\1\20"+
    "\1\0\1\20\1\0\1\20\1\0\4\20\1\0\13\20\2\0\4\20"+
    "\5\0\5\20\4\0\1\20\21\0\51\20\u0a77\0\57\20\1\0\57\20"+
    "\1\0\205\20\6\0\4\20\3\21\2\20\14\0\46\20\1\0\1\20"+
    "\5\0\1\20\2\0\70\20\7\0\1\20\17\0\1\21\27\20\11\0"+
    "\7\20\1\0\7\20\1\0\7\20\1\0\7\20\1\0\7\20\1\0"+
    "\7\20\1\0\7\20\1\0\7\20\1\0\40\21\57\0\1\20\u01d5\0"+
    "\3\20\31\0\11\20\6\21\1\0\5\20\2\0\5\20\4\0\126\20"+
    "\2\0\2\21\2\0\3\20\1\0\132\20\1\0\4\20\5\0\51\20"+
    "\3\0\136\20\21\0\33\20\65\0\20\20\u0200\0\u19b6\20\112\0\u51cd\20"+
    "\63\0\u048d\20\103\0\56\20\2\0\u010d\20\3\0\20\20\12\21\2\20"+
    "\24\0\57\20\1\21\4\0\12\21\1\0\31\20\7\0\1\21\120\20"+
    "\2\21\45\0\11\20\2\0\147\20\2\0\4\20\1\0\4\20\14\0"+
    "\13\20\115\0\12\20\1\21\3\20\1\21\4\20\1\21\27\20\5\21"+
    "\20\0\1\20\7\0\64\20\14\0\2\21\62\20\21\21\13\0\12\21"+
    "\6\0\22\21\6\20\3\0\1\20\4\0\12\21\34\20\10\21\2\0"+
    "\27\20\15\21\14\0\35\20\3\0\4\21\57\20\16\21\16\0\1\20"+
    "\12\21\46\0\51\20\16\21\11\0\3\20\1\21\10\20\2\21\2\0"+
    "\12\21\6\0\27\20\3\0\1\20\1\21\4\0\60\20\1\21\1\20"+
    "\3\21\2\20\2\21\5\20\2\21\1\20\1\21\1\20\30\0\3\20"+
    "\2\0\13\20\5\21\2\0\3\20\2\21\12\0\6\20\2\0\6\20"+
    "\2\0\6\20\11\0\7\20\1\0\7\20\221\0\43\20\10\21\1\0"+
    "\2\21\2\0\12\21\6\0\u2ba4\20\14\0\27\20\4\0\61\20\u2104\0"+
    "\u016e\20\2\0\152\20\46\0\7\20\14\0\5\20\5\0\1\20\1\21"+
    "\12\20\1\0\15\20\1\0\5\20\1\0\1\20\1\0\2\20\1\0"+
    "\2\20\1\0\154\20\41\0\u016b\20\22\0\100\20\2\0\66\20\50\0"+
    "\15\20\3\0\20\21\20\0\7\21\14\0\2\20\30\0\3\20\31\0"+
    "\1\20\6\0\5\20\1\0\207\20\2\0\1\21\4\0\1\20\13\0"+
    "\12\21\7\0\32\20\4\0\1\20\1\0\32\20\13\0\131\20\3\0"+
    "\6\20\2\0\6\20\2\0\6\20\2\0\3\20\3\0\2\20\3\0"+
    "\2\20\22\0\3\21\4\0\14\20\1\0\32\20\1\0\23\20\1\0"+
    "\2\20\1\0\17\20\2\0\16\20\42\0\173\20\105\0\65\20\210\0"+
    "\1\21\202\0\35\20\3\0\61\20\57\0\37\20\21\0\33\20\65\0"+
    "\36\20\2\0\44\20\4\0\10\20\1\0\5\20\52\0\236\20\2\0"+
    "\12\21\u0356\0\6\20\2\0\1\20\1\0\54\20\1\0\2\20\3\0"+
    "\1\20\2\0\27\20\252\0\26\20\12\0\32\20\106\0\70\20\6\0"+
    "\2\20\100\0\1\20\3\21\1\0\2\21\5\0\4\21\4\20\1\0"+
    "\3\20\1\0\33\20\4\0\3\21\4\0\1\21\40\0\35\20\203\0"+
    "\66\20\12\0\26\20\12\0\23\20\215\0\111\20\u03b7\0\3\21\65\20"+
    "\17\21\37\0\12\21\20\0\3\21\55\20\13\21\2\0\1\21\22\0"+
    "\31\20\7\0\12\21\6\0\3\21\44\20\16\21\1\0\12\21\100\0"+
    "\3\21\60\20\16\21\4\20\13\0\12\21\u04a6\0\53\20\15\21\10\0"+
    "\12\21\u0936\0\u036f\20\221\0\143\20\u0b9d\0\u042f\20\u33d1\0\u0239\20\u04c7\0"+
    "\105\20\13\0\1\20\56\21\20\0\4\21\15\20\u4060\0\2\20\u2163\0"+
    "\5\21\3\0\26\21\2\0\7\21\36\0\4\21\224\0\3\21\u01bb\0"+
    "\125\20\1\0\107\20\1\0\2\20\2\0\1\20\2\0\2\20\2\0"+
    "\4\20\1\0\14\20\1\0\1\20\1\0\7\20\1\0\101\20\1\0"+
    "\4\20\2\0\10\20\1\0\7\20\1\0\34\20\1\0\4\20\1\0"+
    "\5\20\1\0\1\20\3\0\7\20\1\0\u0154\20\2\0\31\20\1\0"+
    "\31\20\1\0\37\20\1\0\31\20\1\0\37\20\1\0\31\20\1\0"+
    "\37\20\1\0\31\20\1\0\37\20\1\0\31\20\1\0\10\20\2\0"+
    "\62\21\u1600\0\4\20\1\0\33\20\1\0\2\20\1\0\1\20\2\0"+
    "\1\20\1\0\12\20\1\0\4\20\1\0\1\20\1\0\1\20\6\0"+
    "\1\20\4\0\1\20\1\0\1\20\1\0\1\20\1\0\3\20\1\0"+
    "\2\20\1\0\1\20\2\0\1\20\1\0\1\20\1\0\1\20\1\0"+
    "\1\20\1\0\1\20\1\0\2\20\1\0\1\20\2\0\4\20\1\0"+
    "\7\20\1\0\4\20\1\0\4\20\1\0\1\20\1\0\12\20\1\0"+
    "\21\20\5\0\3\20\1\0\5\20\1\0\21\20\u1144\0\ua6d7\20\51\0"+
    "\u1035\20\13\0\336\20\u3fe2\0\u021e\20\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\u05ee\0"+
    "\1\21\36\0\140\21\200\0\360\21\uffff\0\uffff\0\ufe12\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\2\2\1\1\1\3\1\4\1\5\1\6"+
    "\1\7\1\10\1\11\1\12\1\13\1\14\1\15\7\16"+
    "\2\17\10\16\1\20\1\21\1\22\1\23\1\1\1\2"+
    "\1\0\13\16\1\24\6\16\1\25\1\0\13\16\1\26"+
    "\1\27\4\16\1\30\1\31\1\16\1\32\15\16\1\33"+
    "\6\16\1\34\1\0\1\35\1\36\1\16\1\37\1\40"+
    "\1\41\1\16\1\42\1\16\1\0\1\43\1\0\1\44"+
    "\4\0\1\45\10\0\1\46";

  private static int [] zzUnpackAction() {
    int [] result = new int[130];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\60\0\140\0\60\0\220\0\60\0\60\0\60"+
    "\0\60\0\60\0\60\0\60\0\60\0\60\0\60\0\60"+
    "\0\300\0\360\0\u0120\0\u0150\0\u0180\0\u01b0\0\u01e0\0\60"+
    "\0\u0210\0\u0240\0\u0270\0\u02a0\0\u02d0\0\u0300\0\u0330\0\u0360"+
    "\0\u0390\0\60\0\60\0\60\0\60\0\u03c0\0\u03f0\0\u0420"+
    "\0\u0450\0\u0480\0\u04b0\0\u04e0\0\u0510\0\u0540\0\u0570\0\u05a0"+
    "\0\u05d0\0\u0600\0\u0630\0\300\0\u0660\0\u0690\0\u06c0\0\u06f0"+
    "\0\u0720\0\u0750\0\60\0\u0780\0\u07b0\0\u07e0\0\u0810\0\u0840"+
    "\0\u0870\0\u08a0\0\u08d0\0\u0900\0\u0930\0\u0960\0\u0990\0\300"+
    "\0\300\0\u09c0\0\u09f0\0\u0a20\0\u0a50\0\300\0\300\0\u0a80"+
    "\0\300\0\u0ab0\0\u0ae0\0\u0b10\0\u0b40\0\u0b70\0\u0ba0\0\u0bd0"+
    "\0\u0c00\0\u0c30\0\u0c60\0\u0c90\0\u0cc0\0\u0cf0\0\300\0\u0d20"+
    "\0\u0d50\0\u0d80\0\u0db0\0\u0de0\0\u0e10\0\300\0\u0e40\0\300"+
    "\0\300\0\u0e70\0\300\0\300\0\300\0\u0ea0\0\300\0\u0ed0"+
    "\0\u0f00\0\300\0\u0f30\0\300\0\u0f60\0\u0f90\0\u0fc0\0\u0ff0"+
    "\0\60\0\u1020\0\u1050\0\u1080\0\u10b0\0\u10e0\0\u1110\0\u1140"+
    "\0\u1170\0\60";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[130];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\2\1\3\2\4\1\5\1\6\1\7\1\10\1\11"+
    "\1\12\1\13\1\14\1\15\1\16\1\17\1\20\1\21"+
    "\1\2\1\22\1\23\1\21\1\24\1\25\1\21\1\26"+
    "\1\27\1\30\1\31\1\32\3\21\1\33\1\34\1\35"+
    "\2\21\1\36\1\37\2\21\1\40\1\41\1\42\1\43"+
    "\1\44\1\45\1\46\62\0\1\4\61\0\1\47\1\50"+
    "\72\0\33\21\25\0\3\21\1\51\24\21\1\52\2\21"+
    "\25\0\5\21\1\53\25\21\25\0\10\21\1\54\12\21"+
    "\1\55\7\21\25\0\7\21\1\56\23\21\25\0\5\21"+
    "\1\57\25\21\25\0\2\21\1\60\30\21\37\0\2\31"+
    "\44\0\2\21\1\61\12\21\1\62\15\21\25\0\4\21"+
    "\1\63\26\21\25\0\6\21\1\64\13\21\1\65\10\21"+
    "\25\0\5\21\1\66\25\21\25\0\17\21\1\67\13\21"+
    "\25\0\10\21\1\70\22\21\25\0\17\21\1\71\13\21"+
    "\25\0\30\21\1\72\2\21\64\0\1\73\1\47\1\3"+
    "\1\4\55\47\5\50\1\74\52\50\20\0\4\21\1\75"+
    "\26\21\25\0\21\21\1\76\11\21\25\0\2\21\1\77"+
    "\30\21\25\0\11\21\1\100\21\21\25\0\2\21\1\101"+
    "\30\21\25\0\10\21\1\102\22\21\25\0\22\21\1\103"+
    "\10\21\25\0\7\21\1\104\23\21\25\0\3\21\1\105"+
    "\27\21\25\0\11\21\1\106\21\21\25\0\25\21\1\107"+
    "\5\21\25\0\2\21\1\110\30\21\25\0\32\21\1\111"+
    "\25\0\17\21\1\112\13\21\25\0\7\21\1\113\23\21"+
    "\25\0\21\21\1\114\11\21\25\0\21\21\1\115\11\21"+
    "\5\0\4\50\1\4\1\74\52\50\20\0\5\21\1\116"+
    "\25\21\25\0\11\21\1\117\21\21\25\0\4\21\1\120"+
    "\26\21\25\0\5\21\1\121\25\21\25\0\5\21\1\122"+
    "\25\21\25\0\11\21\1\123\21\21\25\0\27\21\1\124"+
    "\3\21\25\0\2\21\1\125\30\21\25\0\21\21\1\126"+
    "\11\21\25\0\2\21\1\127\30\21\25\0\10\21\1\130"+
    "\22\21\25\0\10\21\1\131\22\21\25\0\11\21\1\132"+
    "\21\21\25\0\24\21\1\133\6\21\25\0\10\21\1\134"+
    "\22\21\25\0\3\21\1\135\27\21\25\0\22\21\1\136"+
    "\10\21\25\0\5\21\1\137\25\21\25\0\2\21\1\140"+
    "\30\21\25\0\21\21\1\141\11\21\25\0\22\21\1\142"+
    "\10\21\25\0\5\21\1\143\25\21\25\0\21\21\1\144"+
    "\11\21\25\0\5\21\1\145\25\21\25\0\11\21\1\146"+
    "\21\21\6\0\3\147\14\0\33\21\25\0\5\21\1\150"+
    "\25\21\25\0\22\21\1\151\10\21\25\0\24\21\1\152"+
    "\6\21\25\0\30\21\1\153\2\21\25\0\26\21\1\154"+
    "\4\21\25\0\27\21\1\155\3\21\25\0\16\21\1\156"+
    "\14\21\25\0\26\21\1\157\4\21\25\0\7\21\1\160"+
    "\23\21\6\0\3\147\32\0\1\161\41\0\11\21\1\162"+
    "\21\21\23\0\1\163\1\0\33\21\25\0\22\21\1\164"+
    "\10\21\34\0\1\165\67\0\1\166\61\0\1\167\42\0"+
    "\1\170\75\0\1\171\37\0\1\172\53\0\1\173\101\0"+
    "\1\174\42\0\1\175\75\0\1\176\60\0\1\177\37\0"+
    "\1\200\65\0\1\201\71\0\1\202\15\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[4512];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\1\11\1\1\1\11\1\1\13\11\7\1\1\11"+
    "\11\1\4\11\2\1\1\0\22\1\1\11\1\0\52\1"+
    "\1\0\11\1\1\0\1\1\1\0\1\1\4\0\1\11"+
    "\10\0\1\11";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[130];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;
  
  /** 
   * The number of occupied positions in zzBuffer beyond zzEndRead.
   * When a lead/high surrogate has been read from the input stream
   * into the final zzBuffer position, this will have a value of 1;
   * otherwise, it will have a value of 0.
   */
  private int zzFinalHighSurrogate = 0;

  /* user code: */

  private Symbol symbol(int type, Object value) {
      //  System.out.println("We are outputting: " + type + " and " + value.toString());
        return new Symbol(type,yyline+1,yycolumn ,value);
  }

  private Symbol symbol(int type) {
       // System.out.println("We are outputting: " + type);
        return new Symbol(type,yyline+1,yycolumn);
  }


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public MiniJavaLexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x110000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 2850) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length - zzFinalHighSurrogate) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzBuffer.length*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
    }

    /* fill the buffer with new input */
    int requested = zzBuffer.length - zzEndRead;           
    int totalRead = 0;
    while (totalRead < requested) {
      int numRead = zzReader.read(zzBuffer, zzEndRead + totalRead, requested - totalRead);
      if (numRead == -1) {
        break;
      }
      totalRead += numRead;
    }

    if (totalRead > 0) {
      zzEndRead += totalRead;
      if (totalRead == requested) { /* possibly more input available */
        if (Character.isHighSurrogate(zzBuffer[zzEndRead - 1])) {
          --zzEndRead;
          zzFinalHighSurrogate = 1;
        }
      }
      return false;
    }

    // totalRead = 0: End of stream
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * Internal scan buffer is resized down to its initial length, if it has grown.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    zzFinalHighSurrogate = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
    if (zzBuffer.length > ZZ_BUFFERSIZE)
      zzBuffer = new char[ZZ_BUFFERSIZE];
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
      yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public java_cup.runtime.Symbol next_token() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      int zzCh;
      int zzCharCount;
      for (zzCurrentPosL = zzStartRead  ;
           zzCurrentPosL < zzMarkedPosL ;
           zzCurrentPosL += zzCharCount ) {
        zzCh = Character.codePointAt(zzBufferL, zzCurrentPosL, zzMarkedPosL);
        zzCharCount = Character.charCount(zzCh);
        switch (zzCh) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn += zzCharCount;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 1: 
          { return symbol(sym.UNKNOWN, yytext());
          }
        case 39: break;
        case 2: 
          { /* Ignore */
          }
        case 40: break;
        case 3: 
          { return symbol( sym.TIMES);
          }
        case 41: break;
        case 4: 
          { return symbol( sym.RIGHT_CURLY);
          }
        case 42: break;
        case 5: 
          { return symbol( sym.LEFT_CURLY);
          }
        case 43: break;
        case 6: 
          { return symbol( sym.RIGHT_PAREN, ")");
          }
        case 44: break;
        case 7: 
          { return symbol( sym.LEFT_PAREN, "(");
          }
        case 45: break;
        case 8: 
          { return symbol( sym.LEFT_B, "[");
          }
        case 46: break;
        case 9: 
          { return symbol( sym.RIGHT_B,"]");
          }
        case 47: break;
        case 10: 
          { return symbol( sym.SEMI, ";");
          }
        case 48: break;
        case 11: 
          { return symbol( sym.COMMA,yytext()+" ");
          }
        case 49: break;
        case 12: 
          { return symbol( sym.DOT, ".");
          }
        case 50: break;
        case 13: 
          { return symbol( sym.NOT );
          }
        case 51: break;
        case 14: 
          { return symbol( sym.ID, new Identifier(yytext(),yyline,yycolumn));
          }
        case 52: break;
        case 15: 
          { return symbol( sym.INTEGER_LITERAL, Integer.parseInt(yytext()));
          }
        case 53: break;
        case 16: 
          { return symbol( sym.PLUS);
          }
        case 54: break;
        case 17: 
          { return symbol( sym.MINUS);
          }
        case 55: break;
        case 18: 
          { return symbol( sym.LESS);
          }
        case 56: break;
        case 19: 
          { return symbol( sym.ASSIGN, yytext());
          }
        case 57: break;
        case 20: 
          { return symbol( sym.IF, yytext());
          }
        case 58: break;
        case 21: 
          { return symbol( sym.AND, yytext());
          }
        case 59: break;
        case 22: 
          { return symbol( sym.INT, yytext());
          }
        case 60: break;
        case 23: 
          { return symbol( sym.NEW, yytext());
          }
        case 61: break;
        case 24: 
          { return symbol( sym.TRUE);
          }
        case 62: break;
        case 25: 
          { return symbol( sym.THIS, yytext());
          }
        case 63: break;
        case 26: 
          { return symbol( sym.ELSE, "else");
          }
        case 64: break;
        case 27: 
          { return symbol( sym.FALSE);
          }
        case 65: break;
        case 28: 
          { return symbol( sym.CLASS, yytext());
          }
        case 66: break;
        case 29: 
          { return symbol( sym.WHILE, yytext());
          }
        case 67: break;
        case 30: 
          { return symbol( sym.RETURN);
          }
        case 68: break;
        case 31: 
          { return symbol( sym.LENGTH, yytext());
          }
        case 69: break;
        case 32: 
          { return symbol (sym.STATIC,yytext());
          }
        case 70: break;
        case 33: 
          { return symbol( sym.STRING);
          }
        case 71: break;
        case 34: 
          { return symbol( sym.PUBLIC, yytext());
          }
        case 72: break;
        case 35: 
          { return symbol( sym.EXTENDS," "+   yytext() + " ");
          }
        case 73: break;
        case 36: 
          { return symbol( sym.BOOLEAN, yytext());
          }
        case 74: break;
        case 37: 
          { return symbol( sym.VOID_MAIN);
          }
        case 75: break;
        case 38: 
          { return symbol( sym.PRINT, "System.out.println");
          }
        case 76: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            zzDoEOF();
              { return new java_cup.runtime.Symbol(sym.EOF); }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
