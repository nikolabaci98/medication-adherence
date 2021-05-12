package edu.cuny.qc.cs.medication_management.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import edu.cuny.qc.cs.medication_management.R;
import edu.cuny.qc.cs.medication_management.data.User;
/*Christopher Jason- this is the ViewLinksFragment, when the View links activity is created it calls this fragment which sets the layout to the appropriate layout, it uses a recycler view which allows us to add items to
 this view, displaying information to the patient and caregiver related to who is currently linked to them, from here the user can click the add link button to create a link between them and another person by entering their
 user id

 */
public class ViewLinksFragment extends Fragment {
    RecyclerView rcv;
    User currentUser;
    Button addlinks;
    linklistAdapter adapter;
    ArrayList<String> links;
    public View onCreateView(LayoutInflater inf, ViewGroup vg, Bundle savedInstanceState){
        View view = inf.inflate(R.layout.activity_listlinks, vg, false);
        rcv = (RecyclerView) view.findViewById(R.id.linklist);
        rcv.setLayoutManager(new LinearLayoutManager(getActivity()));

        currentUser = getActivity().getIntent().getParcelableExtra("currentUser");
        rcv.setLayoutManager(new LinearLayoutManager(getActivity()));
        links = new ArrayList<>();
        addlinks = view.findViewById(R.id.addlinks);
        setup();
        return view;
    }
    public void setup(){

        getLinks gl = new getLinks(currentUser,"");
        gl.fillLinks();
        System.out.println(links.toString());
        adapter = new linklistAdapter(links);
        rcv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private class linklistHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView displayuserID;
        Button rlbtn;

        int counter = 0;


        public linklistHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.linkmodel, parent, false));
            displayuserID = itemView.findViewById(R.id.linkuserText);
            rlbtn = itemView.findViewById(R.id.removelinkbtn);
            rlbtn.setOnClickListener(this);
        }

        public void bind(){
            int pos = getAdapterPosition();
            String userid = adapter.linklist.get(pos);
            displayuserID.setText(userid);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == rlbtn.getId()){
                int pos = getAdapterPosition();
                getLinks gl = new getLinks(currentUser, adapter.linklist.get(pos));
                System.out.println(gl.userid2);
                gl.removeLinks();
                links.remove(pos);
                //adapter.notifyItemRemoved(pos);
                adapter.notifyItemRangeChanged(pos, links.size());
                adapter.notifyDataSetChanged();
            }
        }


    }

    private class linklistAdapter extends RecyclerView.Adapter<ViewLinksFragment.linklistHolder> {
        private ArrayList<String> linklist;
        public linklistAdapter(ArrayList<String> list){
            linklist = list;
        }


        @NonNull
        @Override
        public ViewLinksFragment.linklistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inf = LayoutInflater.from(getActivity());
            return new ViewLinksFragment.linklistHolder(inf, parent);
        }

        @Override
        public void onBindViewHolder(ViewLinksFragment.linklistHolder holder, int position){

            holder.bind();
        }

        @Override
        public int getItemCount(){
            return linklist.size();
        }
    }
    protected class getLinks{
        Executor exec = Executors.newSingleThreadExecutor();
        User user;
        boolean t = false;
        String userid2;
        public getLinks(User user, String userID2){
            this.user = user; this.userid2= userID2;
        }
        public void getLinklist(){
            exec.execute( ()->{
                try{

                    URL link = new URL("http://68.198.11.61:8089/testretreiveUserData/getLinklist");
                    HttpURLConnection connect = (HttpURLConnection) link.openConnection();
                    connect.setRequestMethod("POST");
                    connect.setDoOutput(true);
                    connect.setDoInput(true);
                    OutputStream os = connect.getOutputStream();
                    BufferedWriter write = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    // String requestBody = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode("test12345", "UTF-8");
                    StringBuilder result = new StringBuilder();
                    result.append(URLEncoder.encode("userID", "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(user.getUserID(), "UTF-8"));

                    write.write(result.toString());
                    write.flush();
                    write.close();
                    os.close();
                    connect.connect();
                    InputStream in = new BufferedInputStream(connect.getInputStream());
                    BufferedReader b = new BufferedReader(new InputStreamReader(in));
                    String x;
                    while ((x = b.readLine()) != null) {
                        System.out.println(x);
                        links.add(x);
                    }

                    t = true;
                }
                catch(Exception e){
                    e.printStackTrace();
                }

            });
        }
        public void removeLink(){
            exec.execute( ()->{
                try{

                    URL link = new URL("http://68.198.11.61:8089/testretreiveUserData/removeLink");
                    HttpURLConnection connect = (HttpURLConnection) link.openConnection();
                    connect.setRequestMethod("POST");
                    connect.setDoOutput(true);
                    connect.setDoInput(true);
                    OutputStream os = connect.getOutputStream();
                    BufferedWriter write = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    // String requestBody = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode("test12345", "UTF-8");
                    StringBuilder result = new StringBuilder();
                    System.out.println(user.getUserID());
                    System.out.println(userid2);
                    result.append(URLEncoder.encode("userID1", "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(user.getUserID(), "UTF-8"));
                    result.append("&");
                    result.append(URLEncoder.encode("userID2", "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(userid2, "UTF-8"));
                    write.write(result.toString());
                    write.flush();
                    write.close();
                    os.close();
                    connect.connect();
                    InputStream in = new BufferedInputStream(connect.getInputStream());
                    BufferedReader b = new BufferedReader(new InputStreamReader(in));
                    String x;
                    while ((x = b.readLine()) != null) {
                        System.out.println(x);
                       // links.add(x);
                    }

                    t = true;
                }
                catch(Exception e){
                    e.printStackTrace();
                }

            });
        }
        public void fillLinks(){
             t = false;
             getLinklist();
             while(t == false){
                 try{
                     Thread.sleep(1000);
                 }
                 catch(Exception e){
                     e.printStackTrace();
                     System.exit(0);
                 }
             }
        }
        public void removeLinks(){
            t = false;
            removeLink();
            while(t == false){
                try{
                    Thread.sleep(1000);
                }
                catch(Exception e){
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        }

    }
}
