package lua;

public class Token {
	public String valor;
	
	public Token(String valor) {
		this.valor = valor;
	}
	
	@Override
	public String toString() {
		return this.valor;
	}
}