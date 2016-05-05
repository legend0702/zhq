package cn.zhuhongqing.utils;

import java.nio.file.FileSystems;

/**
 * Pool of <code>String</code> constants to prevent repeating of hard-coded
 * <code>String</code> literals in the code. Due to fact that these are
 * <code>public static final</code> they will be inlined by java compiler and
 * reference to this class will be dropped. There is <b>no</b> performance gain
 * of using this pool. Read:
 * http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#3.10.5
 * <ul>
 * <li>Literal strings within the same class in the same package represent
 * references to the same <code>String</code> object.</li>
 * <li>Literal strings within different classes in the same package represent
 * references to the same <code>String</code> object.</li>
 * <li>Literal strings within different classes in different packages likewise
 * represent references to the same <code>String</code> object.</li>
 * <li>Strings computed by constant expressions are computed at compile time and
 * then treated as if they were literals.</li>
 * <li>Strings computed by concatenation at run time are newly created and
 * therefore distinct.</li>
 * </ul>
 */

public interface StringPool {

	final String AMPERSAND = "&";
	final String AND = "and";
	final String AT = "@";
	final String ASTERISK = "*";
	final String STAR = ASTERISK;
	final String BACK_SLASH = "\\";
	final String COLON = ":";
	final String COMMA = ",";
	final String DASH = "-";
	final String DOLLAR = "$";
	final String DOT = ".";
	final String DOTDOT = "..";
	final String DOT_CLASS = ".class";
	final String DOT_JAVA = ".java";
	final String EMPTY = "";
	final String EQUALS = "=";
	final String FALSE = "false";
	final String SLASH = "/";
	final String HASH = "#";
	final String HAT = "^";
	final String LEFT_BRACE = "{";
	final String LEFT_BRACKET = "(";
	final String LEFT_CHEV = "<";
	final String NEWLINE = "\n";
	final String N = "n";
	final String NO = "no";
	final String NULL = "null";
	final String OFF = "off";
	final String ON = "on";
	final String PERCENT = "%";
	final String PIPE = "|";
	final String PLUS = "+";
	final String MINUS = "-";
	final String QUESTION_MARK = "?";
	final String EXCLAMATION_MARK = "!";
	final String QUOTE = "\"";
	final String RETURN = "\r";
	final String TAB = "\t";
	final String RIGHT_BRACE = "}";
	final String RIGHT_BRACKET = ")";
	final String RIGHT_CHEV = ">";
	final String SEMICOLON = ";";
	final String SINGLE_QUOTE = "'";
	final String SPACE = " ";
	final String LEFT_SQ_BRACKET = "[";
	final String RIGHT_SQ_BRACKET = "]";
	final String TRUE = "true";
	final String UNDERSCORE = "_";
	final String UTF_8 = "UTF-8";
	final String US_ASCII = "US-ASCII";
	final String ISO_8859_1 = "ISO-8859-1";
	final String Y = "y";
	final String YES = "yes";
	final String ONE = "1";
	final String ZERO = "0";
	final String DOLLAR_LEFT_BRACE = "${";
	final String CRLF = "\r\n";
	final String PROTOCOL_START = "://";
	final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();
	// final String FILE_SEPARATOR = SLASH;

	final String HTML_NBSP = "&nbsp;";
	final String HTML_AMP = "&amp";
	final String HTML_QUOTE = "&quot;";
	final String HTML_LT = "&lt;";
	final String HTML_GT = "&gt;";

	// ---------------------------------------------------------------- array

	final String[] EMPTY_ARRAY = new String[0];
}