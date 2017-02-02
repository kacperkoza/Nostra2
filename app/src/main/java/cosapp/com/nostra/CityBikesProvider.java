package cosapp.com.nostra;

/**
 * Created by kkoza on 02.02.2017.
 */

public class CityBikesProvider {
    private String name;
    private String website;
    private String terms;
    private String hotline;

    public CityBikesProvider(String name, String website, String terms, String hotline) {
        this.name = name;
        this.website = website;
        this.terms = terms;
        this.hotline = hotline;
    }

    @Override
    public String toString() {
        return "CityBikesProvider{" +
                "name='" + name + '\'' +
                ", website='" + website + '\'' +
                ", terms='" + terms + '\'' +
                ", hotline='" + hotline + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getWebsite() {
        return website;
    }

    public String getTerms() {
        return terms;
    }

    public String getHotline() {
        return hotline;
    }
}
