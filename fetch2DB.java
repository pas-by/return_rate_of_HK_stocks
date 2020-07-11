//============================================================+
// File name   : fetch2DB.java
// Begin       : 2020-07-11
// Last Update : 2020-07-11
//
// Description : fetch output of hkStocks5Yr.py
//               into data base
//
// Author      : pas_by@yahoo.com
//
// (c) Copyright: pas_by@yahoo.com
//============================================================+

import java.io.*;
import java.util.*;
import java.sql.*;

public class fetch2DB{
    public static void main(String[] args)throws Exception{
        mySqlSsl dbConnectionObject = new mySqlSsl();
        dbConnectionObject.printServerInfo();

        String sqlString = "INSERT INTO five_year_return(code, `5`, `4`, `3`, `2`, `1`)VALUES(?,?,?,?,?,?)";
        PreparedStatement pstm = dbConnectionObject.getConnection().prepareStatement(sqlString);

        fetchInputFile x = new fetchInputFile();
        LineNumberReader in = new LineNumberReader(new  FileReader(x.getOutputFileName()));

        String aLine = null;
        while((aLine=in.readLine())!=null){
            System.out.print(in.getLineNumber() + " - ");

            aLine = aLine.trim();
            //  remove square brackets
            aLine = aLine.replaceAll("[\\[]", "");
            aLine = aLine.replaceAll("(\\])", "");
            //  remove spaces
            aLine = aLine.replaceAll("(\\s)", "");

            StringTokenizer st = new StringTokenizer(aLine, ",");
            String[] retRate = new String[6];
            for(int i=0; i<6; i++){
                retRate[i] = st.nextToken();
                System.out.print("," + retRate[i]);
                if(retRate[i].equals("nan")){
                    pstm.setNull(i+1, Types.VARCHAR);
                }else{
                    pstm.setString(i+1, retRate[i]);
                }
            }
            System.out.println(" - " + pstm.executeUpdate());
        }

        in.close();
    }
}
