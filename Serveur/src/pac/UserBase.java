package pac;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class UserBase {
	ArrayList<String[]> liste;
	BufferedReader in;
	PrintWriter out;
	String file;
	
	public UserBase(String file){
		try {
			this.file = file;
			liste = new ArrayList<>();
			in = new BufferedReader(new FileReader(file));
			out = new PrintWriter(new FileWriter(file, true));
			String str;
			while ((str = in.readLine()) != null) {
				liste.add(str.split(":"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<String[]> getListe() {
		return liste;
	}
	
	public boolean exist(String nam,String Pass) {
		for (String[] strings : liste) {
			if(strings[0].equals(nam)&& strings[1].equals(Pass)) return true;
		}
		return false;
	}
	
	public boolean exist(String nam) {
		for (String[] strings : liste) {
			if(strings[0].equals(nam)) return true;
		}
		return false;
	}
	
	public int getLevel(String nam) throws Exception {
		for (String[] strings : liste) {
			if(strings[0].equals(nam)) return Integer.parseInt(strings[2]);
		}throw new Exception("User not find");
	}
	
	public void add(String nam,String pass,String Level) throws Exception {
		if(exist(nam))throw new Exception("User already exist");
		out.println(nam+":"+pass+":"+Level);
		out.flush();
	}
	
	public void remouv(String nam) throws Exception {
		if(exist(nam)) {
			File tmp = new File("tmp.txt");
			PrintWriter tmpOut = new PrintWriter(tmp);
			String s ;
			while ((s = in.readLine()) != null) {
				if(!s.equals(nam)) {
					tmpOut.println(s);
				}
			}
			tmpOut.close();
			in.close();
			out.close();			
			tmp.renameTo(new File(file));
			in = new BufferedReader(new FileReader(tmp));
			out = new PrintWriter(tmp);
		}else throw new Exception("User not find");
	}
	
}
