package lua;

import java_cup.runtime.*;
import java_cup.*;
import java.util.*;

%%

%{

	private Symbol symbol(int symbol_code, int yyline, int yycolumn) {
		return new Symbol(symbol_code, yyline, yycolumn);
	}
	
	private Symbol symbol(int symbol_code, int yyline, int yycolumn, Object value) {
		return new Symbol(symbol_code, yyline, yycolumn, value);
	}
	
	private Token createToken(String valor) {
	    Token token = new Token(valor);
	    return token;
	}

%}

%cup
%public
%class Lexer
%line
%column

comeca_comentario = "--[["
conteudo_comentario = ([^-]|[^\[{2}])*
termina_comentario = "--]]"

comentario_em_bloco = {comeca_comentario}{conteudo_comentario}{termina_comentario}
comentario_curto = -{2}[^\[][^\]][^\n]*
expoente = [-+]?[0-9]*\.?[0-9]+([eE][-+]?[0-9]+)?
float = [0-9]+\.[0-9]*
inteiro = 0|[1-9][0-9]*
brancos = [\n| |\t|\r|]
name = [_|a-z|A-Z][a-z|A-Z|0-9|_]*
stringLua = \"(([^\\]+)|(\\.))*\"
number = {float} | {inteiro} | {expoente}
comentario = {comentario_em_bloco} | {comentario_curto}

//comentario number string id
%%

<YYINITIAL> {

{comentario_curto} 		{ System.out.println("COMENTARIO CURTO"); }
{comentario_em_bloco} 	{ System.out.println("COMENTARIO LONGO"); }
"and" 					{ System.out.println("OPERADOR-E"); return new Symbol(Sym.OPERADOR_E, createToken(yytext())); }
"break" 				{ System.out.println("QUEBRA DE LACO"); return new Symbol(Sym.QUEBRA_LACO, createToken(yytext())); }
"do" 					{ System.out.println("LACO FACA"); return new Symbol(Sym.LACO_FACA, createToken(yytext())); }
"elseif" 				{ System.out.println("senao SE"); return new Symbol(Sym.SENAO_SE, createToken(yytext())); }
"else" 					{ System.out.println("senao"); return new Symbol(Sym.SENAO, createToken(yytext())); }
"end" 					{ System.out.println("FIM"); return new Symbol(Sym.FIM_DO_LACO, createToken(yytext())); }
"false" 				{ System.out.println("FALSO"); return new Symbol(Sym.VALOR_FALSO, createToken(yytext())); }
"for" 					{ System.out.println("LACO PARA"); return new Symbol(Sym.LACO_PARA, createToken(yytext())); }
"function" 				{ System.out.println("FUNCAO"); return new Symbol(Sym.FUNCAO, createToken(yytext())); }
"local function" 		{ System.out.println("FUNCAO LOCAL"); return new Symbol (Sym.FUNCAO_LOCAL, createToken(yytext())); } 
"if" 					{ System.out.println("SE"); return new Symbol(Sym.SE_CONDICIONAL, createToken(yytext())); }
"in" 					{ System.out.println("DENTRO"); return new Symbol(Sym.DENTRO, createToken(yytext())); }
"local" 				{ System.out.println("DEFINICAO_LOCAL"); return new Symbol(Sym.DEFINICAO_LOCAL, createToken(yytext())); }
"nil"  					{ System.out.println("NUL"); return new Symbol(Sym.NULO, createToken(yytext())); }
"not"  					{ System.out.println("NEGACAO"); return new Symbol(Sym.NEGACAO, createToken(yytext())); }
"or"  					{ System.out.println("OPERADOR_OU"); return new Symbol(Sym.OPERADOR_OU, createToken(yytext())); }
"repeat"  				{ System.out.println("REPETIR"); return new Symbol(Sym.REPETICAO, createToken(yytext())); }
"then"  				{ System.out.println("ENTAO"); return new Symbol(Sym.ENTAO, createToken(yytext())); }
"true" 					{ System.out.println("VERDADEIRO"); return new Symbol(Sym.VALOR_VERDADEIRO, createToken(yytext())); }
"until"  				{ System.out.println("ATE"); return new Symbol(Sym.ATE_LIMITE_LACO, createToken(yytext())); }
"while"  				{ System.out.println("ENQUANTO"); return new Symbol(Sym.LACO_ENQUANTO, createToken(yytext())); }
"return"  				{ System.out.println("RETORNE"); return new Symbol(Sym.RETORNO, createToken(yytext())); }

">=" 					{ System.out.println(">="); return new Symbol(Sym.MAIOR_IGUAL_QUE, createToken(yytext())); }
"<=" 					{ System.out.println("<="); return new Symbol(Sym.MENOR_IGUAL_QUE, createToken(yytext())); }
">" 					{ System.out.println(">"); return new Symbol(Sym.MAIOR_QUE, createToken(yytext())); }
"<" 					{ System.out.println("<"); return new Symbol(Sym.MENOR_QUE, createToken(yytext())); }
"==" 					{ System.out.println("=="); return new Symbol(Sym.IGUAL_COMPARATIVO, createToken(yytext())); }
"=" 					{ System.out.println("="); return new Symbol(Sym.ATRIBUICAO, createToken(yytext())); }
"~=" 					{ System.out.println("~="); return new Symbol(Sym.DIFERENTE, createToken(yytext())); }
"+"  					{ System.out.println("+"); return new Symbol(Sym.SOMA, createToken(yytext())); }
"-" 					{ System.out.println("-"); return new Symbol(Sym.SUBTRACAO, createToken(yytext())); }
"*" 					{ System.out.println("*"); return new Symbol(Sym.MULTIPLICACAO, createToken(yytext())); }
"/" 					{ System.out.println("/"); return new Symbol(Sym.DIVISAO, createToken(yytext())); }
"^" 					{ System.out.println("^"); return new Symbol(Sym.EXPONENCIACAO, createToken(yytext())); }
"%" 					{ System.out.println("%"); return new Symbol(Sym.RESTO, createToken(yytext())); }
"#" 					{ System.out.println("#"); return new Symbol(Sym.TAMANHO, createToken(yytext())); }
";" 					{ System.out.println("PONTO_E_VIRGULA"); return new Symbol(Sym.PONTO_E_VIRGULA, createToken(yytext())); }
":" 					{ System.out.println(":"); return new Symbol(Sym.DOIS_PONTOS, createToken(yytext())); }
"(" 					{ System.out.println("("); return new Symbol(Sym.ABRE_PARENTESIS, createToken(yytext())); }
")" 					{ System.out.println(")"); return new Symbol(Sym.FECHA_PARENTESIS, createToken(yytext())); }
"{" 					{ System.out.println("{"); return new Symbol(Sym.ABRE_CHAVES, createToken(yytext())); }
"}"  					{ System.out.println("}"); return new Symbol(Sym.FECHA_CHAVES, createToken(yytext())); }
"["  					{ System.out.println("["); return new Symbol(Sym.ABRE_COLCHETES, createToken(yytext())); }
"]" 					{ System.out.println("]"); return new Symbol(Sym.FECHA_COLCHETES, createToken(yytext())); }
"..." 					{ System.out.println("tres pontos"); return new Symbol(Sym.EXPRESSAO_VARARG, createToken(yytext())); }
".." 					{ System.out.println("dois pontos"); return new Symbol(Sym.CONCATENAR_STRING, createToken(yytext())); }
"." 					{ System.out.println("ponto"); return new Symbol(Sym.PONTO, createToken(yytext())); }
"," 					{ System.out.println("VIRGULA"); return new Symbol(Sym.VIRGULA, createToken(yytext())); }

{number} 				{ System.out.println("number"); return new Symbol(Sym.NUMERO, createToken(yytext())); }
{brancos} 				{ }
{stringLua} 			{ System.out.println("StringLua"); return new Symbol(Sym.STRING_LUA, createToken(yytext())); }
{name} 					{ System.out.println("nome"); return new Symbol(Sym.NOME, createToken(yytext())); }

}
. { throw new RuntimeException("Caractere invalido " + yytext() + " na linha " + yyline + ", coluna " +yycolumn); }