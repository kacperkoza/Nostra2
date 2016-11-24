package cosapp.com.nostra;

import android.util.Xml;

import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cosapp.com.nostra.Place.BikeStation;

/**
 * Created by Little on 2016-11-13.
 */
public class NextBikeXMLParser {

    private final String namespace = null;
    private XmlPullParser parser;

    public List getInfoAboutBikeStations(InputStream inputStream,String countryName, String cityName)
            throws XmlPullParserException, IOException{
        try{
            parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(inputStream , null);
            parser.nextTag();
            return readData(countryName,cityName);
        }finally {
            inputStream.close();
        }
    }

    private List readData(String countryName, String cityName) throws XmlPullParserException, IOException {
        ArrayList<BikeStation> places = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, namespace, "markers");
        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
                places.addAll(readFrom(countryName,cityName));
        }
        return places;
    }

    private ArrayList<BikeStation> readFrom(String countryName, String cityName) throws IOException,XmlPullParserException{
        ArrayList<BikeStation> places = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG,namespace,"country");

        String name = parser.getAttributeValue(null,"name");
        if(name.equals(countryName)){
            places.addAll(readFromCity(cityName));
        }else{
            skip();
        }
        return  places;
    }

    private ArrayList<BikeStation> readFromCity(String cityName) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG,namespace, "city");
        String city = parser.getAttributeValue(null , "name");
        if(city.equals(cityName)){
            return readCity();
        }else{
            skip();
        }
        return null;
    }

    private ArrayList<BikeStation> readCity() throws XmlPullParserException, IOException{
        ArrayList<BikeStation> places = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG,namespace,"city");

        while (parser.next() != XmlPullParser.END_TAG){

            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }

            places.add(readPlace());
        }
        return places;
    }

    private BikeStation readPlace() throws XmlPullParserException, IOException{
        BikeStation bikeStation = new BikeStation();
        parser.require(XmlPullParser.START_TAG, namespace, "place");
        String name;
        double longitude;
        double latitude;
        name = parser.getAttributeValue(null,"name");
        longitude = Double.parseDouble( parser.getAttributeValue(null,"lng"));
        latitude = Double.parseDouble( parser.getAttributeValue(null,"lat"));
        bikeStation.setPlaceName(name);
        bikeStation.setCoordinates(new LatLng(longitude, latitude));
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG,namespace,"place");

        return bikeStation;
    }

    private void skip() throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}