import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class Parser {
	
	public static void parseCluster(String filename, String baseDir, String outDir) {
		BufferedReader reader;
		Vector<String> funcs = new Vector<String>();
		String lastCluster = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();
			while (line != null) {

				
				String clusterName = line.split("\\s+")[1];
				String func = line.split("\\s+")[2];
				
				if (lastCluster != null && !clusterName.equals(lastCluster)) {
					Parser.printCluster(baseDir, outDir, funcs, lastCluster);
					funcs = new Vector<String>();
				}
				
				funcs.add(func);
				lastCluster = clusterName;
	
				line = reader.readLine();
			}
			Parser.printCluster(baseDir, outDir, funcs, lastCluster);
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void printCluster(String baseDir, String outDir, Vector<String> funcs, String clusterName) {
		FileOutputStream fop = null;
		File file;
		try {
			file = new File(outDir + '\\' + clusterName + ".txt");
			fop = new FileOutputStream(file);

			if (!file.exists()) {
				file.createNewFile();
			}
			
			String num = new String(funcs.size() + "\n");
			fop.write(num.getBytes());
			fop.flush();
			
			for (String f : funcs) {
				f = f.replace("()", "");
				String[] temp = f.split("\\.");
				String func = temp[temp.length - 1];
				String dir = baseDir + '\\' + f.replace('.' + func, "").replace('.', '\\') + ".java";
				
				System.out.println(func);
				System.out.println(dir);
				fop.write("-----CSCI578-----\n".getBytes());
				fop.flush();
				Parser.printFunction(dir, func, fop);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("");
		}
		
	}
	private static void printFunction(String filename, String func, FileOutputStream fop) throws IOException {
		BufferedReader reader;
		
		
		Vector<String> members = new Vector<String>();
		
		
		String line = new String();
		boolean inQuote = false;
		boolean inComment1 = false; // 
		boolean inComment2 = false; /**/
		
		String lump = new String();
		
		int depth = 0;
		try {
			reader = new BufferedReader(new FileReader(filename));
			int r;
			while ((r = reader.read()) != -1) {
	            char ch = (char) r;
	            line += ch;
	            
	            if (depth >= 1) {
	            	lump += ch;
	            }
	            
	            if (ch == '\n')  {
//	            	System.out.print(depth);
	            	if (inComment1 || inComment2) {
//	            		System.out.print("------");
	            	}
//	            	System.out.print(line);
	            	line = new String();
	            	inComment1 = false;
	            }
	            else if (ch == '\"') {
	            	inQuote = !inQuote;
	            }
	            else if (!inQuote && line.endsWith("//")) {
	            	inComment1 = true;
	            }
	            else if (!inQuote && line.endsWith("/*")) {
	            	inComment2 = true;
	            }
	            else if (!inQuote && line.endsWith("*/")) {
	            	inComment2 = false;
	            }
	            else if (ch == '{' && !inQuote && !inComment1 && !inComment2) {
	            	depth += 1;
//	            	System.out.print(depth);
	            }
	            else if (ch == '}' && !inQuote && !inComment1 && !inComment2) {
	            	depth -= 1;
//	            	System.out.print(depth);
	            	if (depth == 1) {
	            		lump += '\n';
	            		members.add(lump);
//	            		System.out.println("**********************");
//	            		System.out.print(lump);
//	            		System.out.println("_____________________");
	            		lump = new String();
	            	}
	            }
	            else if (ch == ';' && !inQuote && !inComment1 && !inComment2) {
	            	if (depth == 1) {
	            		lump += '\n';
	            		members.add(lump);
//	            		System.out.println("**********************");
//	            		System.out.print(lump);
//	            		System.out.println("_____________________");
	            		lump = new String();
	            	}
	            }
	        }
			
			reader.close();
		} catch (IOException e) {
			System.out.println("File does not exist!");
//			e.printStackTrace();
		}
		boolean found = false;
		for (String m : members) {
			int pos = m.indexOf('{');
			if (pos == -1) {
				if (m.contains(func+"(")) {
					fop.write(m.getBytes());
					fop.flush();
					found = true;
				}
			}
			else {
				if (m.substring(0,pos).contains(func+"(")) {
					fop.write(m.getBytes());
					fop.flush();
					found = true;
				}
			}
		}
		if (found) {
			System.out.println("found");
		}
		else {
			System.out.println("missing");
		}
//		if (isInClass) 
//			System.out.print(s);
	}

	
//	private static void printFunction(String filename, String func, FileOutputStream fop) throws IOException {
//		BufferedReader reader;
//		boolean isInClass = false;
//		Vector<String> members = new Vector<String>();
//		Vector<String> declarations = new Vector<String>();
//		String s = new String();
//		long count = 0;
//		String declaration = null;
//		try {
//			reader = new BufferedReader(new FileReader(filename));
//			String line = reader.readLine();
//			while (line != null) {
//				if (!isInClass) {
//					if (line.trim().startsWith("public") && (line.contains("class") 
//							|| line.contains("interface")))
//						isInClass = true;
//				}
//				else {
//					s += line + '\n';
//					// escape comments
//					if (!line.trim().startsWith("//") && !line.trim().startsWith("*")
//							&& !line.trim().startsWith("/*")) {
//						if (line.trim().startsWith("public")
//								|| line.trim().startsWith("private")
//								|| line.trim().startsWith("protected")) {
//							if (declaration == null)
//								declaration = new String(line.trim());
//						}
//						// case 1
//						long left = line.chars().filter(ch -> ch == '{').count();
//						long right = line.chars().filter(ch -> ch == '}').count();
//						if (left + right > 0) {
//							count = count + left - right;
//							if (count == 0) {
//								members.add(s);
//								declarations.add(declaration);
//								declaration = null;
//								s = new String();
//								count = 0;
//							}
//						}
//						// case 2
//						if (count == 0) {
//							if (line.trim().startsWith("protected")
//									&& line.trim().endsWith(";")) {
//								members.add(s);
//								declarations.add(declaration);
//								declaration = null;
//								s = new String();
//							}
//							if (line.trim().startsWith("private")
//									&& line.trim().endsWith(";")) {
//								members.add(s);
//								declarations.add(declaration);
//								declaration = null;
//								s = new String();
//							}
//						}				
//					}
//				}
//				
//				line = reader.readLine();
//			}
//			reader.close();
//		} catch (IOException e) {
//			System.out.println("File does not exist!");
////			e.printStackTrace();
//		}
//		boolean found = false;
//		for (int i = 0; i < members.size(); i++) {
//			System.out.println("--------------------------------------");
//			System.out.print(declarations.get(i) != null ? declarations.get(i) : "unknown");
//			System.out.print(members.get(i));
//			if (declarations.get(i) != null && declarations.get(i).contains(func)) {
////				System.out.print("!");
//				fop.write(members.get(i).getBytes());
//				fop.flush();
//				found = true;
//			}
//		}
//		if (found) {
//			System.out.println("found");
//		}
//		else {
//			System.out.println("missing");
//		}
////		if (isInClass) 
////			System.out.print(s);
//	}
}
