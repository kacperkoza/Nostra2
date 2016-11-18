package cosapp.com.nostra;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Little on 2016-11-13.
 */
public class NextBikeXMLParser {

    private final String namespace = null;

    public List parse(InputStream inputStream) throws XmlPullParserException, IOException{
        try{
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(inputStream , null);
            parser.nextTag();
            return readFeed(parser);
        }finally {
            inputStream.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<BikeStation> places = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, namespace, "markers");
        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String country = parser.getName();
            if(country.equals("country")){

                country = parser.getAttributeValue(null,"country");
                if(country.equals("DE")){

                    parser.require(XmlPullParser.START_TAG,namespace,"country");

                    while(parser.next() != XmlPullParser.END_TAG){

                        if(parser.getEventType() != XmlPullParser.START_TAG){
                            continue;
                        }

                        String city = parser.getName();
                        if(city.equals("city")){

                            city = parser.getAttributeValue(null,"name");
                            if(city.equals("Poznań")){

                                parser.require(XmlPullParser.START_TAG,namespace,"city");
                                while (parser.getEventType() != XmlPullParser.START_TAG){
                                    places.add(readPlace(parser));
                                }

                            }else{
                                skip(parser);
                            }

                        }else{
                            skip(parser);
                        }
                    }
                }else{
                    skip(parser);
                }
            }else{
                skip(parser);
            }


        }
        return places;
    }

    private BikeStation readPlace(XmlPullParser parser) throws XmlPullParserException, IOException{
        BikeStation bikeStation = new BikeStation();
        parser.require(XmlPullParser.START_TAG, namespace, "place");
        String name;
        double longitude;
        double latitude;
        name = parser.getAttributeValue(null,"name");
        longitude = Double.parseDouble( parser.getAttributeValue(null,"lng"));
        latitude = Double.parseDouble( parser.getAttributeValue(null,"lat"));
        bikeStation.setName(name);
        bikeStation.setLongitude(longitude);
        bikeStation.setLatitude(latitude);
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG,namespace,"place");

        return bikeStation;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
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