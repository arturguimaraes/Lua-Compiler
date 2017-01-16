package lua;

public class TableSymbol {
	private String id;
	private String type;
	
	public TableSymbol() {
		this.id = "";
		this.type = "";
	}
	
	public TableSymbol(String id) {
		this.id = id;
		this.type = "unknown";
	}
	
	public TableSymbol(String id, String type) {
		this.id = id;
		this.type = type;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getType() {
		return this.type;
	}
	
	@Override
	public String toString() {
		return "(" + this.type + ") " + this.id;
	}
}