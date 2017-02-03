package cosapp.com.nostra;

/**
 * Created by kkoza on 02.02.2017.
 */

import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class OpeningHoursReaderTest {

    private String closed = "nieczynne";

    private String opened24h = "24h";

    private String opened24H = "24H";

    private String noInfo = "";

    private String validHours = "10:15-18:25";

    private String validHours2 = "8.00-23.30";

    @Before
    public void setup() {
    }

    @Test
    public void isClosed24h() throws Exception {
        assertEquals(true, OpeningHoursReader.parse(closed).isClosedAllDay());
    }

    @Test
    public void isOpened24h() throws Exception {
        assertEquals(true, OpeningHoursReader.parse(opened24h).isOpenedAllDay());
        assertEquals(true, OpeningHoursReader.parse(opened24H).isOpenedAllDay());
    }

    @Test
    public void noInfo() throws Exception {
        assertEquals(null, OpeningHoursReader.parse(noInfo));
    }

    @Test
    public void validOpeningHours() throws Exception {
        assertEquals(new OpeningHours(new LocalTime(10, 15), new LocalTime(18, 25)),
                OpeningHoursReader.parse(validHours));

        assertEquals(new OpeningHours(new LocalTime(8, 0), new LocalTime(23, 30)),
                OpeningHoursReader.parse(validHours2));
    }
}