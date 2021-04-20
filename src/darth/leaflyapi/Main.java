package darth.leaflyapi;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        LeaflySearch test = new LeaflySearch();
        JSONObject search = test.fechLeafly("green");
        System.out.println(test.getDescriptionPlain(search));
        System.out.println(test.getGrowInfo(search));
    }
}
