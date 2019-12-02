import java.io.File;
import java.io.FileOutputStream;

public class Main {
	public static void main(String args[]) {

//		String baseDir = "tomcat60\\java";  
//		
//		String outDir = "output60";
//		
//		Parser.parseCluster("60.rsf", baseDir, outDir);
		
		String baseDir = "tomcat85\\java";  
		
		String outDir = "output85";
		
		Parser.parseCluster("85.rsf", baseDir, outDir);
	}
}
