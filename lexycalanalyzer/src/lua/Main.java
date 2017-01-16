package lua;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        
    	String rootPath = Paths.get("", args).toAbsolutePath(). toString();
        String subPath = "/lua";
        String sourceCode = rootPath + subPath + "/program.lua";
 
        try {
        	FileReader fr = new FileReader(sourceCode);
        	Lexer l = new Lexer(fr);
        	Parser p = new Parser(l);
			CodeWriter code = new CodeWriter();
            Object result = p.parse().value;
            code.fecha();
            System.out.println("Compilacao concluida com sucesso!");
            
            Semantic analiseSemantica = new Semantic(result);
            if (analiseSemantica.doSemanticAnalysis())
            	System.out.println("A Analise Semantica ocorreu corretamente!");
            else
            	System.out.println("Ocorreram um ou mais erros na Analise Semantica.");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
