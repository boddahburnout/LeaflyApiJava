package darth.leaflyapi;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class NewLeaflyApi {
    public JSONObject fechLeafly(String weedname) throws ParseException, IOException {
        String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:80.0) Gecko/20100101 Firefox/80.0";
        String LOGIN_FORM_URL = "https://www.leafly.com";
        HashMap<String, String> formData = new HashMap<>();
        formData.put("isAgeVerified", "true");
        formData.put("acceptedTermsOfUseCountry", "us");
        formData.put("acceptedTermsExpiryDate", "Wed, 12 May 2021 16:31:50 GMT");
        formData.put("acceptedTermsOfUseVersion", "3");
        Connection.Response loginForm = Jsoup.connect(LOGIN_FORM_URL)
                .method(Connection.Method.GET)
                .userAgent(USER_AGENT)
                .execute();
        HashMap<String, String> cookies = new HashMap<>(loginForm.cookies());

        Document strainsearch = Jsoup.connect("https://www.leafly.com/search?lat=43.2329&lon=-86.1885&q="+weedname.toLowerCase().trim().replaceAll(" ", "+")).data(formData).cookies(cookies).get();

        Element searchresult = strainsearch.select("a.rounded-b*").first();

        String elementsearch = searchresult.attr("href");

        String straintitle = elementsearch.replaceAll("/strains/", "");

        File filecache = new File("Leafly-cache/" + straintitle.toLowerCase().trim().replaceAll(" ", "-")+".json");

        if (filecache.exists()) {
            JSONParser jsonParser = new JSONParser();

            try (FileReader reader = new FileReader(filecache)) {
                Object obj = jsonParser.parse(reader);
                JSONObject data = (JSONObject) obj;
                return data;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Document straindoc = Jsoup.connect(LOGIN_FORM_URL + elementsearch).data(formData).cookies(cookies).get();

        Element element = straindoc.getElementById("__NEXT_DATA__");

        JSONParser parser = new JSONParser();

        JSONObject json = (JSONObject) parser.parse(element.html());

        JSONObject props = (JSONObject) json.get("props");

        JSONObject pageProps = (JSONObject) props.get("pageProps");

        JSONObject strain = (JSONObject) pageProps.get("strain");

        Path path = Paths.get("Leafly-cache/"+getStrainName(strain).toString().replaceAll(" ", "-")+".json");

        try {
            Files.writeString(path, strain.toJSONString(), StandardCharsets.UTF_8);
        } catch (NoSuchFileException e) {
            File cache = new File("Leafly-cache/");
            cache.mkdirs();
            Files.writeString(path, strain.toJSONString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strain;
    }

    public Object getStrainName(JSONObject json) {
        return json.get("name");
    }

    public Object getAverageRating(JSONObject json) {
        return json.get("averageRating");
    }

    public Object getRating(JSONObject json) {
        return json.get("rating");
    }

    public Object getCategory(JSONObject json) {
        return json.get("category");
    }

    public Object getChemotype(JSONObject json) {
        return json.get("chemotype");
    }

    public Object getDescriptionPlain(JSONObject json) {
        return json.get("descriptionPlain");
    }

    public Object getFlowerImagePng(JSONObject json) {
        return json.get("flowerImagePng");
    }

    public Object getSymbol(JSONObject json) {
        return json.get("symbol");
    }

    public Object getPageUrl(JSONObject json) {
        return json.get("pageUrl");
    }

    public Object getAka(JSONObject json) {
        return json.get("aka");
    }

    public Object getShortDescriptionPlain(JSONObject json) {
        return json.get("shortDescriptionPlain");
    }

    public Object getReviewCount(JSONObject json) {
        return json.get("reviewCount");
    }

    public Object getTopEffect(JSONObject json) {
        return json.get("topEffect");
    }

    public Object getThcPotency(JSONObject json) {
        return json.get("thcPotency");
    }

    public Object getVideoUrl(JSONObject json) {
        return json.get("videoUrl");
    }

    public Object getNugImage(JSONObject json) {
        return json.get("nugImage");
    }

    public Object[] getSimilarStrains(JSONObject json) {
        JSONArray similarStrains = (JSONArray) json.get("similarStrains");
        Object[] SimilarStrains = new Object[similarStrains.size()];
        for (int i = 0; i < similarStrains.size(); i++) {
            JSONObject info = (JSONObject) similarStrains.get(i);
            JSONObject straininfo = (JSONObject) info.get("strain");
            SimilarStrains[i] = straininfo.get("name");
        }
        return SimilarStrains;
    }

    public Object[] getTerps(JSONObject json) {
        JSONArray Terps = (JSONArray) json.get("terps");
        Object[] terps = new String[Terps.size()];
        int i = 0;
        for (Object canna : Terps.toArray()) {
            JSONObject detail = (JSONObject) canna;
            terps[i] = detail.get("name");
            i = 1 + i;
        }
        return terps;
    }

    public Object[] getConditions(JSONObject json) {
        JSONObject conditions = (JSONObject) json.get("conditions");
        Object[] Conditions = new Object[conditions.values().size()];
        int i = 0;
        for (Object contition : conditions.values()) {
            JSONObject detail = (JSONObject) contition;
            Conditions[i] = detail.get("name");
            i = 1 + i;
        }
        return Conditions;
    }

    public Object[] getNegatives(JSONObject json) {
        JSONObject negatives = (JSONObject) json.get("negatives");
        Object[] Negatives = new Object[negatives.values().size()];
        int i = 0;
        for (Object negative : negatives.values()) {
            JSONObject negativeobj = (JSONObject) negative;
            Negatives[i] = negativeobj.get("name");
            i = i + 1;
        }
        return Negatives;
    }

    public Object[] getEffects(JSONObject json) {
        JSONObject effects = (JSONObject) json.get("effects");
        Object[] Effects = new Object[effects.values().size()];
        int i = 0;
        for (Object effect : effects.values()) {
            JSONObject detail = (JSONObject) effect;
            Effects[i] = detail.get("name");
            i = 1 + i;
        }
        return Effects;
    }

    public Object[] getFlavors(JSONObject json) {
        JSONObject flavors = (JSONObject) json.get("flavors");
        Object[] Flavors = new Object[flavors.values().size()];
        int i = 0;
        for (Object flavor : flavors.values()) {
            JSONObject detail = (JSONObject) flavor;
            Flavors[i] = detail.get("name");
            i = 1 + i;
        }
        return Flavors;
    }

    public Object[] getPopularIn(JSONObject json) {
        JSONArray array = (JSONArray) json.get("popularIn");
        Object[] popularin = new Object[array.size()];
        for (int i = 0; i < array.size(); i++) {
            JSONObject location = (JSONObject) array.get(i);
            popularin[i] = location.get("location");
        }
        return popularin;
    }

    public Map<Object, Object> getCannabinoids(JSONObject json) {
        JSONObject cannabinoids = (JSONObject) json.get("cannabinoids");
        Map<Object, Object> Cannabinoids = new HashMap<>();
        for (Object canna : cannabinoids.values()) {
            JSONObject detail = (JSONObject) canna;
            Cannabinoids.put(detail.get("displayName"), detail.get("percentile50"));
        }
        return Cannabinoids;
    }

    public Map<Object, Object> getTopTerps(JSONObject json) {
        JSONObject topterp = (JSONObject) json.get("topTerps");
        Map<Object, Object> topterps = new HashMap<>();
        for (Object terp : topterp.values()) {
            JSONObject detail = (JSONObject) terp;
            topterps.put(detail.get("name"), detail.get("description"));
        }
        return topterps;
    }

    public Map<String, Object> getGrowInfo(JSONObject json) {
        JSONObject growinfo = (JSONObject) json.get("growInfo");
        Map<String, Object> GrowInfo = new HashMap<>();
        for (Object entry : growinfo.values()) {
            GrowInfo.put("averageYield", growinfo.get("averageYield"));
            GrowInfo.put("difficulty", growinfo.get("difficulty"));
            GrowInfo.put("enviroment", growinfo.get("enviroment"));
            GrowInfo.put("floweringDays", growinfo.get("floweringDays"));
            GrowInfo.put("growNotesPlain", growinfo.get("growNotesPlain"));
            GrowInfo.put("height", growinfo.get("height"));
            GrowInfo.put("outdoorFinish", growinfo.get("outdoorFinish"));
        }
        return GrowInfo;
    }
}