package cosapp.com.nostra;

/**
 * Created by kkoza on 12.11.2016.
 */
public class Utils {

    public static String deleteHTMLTags(String stringWithTags) {
        return stringWithTags.replaceAll("\\<[^>]*>","");
    }

}
