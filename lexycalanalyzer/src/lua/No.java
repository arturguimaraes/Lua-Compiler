package lua;

import java.util.List;
import java.util.ArrayList;
import java_cup.runtime.*;
import java_cup.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class No {
    private Token token;
    private List<No> filhos;

    public No() {
    	this.token = null;
    	this.filhos = new ArrayList<>();
    }

    public No(Token token){
        this.token = token;
        this.filhos = new ArrayList<>();
    }	
    
    public String getToken() {
    	return this.token.toString();
    }

    public void addFilho(Token token){
        filhos.add(new No(token));
    }

    public void addFilho(No no){
        filhos.add(no);
    }
    
    public List<No> getFilhos() {
    	if(this.filhos.size() > 0)
    		return this.filhos;
    	else
    		return null;
    }
    
    public No getFilho(int position) {
    	if(this.filhos.size() > position) {
    		if(this.filhos.get(position) != null)
    			return this.filhos.get(position);
    	}
    	return null;
    }	
    
    public No getNeto(int posFilho, int posNeto) {
    	if(this.filhos.size() > posFilho) {
    		if(this.filhos.get(posFilho).filhos.size() > posNeto) {
    			if(this.filhos.get(posFilho).filhos.get(posNeto) != null)
        			return this.filhos.get(posFilho).filhos.get(posNeto);
    		}
    	}
    	return null;
    }
    
    public No getBisneto(int posFilho, int posNeto, int posBisneto) {
    	if(this.filhos.size() > posFilho) {
    		if(this.filhos.get(posFilho).filhos.size() > posNeto) {
    			if(this.filhos.get(posFilho).filhos.get(posNeto).filhos.size() > posBisneto) {
    				if(this.filhos.get(posFilho).filhos.get(posNeto).filhos.get(posBisneto) != null)
    					return this.filhos.get(posFilho).filhos.get(posNeto).filhos.get(posBisneto);
    			}
    		}
    	}
    	return null;
    }

    public String escreve(Token token) throws IOException{	  	
		switch(token.valor)
		{
		case "[":
			return "[";
		case "]":
			return "]";
		case "(":
			return "(";
		case ")":
			return ")";
		case "and":
			return "and";
		case "break":
			return "break\n";
		case "end":
			return "}";
		case "do":
			return "do {\n";
		case "elseif":
			return "} else if {\n";
		case "else":
			return "} else {\n";
		case "false":
			return "false";
		case "for":
			return "for ";
		case "function":
			return "public void function() {\n";
		case "local":
			return "private ";
		case "local function":
			return "private void function () { \n";
		case "if":
			return "if ";
		case "nil":
			return "null";
		case "then":
			return "{ \n";
		case "#":
			return "";
		
		}
		return token.valor+" ";
	}
    
    @Override
    public String toString() {
        if (this.token != null)
			try {
				imprime();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        return "";
    }
    
    public void imprime() throws IOException {
    	this.imprime("", false);
    }
    
    private void imprime(String prefixo, boolean folha) throws IOException {
        if(this.token != null && this.token.valor != null) {
        	String tabulacao = "", espaco = "";
            
            if(folha) {
            	tabulacao = " -- ";
            	espaco ="    ";
            } else{
            	tabulacao = "|-- ";
            	espaco = "|   ";
            }    
            
            System.out.println(prefixo + tabulacao + this.token.valor);
            
    		FileWriter fw = null;
    		
            fw = new FileWriter("lua/GeneratedCode.java", true);
            String token_java = this.escreve(token);
            fw.append(token_java);
			
			if (fw != null){
				fw.close();
        	};
            
            
            for (int i = 0; i < filhos.size() - 1; i++) {
                if(filhos.get(i) != null)
                	filhos.get(i).imprime(prefixo + espaco, false);
            }
            
            if (filhos.size() > 0 && filhos.get(filhos.size() - 1) != null)
                filhos.get(filhos.size() - 1).imprime(prefixo + espaco, true);
        }
    }
}
