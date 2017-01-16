#!/bin/bash

# Navegar para pasta lua:
cd src/lua

# Removendo arquivos gerados em outra execucao
rm -f Lexer.java
rm -f Parser.java
rm -f Sym.java

# Para criacao do Lexer.java:
java -jar ../../jflex-1.6.1.jar Lexer.lex 

# Para criacao do Parser.java e Sym.java:
java -jar ../../java-cup-11a.jar -parser Parser -expect 11 -symbols Sym Parser.cup


# EXECUCAO POR LINHA DE COMANDO:

# (MAC)
cd ..
javac -classpath ../jflex-1.6.1.jar:../java-cup-11a.jar lua/*.java
java -cp .:../jflex-1.6.1.jar:../java-cup-11a.jar lua.Main

