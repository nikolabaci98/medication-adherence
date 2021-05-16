//import java.io.BufferedInputStream;
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;testRetrieveXml trx = new testRetrieveXml();
//        trx.doTest(username.getText().toString(), phoneNumber.getText().toString());
////use this to test, this will retrieve the sample xml file from the database and print it to the console
//        trx.doTest(“testname”, “11234567890”);
//
//private class testRetrieveXml{
//    Executor exec = Executors.newSingleThreadExecutor();
//    boolean done = false;
//    boolean test = false;
//    public void testRetreive(String fullname, String phoneNumber) {
//        // boolean test;
//        // User user;
//        exec.execute(() -> {
//            try {
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
//                String requestBody = requestBody(stuff);;
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
//                    System.out.println(read);
//                }
//                b.close();
//                done = true;
//            }
//            catch(Exception e){
//                e.printStackTrace();
//            }
//            done = true;
//        });
//        // done = true;
//        // t = false;
//    }
//
//    public void doTest(String username, String phoneNumber){
//        // testlogin tl = new testlogin();
//        done = false;
//        test= false;
//           /* File file = new File(getApplicationContext().getFilesDir().getPath().toString() +"/userinfo.xml");
//            try {
//                FileWriter fwrite = new FileWriter(file);
//                fwrite.write(username + "\n");
//                fwrite.write(phoneNumber + "\n");
//                fwrite.flush();
//                fwrite.close();
//            }
//            catch(Exception e){
//                e.printStackTrace();
//            }*/
//        testRetreive(username, phoneNumber);
//        // int counter = 0;
//        while(done == false){
//            //sleep(1000);
//            //counter++;
//            // if(counter == 1000000) {
//            try {   // counter = 0;
//                Thread.sleep(2000);
//            }
//            catch(Exception e){
//                e.printStackTrace();
//                System.exit(0);
//            }
//            //System.out.println("waiting");
//            // }
//        }
//
//        //return test;
//    }
//    public String requestBody(ArrayList<String> stufflist) throws Exception{
//        StringBuilder list = new StringBuilder();
//        //maybe some encryption here for temp2
//        for(int i = 0; i < stufflist.size(); i++){
//            String temp1 = stufflist.get(i);
//            i++;
//            String temp2 = stufflist.get(i);
//            list.append(URLEncoder.encode(temp1, "UTF-8"));
//            list.append("=");
//            list.append(URLEncoder.encode(temp2,"UTF-8"));
//            if((i+1) < stufflist.size())
//                list.append("&");
//        }
//        return list.toString();
//    }
//
//}