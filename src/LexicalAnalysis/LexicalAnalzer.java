package LexicalAnalysis;

class ReservedWords {
	public String name;
	public TokenType tokenType;

	public ReservedWords(String name, TokenType tokenType) {
		super();
		this.name = name;
		this.tokenType = tokenType;
	}

}

/*
 * FALSE, NONE, TRUE, AND, AS, ASSERT, BREAK, CLASS, CONTINUE, DEF, DEL, ELIF,
 * ELSE, EXCEPT, FINALLY, FOR, FROM, GLOBAL, IF, IMPORT, IN, IS, LAMBDA,
 * NONLOCAL, NOT, OR, PASS, RAISE, RETURN, TRY, WHILE, WITH, YIELD
 */
public class LexicalAnalzer {
	ReservedWords[] reservedWords = {
			new ReservedWords("auto", TokenType.AUTO), new ReservedWords("break", TokenType.BREAK),
			new ReservedWords("case", TokenType.CASE), new ReservedWords("char", TokenType.CHAR),
			new ReservedWords("constcontinue", TokenType.CONSTCONTINUE),
			new ReservedWords("default", TokenType.DEFAULT), new ReservedWords("do", TokenType.DO),
			new ReservedWords("double", TokenType.DOUBLE), new ReservedWords("else", TokenType.ELSE),
			new ReservedWords("enum", TokenType.ENUM), new ReservedWords("extern", TokenType.EXTERN),
			new ReservedWords("float", TokenType.FLOAT), new ReservedWords("for", TokenType.FOR),
			new ReservedWords("goto", TokenType.GOTO), new ReservedWords("if", TokenType.IF),
			new ReservedWords("int", TokenType.INT), new ReservedWords("long", TokenType.LONG),
			new ReservedWords("redister", TokenType.REDISTER), new ReservedWords("return", TokenType.RETURN),
			new ReservedWords("short", TokenType.SHORT), new ReservedWords("signed", TokenType.SIGNED),
			new ReservedWords("sizeof", TokenType.SIZEOF), new ReservedWords("static", TokenType.STATIC),
			new ReservedWords("struct", TokenType.STRUCT), new ReservedWords("switch", TokenType.SWITCH),
			new ReservedWords("typedef", TokenType.TYPEDEF), new ReservedWords("union", TokenType.UNION),
			new ReservedWords("unsigned", TokenType.UNSIGNED), new ReservedWords("void", TokenType.VOID),
			new ReservedWords("volatile", TokenType.VOLATILE), new ReservedWords("while", TokenType.WHILE), };

	public TokenType getToken(String codes) {
		TokenType currentToken = TokenType.VOID;

		boolean save;
		int codesIndex = 0;
		while (codesIndex < codes.length()) {
			StateType state = StateType.START;
			StringBuffer tokenString = new StringBuffer();
			while (state != StateType.DONE && codesIndex < codes.length()) {
				char c = codes.charAt(codesIndex);
				save = true;
				switch (state) {
				case START:
					if (Character.isDigit(c))
						state = StateType.INNUM;
					else if (Character.isAlphabetic(c))
						state = StateType.INID;
					else if ((c == ' ') || (c == '\t') || (c == '\n'))
						save = false;
					else if (c == '+' && codesIndex + 1 < codes.length() && codes.charAt(codesIndex + 1) == '+') {
						currentToken = TokenType.INC;
						state = StateType.DONE;
						codesIndex++;
					}
					else if (c == '-' && codesIndex + 1 < codes.length() && codes.charAt(codesIndex + 1) == '-') {
						currentToken = TokenType.DEC;
						state = StateType.DONE;
						codesIndex++;
					} 
					else if (c == '>' && codesIndex + 1 < codes.length() && codes.charAt(codesIndex + 1) == '=') {
						currentToken = TokenType.RTEQ;
						state = StateType.DONE;
						codesIndex++;
					} 
					else if (c == '<' && codesIndex + 1 < codes.length() && codes.charAt(codesIndex + 1) == '=') {
						currentToken = TokenType.LTEQ;
						state = StateType.DONE;
						codesIndex++;
					}
					else if (c == '&' && codesIndex + 1 < codes.length() && codes.charAt(codesIndex + 1) == '&') {
						currentToken = TokenType.AND;
						state = StateType.DONE;
						codesIndex++;
					}
					else if (c == '|' && codesIndex + 1 < codes.length() && codes.charAt(codesIndex + 1) == '|') {
						currentToken = TokenType.OR;
						state = StateType.DONE;
						codesIndex++;
					}
					else if (c == '!' && codesIndex + 1 < codes.length() && codes.charAt(codesIndex + 1) == '=') {
						currentToken = TokenType.NOTEQ;
						state = StateType.DONE;
						codesIndex++;
					}
					else {
						
						state = StateType.DONE;
						switch (c) {
						case '{':
							currentToken = TokenType.BLPAREN;
							break;
						case '}':
							currentToken = TokenType.BRPAREN;
							break;
						case ',':
							currentToken = TokenType.COMMA;
							break;
						case ':':
							currentToken = TokenType.ASSIGN;
							break;
						case '<':
							currentToken = TokenType.LT;
							break;
						case '+':
							currentToken = TokenType.PLUS;
							break;
						case '-':
							currentToken = TokenType.MINUS;
							break;
						case '*':
							currentToken = TokenType.TIMES;
							break;
						case '/':
							currentToken = TokenType.OVER;
							break;
						case '(':
							currentToken = TokenType.LPAREN;
							break;
						case ')':
							currentToken = TokenType.RPAREN;
							break;
						case ';':
							currentToken = TokenType.SEMI;
							break;
						case '=':
							currentToken = TokenType.EQ;
							break;
						default:
							currentToken = TokenType.ERROR;
							break;
						}
					}
					break;
				case INCOMMENT:
					save = false;
					if (codesIndex >= codes.length()) {
						state = StateType.DONE;
						currentToken = TokenType.ENDFILE;
					} else if (c == '}')
						state = StateType.START;
					break;
				case INNUM:
					if (!Character.isDigit(c)) {
						codesIndex--;
						save = false;
						state = StateType.DONE;
						currentToken = TokenType.NUM;
					} else if (codesIndex == codes.length() - 1) {
						save = true;
						state = StateType.DONE;
						currentToken = TokenType.NUM;
					}
					break;
				case INID:
					if (!Character.isAlphabetic(c)) {
						codesIndex--;
						save = false;
						state = StateType.DONE;
						currentToken = TokenType.ID;
					}
					break;
				case DONE:
					break;
				default:
					break;

				}
				codesIndex++;
				if ((save) && (state != StateType.START)) {
					tokenString.append(c);
					// System.out.print(tokenString.toString());
					// System.out.print(currentToken);
				}

				if (state == StateType.DONE) {
					if (currentToken == TokenType.ID)
						currentToken = reservedLookup(tokenString.toString());
					printToken(currentToken, tokenString.toString());
				}
				// System.out.print(tokenString.toString());

			}
		}

		return currentToken;

	}

	void printToken(TokenType token, String tokenString) {
		switch (token) {
		case AUTO:
		case BREAK:
		case CASE:
		case CHAR:
		case CONSTCONTINUE:
		case DEFAULT:
		case DO:
		case DOUBLE:
		case ELSE:
		case ENUM:
		case EXTERN:
		case FLOAT:
		case FOR:
		case GOTO:
		case IF:
		case INT:
		case LONG:
		case REDISTER:
		case RETURN:
		case SHORT:
		case SIGNED:
		case SIZEOF:
		case STATIC:
		case STRUCT:
		case SWITCH:
		case TYPEDEF:
		case UNION:
		case UNSIGNED:
		case VOID:
		case VOLATILE:
		case WHILE:
			System.out.print("<" + tokenString.toUpperCase() + ", - >   ");
			break;
		case LTEQ:
			System.out.print("< <= , ->   ");
			break;
		case RTEQ:
			System.out.print("< <= , ->   ");
			break;
		case NOTEQ:
			System.out.print("< != , ->   ");
			break;
		case AND:
			System.out.print("< && , ->   ");
			break;
		case OR:
			System.out.print("< || , ->   ");
			break;
		case INC:
			System.out.print("< ++ , ->   ");
			break;
		case DEC:
			System.out.print("< -- , ->   ");
			break;
		case BLPAREN:
			System.out.print("< { , ->   ");
			break;
		case BRPAREN:
			System.out.print("< } , ->   ");
			break;
		case COMMA:
			System.out.print("< , , ->   ");
			break;
		case ASSIGN:
			System.out.print("< : , ->   ");
			break;
		case LT:
			System.out.print("< < , ->   ");
			break;
		case EQ:
			System.out.print("< = , ->   ");
			break;
		case LPAREN:
			System.out.print("< ( , ->   ");
			break;
		case RPAREN:
			System.out.print("< ) , ->   ");
			break;
		case SEMI:
			System.out.print("< ; , ->   ");
			break;
		case PLUS:
			System.out.print("< + , ->   ");
			break;
		case MINUS:
			System.out.print("< - , ->   ");
			break;

		case NUM:
			System.out.print("<NUM" + "," + tokenString + ">   ");

			break;
		case ID:
			System.out.print("<ID" + "," + tokenString + ">   ");
			break;

		default:
			System.out.print("<UNKNOW" + "," + token + ">   ");
		}
	}

	TokenType reservedLookup(String tokenString) {
		int i;
		for (i = 0; i < reservedWords.length; i++)
			if (tokenString.equals(reservedWords[i].name))
				return reservedWords[i].tokenType;
		return TokenType.ID;

	}
}
