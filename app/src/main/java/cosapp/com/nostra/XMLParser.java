package cosapp.com.nostra;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import cosapp.com.nostra.Place.BikeStation;

/**
 * Created by kkoza on 02.02.2017.
 */
public class XMLParser {
    private String website;
    private Document doc;

    public XMLParser(String website) {
        this.website = website;
        this.doc = null;
    }

    public ArrayList<BikeStation> parseNextBike() {
        getDocument();

        ArrayList<BikeStation> list = new ArrayList<>(50);

         NodeList countryList = doc.getElementsByTagName("country");

        for (int i = 0; i < countryList.getLength(); i++) {
            Node item = countryList.item(i);

            if (item.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) item;

                if (element.getAttribute("name").equals("PRM Poland")) {
                    NodeList placeList = ( (Element) item ).getElementsByTagName("place");

                    for (int j = 0; j < placeList.getLength(); j++) {
                        Node singleBikeStation = placeList.item(j);

                        if (singleBikeStation.getNodeType() == Node.ELEMENT_NODE) {
                            Element el = (Element) singleBikeStation;

                            double lat = Double.parseDouble(el.getAttribute("lat"));
                            double lng = Double.parseDouble(el.getAttribute("lng"));

                            String name = el.getAttribute("name");

                            int freeBikes = Integer.parseInt(el.getAttribute("bikes"));

                            String bikesNumbers = "";

                            if (freeBikes > 0) {
                                bikesNumbers = el.getAttribute("bike_numbers");
                                bikesNumbers = bikesNumbers.replaceAll(",", ", ");
                            }

                            int id = Integer.parseInt(el.getAttribute("number"));

                            list.add(new BikeStation(id, new LatLng(lat, lng), name, bikesNumbers, freeBikes));
                        }
                    }
                    return list;
                }
            }
        }
        return null;
    }

    /**
     *  <p>Reads the time of last update of NextBike XML file.</p>
     *
     * @return the time of last update, e.g. 02.02.2017 16:35
     */
    public String readUpdateTime() {
        getDocument();

        NodeList nl = doc.getChildNodes();

        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == Element.COMMENT_NODE) {
                Comment comment = (Comment) nl.item(i);
                return comment.getData();
            }
        }

        return null;
    }

    public CityBikesProvider readInformationBikesProvider() {

        getDocument();

        NodeList list = doc.getElementsByTagName("country");

        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                if (element.getAttribute("name").equals("PRM Poland")) {
                    String name = "Poznański rower miejski";
                    String website = element.getAttribute("website");
                    String terms = element.getAttribute("terms");
                    String hotline = element.getAttribute("hotline");

                    return new CityBikesProvider(name, website, terms, hotline);
                }
            }
        }
        return null;
    }

    private void getDocument() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = null;

                    try {
                        dBuilder = dbFactory.newDocumentBuilder();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    }

                    doc = dBuilder.parse(website);

                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //start getting XML from website
        thread.start();

        //wait for thread to finish
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // optional, but recommended
        // read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        doc.getDocumentElement().normalize();

        Log.i("Root element: ", doc.getDocumentElement().getNodeName());
    }
}
