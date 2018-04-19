package LexicalAnalysis;

import utils.MyFileReader;

public class UnitTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String[] codes=MyFileReader.getFile("d:/pythonfile.txt");
		LexicalAnalzer lexicalAnalzer=new LexicalAnalzer();
		for (String string : codes) {
			System.out.println(string);
			System.out.println();
			lexicalAnalzer.getToken(string);
			System.out.println();
			System.out.println("-------------------------------------------------------------");
		}
		
	}

}
