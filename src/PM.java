import java.io.*;
public class PM {
    public static void main(String[] args)throws Exception 
    {
      File f=new File("password.txt");
      f.createNewFile();
      BufferedWriter fw=new BufferedWriter(new FileWriter(f));
      int j=0;
     // String s="";
      while(j<4)
      {
          int i=(int)(Math.ceil((Math.random()*10)));  
          fw.write(Integer.toString(i));
          j++;      
         
      }
      fw.close();
    }
}
