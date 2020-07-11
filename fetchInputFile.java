//============================================================+
// File name   : fetchInputFile.java
// Begin       : 2020-07-11
// Last Update : 2020-07-11
//
// Description : fetch output of hkStocks5Yr.py
//
// Author: pas_by@yahoo.com
//
// (c) Copyright: pas_by@yahoo.com
//============================================================+

import java.util.*;
import java.io.*;

public class fetchInputFile{
    protected String outputFile;

    public fetchInputFile()throws Exception{
        String configFile = "config.txt";
        Properties prop = new Properties();
        prop.load(new BufferedReader(new FileReader(configFile)));

        String OutputFile = prop.getProperty("outputFile");
        this.outputFile = OutputFile;
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OutputFile)));

        String inputFile = prop.getProperty("inputFile");
        LineNumberReader in = new LineNumberReader(new FileReader(inputFile));

        String aLine = null;
        while((aLine=in.readLine())!=null){
            aLine = aLine.trim();

            //  test code
            //  System.out.print(in.getLineNumber() + " - ");
            //  System.out.println(aLine);

            if(Character.isDigit(aLine.charAt(0)) && aLine.endsWith("]")){
                out.println(aLine);

            }else if(Character.isDigit(aLine.charAt(0))){
                StringTokenizer st = new StringTokenizer(aLine, ",");
                out.print(st.nextToken() + ", ");

                boolean nextLine = true;
                while(nextLine){
                    aLine = in.readLine();
                    aLine = aLine.trim();
                    if(aLine.endsWith("]")){
                        out.println(aLine);
                        nextLine = false;
                    }
                }
            }
        }
        in.close();
        out.close();
    }

    public String getOutputFileName(){
        return new String(outputFile);
    }
}
