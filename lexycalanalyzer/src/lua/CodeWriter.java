package lua;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CodeWriter {
	
	public File f;
	public FileWriter fw;
	
	public CodeWriter() throws IOException {
		this.f = new File("lua/GeneratedCode.java");
		this.fw = new FileWriter(f);		
		fw.append("package lua;\n");
		fw.append("public class GeneratedCode {\n");		
		fw.append("	public static void main(String args[]){\n\n");
		fw.close();
		
	}
	
	public void fecha() throws IOException{
		FileWriter fw = null;
		
        fw = new FileWriter("lua/GeneratedCode.java", true);
        fw.append("}");
		
		if (fw != null){
			fw.close();
    	};
	}

}
