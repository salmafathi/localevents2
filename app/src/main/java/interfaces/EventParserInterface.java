package interfaces;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public interface EventParserInterface {

	public  ArrayList getResponse(JSONObject ja) throws JSONException;
}
