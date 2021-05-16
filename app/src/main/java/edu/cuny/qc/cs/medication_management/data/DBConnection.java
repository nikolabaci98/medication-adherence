package edu.cuny.qc.cs.medication_management.data;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DBConnection {
    Executor exec = Executors.newSingleThreadExecutor();
    boolean done = false;
    boolean test = false;

    public void storeFile(String fullname, String phoneNumber, InputStream file) {
        exec.execute(() -> {
            try {

                URL link = new URL("http://68.198.11.61:8089/testStoreXml/testStoreFile");
                HttpURLConnection connect = (HttpURLConnection) link.openConnection();
                connect.setRequestMethod("POST");
                connect.setDoOutput(true);
                connect.setDoInput(true);
                OutputStream os = connect.getOutputStream();
                BufferedWriter write = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                ArrayList<String> stuff = new ArrayList<>();
                stuff.add("fullname");
                stuff.add(fullname);
                stuff.add("phoneNumber");
                stuff.add(phoneNumber);
                stuff.add("filecontents");
                String filecontents;

                int size = file.available();
                byte[] buffer = new byte[size];
                file.read();
                filecontents = new String(buffer);
                stuff.add(filecontents);


                String requestBody = requestBody(stuff);;
                write.write(requestBody);
                write.flush();
                write.close();
                os.close();
                connect.connect();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        });
    }

//    public InputStream testRetreive(String fullname, String phoneNumber) {
//        // boolean test;
//        // User user;
//        InputStream inp;
//        exec.execute(() -> {
//            try {
//                System.out.println();
//                System.out.println("Retriving File from DB");
//                System.out.println();
//
//                URL link = new URL("http://68.198.11.61:8089/testRetreiveXML/testRetreive");
//                HttpURLConnection connect = (HttpURLConnection) link.openConnection();
//                connect.setRequestMethod("POST");
//                connect.setDoOutput(true);
//                connect.setDoInput(true);
//                OutputStream os = connect.getOutputStream();
//                BufferedWriter write = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//                ArrayList<String> stuff = new ArrayList<>();
//                stuff.add("fullname");
//                stuff.add(fullname);
//                stuff.add("phoneNumber");
//                stuff.add(phoneNumber);
//                String requestBody = requestBody(stuff);
//                write.write(requestBody);
//                write.flush();
//                write.close();
//                os.close();
//                connect.connect();
//                InputStream in = new BufferedInputStream(connect.getInputStream());
//                BufferedReader b = new BufferedReader(new InputStreamReader(in));
//                String read;
//                //print each line from the file, from here we can recreate the file
//                while ((read = b.readLine()) != null) {
//
//                    System.out.println(">>>>><<<<<<>>>>>><<<<<" + read);
//                }
//                b.close();
//                done = true;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }

    public String requestBody(ArrayList<String> stufflist) throws Exception{
        StringBuilder list = new StringBuilder();
        //maybe some encryption here for temp2
        for(int i = 0; i < stufflist.size(); i++){
            String temp1 = stufflist.get(i);
            i++;
            String temp2 = stufflist.get(i);
            list.append(URLEncoder.encode(temp1, "UTF-8"));
            list.append("=");
            list.append(URLEncoder.encode(temp2,"UTF-8"));
            if((i+1) < stufflist.size())
                list.append("&");
        }
        return list.toString();
    }

}
