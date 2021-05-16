package edu.cuny.qc.cs.medication_management.data;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MedicationList extends Application {
    public static MedicationList MedicationList;
    private List<Medication> medicationList;

    private MedicationList(Context context) throws IOException, SAXException, ParserConfigurationException {
        /*
         * Here we make the connection to the DB to retrieve the medications form the parse XML file
         * and put the in the list to be updated in the UpdateUI()
         */
        medicationList = new ArrayList<>();
        parseXML(context);

    }

    private void parseXML(Context context) throws ParserConfigurationException, IOException, SAXException {
        //File xmlFile = new File(Environment.getStorageDirectory() + "/assets/PatientHealthRecord.xml");

        AssetManager assetManager = context.getAssets();
        InputStream xmlFile = assetManager.open("PatientHealthRecord.xml");

        //Enables the app to obtain a parser that produces DOM object trees from XML documents.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //Obtain a DOM Document instances from an XML document
        //Using this class we can obtain a Document from XML
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        //Parse the content of the given file as an XML document and return a new DOM Document object.
        Document doc = dBuilder.parse(xmlFile);

        //Get all elements of the doc that are tagged as <section>
        NodeList sectionList = doc.getElementsByTagName("section");

        Node section = null;
        boolean didBreak = false;
        //Iterate the list of <sections>
        for(int i = 0; i < sectionList.getLength(); i++) {
            section = sectionList.item(i);
            Element e = (Element) section;
            NodeList childrenList = e.getElementsByTagName("title");
            //Get all children with tag <title>, , stop when title is "Medications"
            for(int j = 0; j < childrenList.getLength(); j++) {
                Node child = childrenList.item(j);
                String title = child.getTextContent();
                if(title.equals("Medications")) {
                    didBreak = true;
                    break;
                }
            }

            if(didBreak) {
                break;
            }
        }

        //Get all the elements with tag <tr> inside the Medication section
        Element elem = (Element) section;
        NodeList list = elem.getElementsByTagName("tr");
        for(int i = 1; i < list.getLength(); i = i+3) {
            String s = list.item(i).getTextContent().replaceAll("[\\s]{2,}", "\n").trim();
            String[] arr = s.split("\n");

            if (arr.length < 15) {
                medicationList.add(new Medication(arr[0], arr[1],
                        arr[9], arr[12], true, "", new ArrayList<String>()));
            } else {
                medicationList.add(new Medication(arr[0], arr[1],
                        arr[9], arr[12], true, arr[15], new ArrayList<String>()));
            }

        }
    }

    public List<Medication> getMedicationList(){
        return medicationList;
    }

    public static MedicationList get(Context context) throws ParserConfigurationException, SAXException, IOException {
        if(MedicationList == null){
            MedicationList = new MedicationList(context);
        }
        return MedicationList;
    }


    public void updateReminder(String medName, ArrayList<String> reminderTimes){
        System.out.println("It works!!!!");
        for(int i = 0; i < medicationList.size(); i++){
            if(medicationList.get(i).getMedicationName().equals(medName)){
                medicationList.get(i).setMedicationReminders(reminderTimes);
                break;
            }
        }
    }
}
