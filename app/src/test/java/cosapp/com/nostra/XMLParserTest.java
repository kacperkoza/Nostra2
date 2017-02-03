package cosapp.com.nostra;

import org.junit.Before;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by kkoza on 02.02.2017.
 */

public class XMLParserTest {

    XMLParser xmlParser;

    CityBikesProvider cityBikesProvider;

    String hotline = "+48616668080";

    String terms = "http://poznanskirower.pl/regulamin/";

    String website = "http://www.poznanskirower.pl/";

    String name = "Poznański rower miejski";

    @Before
    public void setup() {
        xmlParser = new XMLParser(Websites.BIKE_STATIONS);
    }

    @Test
    public void readCityBikeProvider() throws Exception {
        cityBikesProvider = xmlParser.readInformationBikesProvider();
        assertEquals(hotline, cityBikesProvider.getHotline());
        assertEquals(terms, cityBikesProvider.getTerms());
        assertEquals(website, cityBikesProvider.getWebsite());
        assertEquals(name, cityBikesProvider.getName());
    }

    @Test
    public void assertDateFormat() {
        //Date format: " 02.02.2017 23:08 "
        // Space at the end and the beggining of string (!).

        Pattern pattern = Pattern.compile
                (" ?[0-9]{2}\\.?[0-9]{2}\\.?[0-9]{4} ?[0-9]{2}\\:?[0-9]{2} ?");

        Matcher matcher = pattern.matcher(xmlParser.readUpdateTime());

        System.out.println(xmlParser.readUpdateTime());
        System.out.println(matcher.matches());

        assertTrue(matcher.matches());
    }

}