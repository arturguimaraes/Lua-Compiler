package lua;

import java.util.List;
import java.util.ArrayList;

public class Semantic {

	private No no;
	private List<TableSymbol> declaradas;
	
	public Semantic (No tree) {
		if (tree != null)
			this.no = tree;
		this.declaradas = new ArrayList<>();
	}
	
	public Semantic (Object tree) {
		if (tree != null)
			this.no = (No) tree;
		this.declaradas = new ArrayList<>();
	}
	
	public Semantic (No tree, List<TableSymbol> tabela) {
		if (tree != null)
			this.no = tree;
		if (tabela.size() > 0)
			this.declaradas = tabela;
		else
			this.declaradas = new ArrayList<>();
	}
	
	public Semantic (Object tree, List<TableSymbol> tabela) {
		if (tree != null)
			this.no = (No) tree;
		if (tabela.size() > 0)
			this.declaradas = tabela;
		else
			this.declaradas = new ArrayList<>();
	}
	
	public Boolean doSemanticAnalysis() {
		String token = this.no.getToken();
		
		if(!token.isEmpty()) {
			//se for local, armazena a variavel ou variaveis na tabela depois desce na recursao
			if(token.equals("local")) {
				if (!this.logicLocal())
					return false;
			}
			
			//se for function, armazena o nome da funcao e os argumentos na tabela depois desce na recursao
			else if (token.equals("function")) {
				if (!this.logicFunction())
					return false;
			}
			
			//se for palavra reservada, desce na recursao
			else if (this.reservada(token)) {
				if (!this.logicReserved())
					return false;
			}
			
			//caso contrario, so pode ser nome ou valor
			else {
				if (!this.logicNameOrNumber())
					return false;
			}
		}
		
		return true;
	}
	
	public Boolean logicLocal() {
		String token = this.no.getToken();
		TableSymbol declaredVariable;
		
		if(!this.declared(this.no.getNeto(0,0).getToken(), "variable")) {
			declaredVariable = new TableSymbol(this.no.getNeto(0,0).getToken(), "variable");
			this.declaradas.add(declaredVariable);
		}
		else {
			System.out.println("Erro Semantico: '" + this.no.getNeto(0,0).getToken() + "' ja foi declarado no escopo!");
			return false;
		}
		//se for declaracao de mais de uma variavel na mesma linha
		if (this.no.getFilhos() != null) {
			for (int i = 1; i < this.no.getFilho(0).getFilhos().size(); i++) {
				if (this.no.getNeto(0,i).getToken().equals(",")) {
					if (this.no.getNeto(0,i+1) != null) {
						if(!this.declared(this.no.getNeto(0,i+1).getToken(), "variable")) {
							declaredVariable = new TableSymbol(this.no.getNeto(0,i+1).getToken(), "variable");
							this.declaradas.add(declaredVariable);
						}
						else {
							System.out.println("Erro Semantico: '" + this.no.getNeto(0,i+1).getToken() + "' ja foi declarado no escopo!");
							return false;
						}
					}
				}
			}
		}
		
		if (this.no.getFilhos() != null) {
			//Continua a recursao para os filhos, com excessao do filho com o nome declarado
			for (No filho : this.no.getFilhos()) {
				if (!this.declared(filho.getToken()) && filho != null) {
					Semantic novaAnalise = new Semantic(filho, this.declaradas);
					//Desce recursivamente, passando a tabela atualizada
					if (!novaAnalise.doSemanticAnalysis())
						return false;
				}
			}
		}
		
		return true;
	}
	
	public Boolean logicFunction() {
		//adiciona a tabela o nome da funcao
		TableSymbol declaredVariable = new TableSymbol(this.no.getFilho(0).getToken(), "function");
		this.declaradas.add(declaredVariable);
		
		//se tiver argumentos, tem que ser adicionados tambems
		if (this.no.getFilhos() != null && this.no.getFilho(1) != null && 
			this.no.getFilho(1).getFilhos()!= null && this.no.getNeto(1, 1) != null && 
			this.no.getNeto(1, 1).getFilhos() != null) {
			for (int i = 0; i < this.no.getNeto(1, 1).getFilhos().size(); i++) {
				if (!this.no.getBisneto(1,1,i).getToken().equals(",")) {
					declaredVariable = new TableSymbol(this.no.getBisneto(1,1,i).getToken(), "argument");
					this.declaradas.add(declaredVariable);
				}
			}
		}
		
		if (this.no.getFilhos() != null) {
			//Continua a recursao para os filhos, com excessao do filho com o nome declarado
			for (No filho : this.no.getFilhos()) {
				if (!this.declared(filho.getToken()) && filho != null) {
					Semantic novaAnalise = new Semantic(filho, this.declaradas);
					//Desce recursivamente, passando a tabela atualizada
					if (!novaAnalise.doSemanticAnalysis())
						return false;
				}
				else if (filho != null && filho.getFilhos() != null) {
					Semantic novaAnalise = new Semantic(filho, this.declaradas);
					//Desce recursivamente, passando a tabela atualizada
					if (!novaAnalise.doSemanticAnalysis())
						return false;
				}
			}
		}
		
		return true;
	}

	public Boolean logicReserved() {
		String token = this.no.getToken();
		
		//tratamento especial para '#' quando for chamada de parametros
		if (token.equals("#")) {
			if (this.no.getFilhos().size() >= 4) {
				//Neste caso, o no '#' tem 4 filhos ou mais
				//e tem '(', '#' e ')' como filhos, o que
				//significa que e uma declaracao de metodo
				if (this.no.getFilho(0).getToken().equals("(") &&
					this.no.getFilho(1).getToken().equals("#") &&
					this.no.getFilho(2).getToken().equals(")")) {
						//Os nos '(' e ')' nao precisam de entrar na recursao
						//entao so e chamada a recursao para o '#'
						Semantic novaAnalise = new Semantic(this.no.getFilho(1), this.declaradas);
						if (!novaAnalise.doSemanticAnalysis())
							return false;
						
						//e entao para todos os outros os filhos
						//porem, como e um novo escopo, a tabela e esvaziada
						for(int i = 3; i < this.no.getFilhos().size(); i++ ){
							//acessa as variaveis passadas no parametro para gerar um novo escopo
							List<TableSymbol> novoEscopo = this.getNewScope(this.no.getFilho(1));
							//adiciona o nome da funcao no novo escopo para haver recursao
							novoEscopo.add(this.getFunctionInScope());
							novaAnalise = new Semantic(this.no.getFilho(i), novoEscopo);
							if (!novaAnalise.doSemanticAnalysis())
								return false;
						}
				}
			}
			//caso o tamanho seja menor que 4, eram apenas parentesis normais
			else {
				if (this.no.getFilhos() != null) {
					for (No filho : this.no.getFilhos()) {
						if (filho != null) {
							//Desce recursivamente, passando a tabela atualizada
							Semantic novaAnalise = new Semantic(filho, this.declaradas);
							if (!novaAnalise.doSemanticAnalysis())
								return false;
						}
					}
				}
			}
		}
		
		//tratamento de todos os outros tokens e palavras reservadas
		else if (this.no.getFilhos() != null) {
			for (No filho : this.no.getFilhos()) {
				if (filho != null) {
					//Desce recursivamente, passando a tabela atualizada
					Semantic novaAnalise = new Semantic(filho, this.declaradas);
					if (!novaAnalise.doSemanticAnalysis())
						return false;
				}
			}
		}
		
		return true;
	}
	
	public Boolean logicNameOrNumber() {
		String token = this.no.getToken();
		TableSymbol declaredVariable;

		//se constar na tabela de declaracoes, desce recursivamente
		if(this.declared(token)) {
			if (this.no.getFilhos() != null) {
				for (No filho : this.no.getFilhos()) {
					if (filho != null) {
						//Desce recursivamente, passando a tabela atualizada
						Semantic novaAnalise = new Semantic(filho, this.declaradas);
						if (!novaAnalise.doSemanticAnalysis())
							return false;
						
					}
				}
			}
		}
		//se nao, verifica se comeca com digito ou e uma string, e desce recursivamente
		else if(Character.isDigit(token.charAt(0)) || token.charAt(0) == '"') {
			if (this.no.getFilhos() != null) {
				for (No filho : this.no.getFilhos()) {
					if (filho != null) {
						//Desce recursivamente, passando a tabela atualizada
						Semantic novaAnalise = new Semantic(filho, this.declaradas);
						if (!novaAnalise.doSemanticAnalysis())
							return false;
						
					}
				}
			}
		}
		
		//se nao, significa que a variavel nao foi declarada dentro do escopo atual
		else {
			System.out.println("Erro Semantico: '" + token + "' nao foi declarado no escopo!");
			return false;
		}
		
		return true;
	}
	
	public Boolean declared(String token) {
		for (TableSymbol tb : this.declaradas) {
			if (tb.getId().equals(token))
				return true;
		}
		return false;
	}
	
	public Boolean declared(String token, String type) {
		for (TableSymbol tb : this.declaradas) {
			if (tb.getId().equals(token) && tb.getType().equals(type))
				return true;
		}
		return false;
	}
	
	public List<TableSymbol> getNewScope(No no) {
		List<TableSymbol> novoEscopo = new ArrayList<>();
		for(No filho : no.getFilhos()) {
			if (!filho.getToken().equals(","))
				novoEscopo.add(new TableSymbol(filho.getToken(), "argument"));
		}
		return novoEscopo;
	}
	
	public TableSymbol getFunctionInScope() {
		for(int i = this.declaradas.size()-1; i >= 0; i--) {
			TableSymbol tb = this.declaradas.get(i);
			if (tb.getType().equals("function"))
				return tb;
		}		
		return null;
	}
	
	public String getTableSymbolType(String token) {
		for (TableSymbol tb : this.declaradas) {
			if (tb.getId().equals(token))
				return tb.getType();
		}
		return null;
	}

	public Boolean reservada(String token) {
		String[] palavrasReservadas = {"and","break","do","elseif","else","end","false","for",
									   "function","local function","if","in","local","nil","not",
									   "or", "print", "repeat","then","true","until","while","return"};
		String[] tokens = {">=","<=",">","<","==","=","~=","+","-","*","/","^","%",
						   "#",";",":","(",")","{","}","[","]","...","..",".",","};
		
		for (String palavra: palavrasReservadas) {
			if(token.equals(palavra))
				return true;
		}    	
		
		for (String palavra: tokens) {
			if(token.equals(palavra))
				return true;
		}

		return false;
	}	
}