package edu.asu.msse.dssoni;

import java.util.*;
import org.json.JSONObject;
import org.json.JSONArray;

public class MovieCollectionSkeleton extends Object {
	MovieCollection movieLib;

	public MovieCollectionSkeleton(MovieCollection movieLib) {
		this.movieLib = movieLib;
	}

	public String callMethod(String request) {
		JSONObject result = new JSONObject();
		try {
			JSONObject theCall = new JSONObject(request);
			String method = theCall.getString("method");
			int id = theCall.getInt("id");
			JSONArray params = null;
			if (!theCall.isNull("params")) {
				params = theCall.getJSONArray("params");
			}
			result.put("id", id);
			result.put("jsonrpc", "2.0");
			if (method.equals("resetFromJsonFile")) {
				movieLib.resetFromJsonFile();
				result.put("result", true);
			} else if (method.equals("saveToJsonFile")) {
				boolean saved = movieLib.saveToJsonFile();
				result.put("result", saved);
			} else if (method.equals("remove")) {
				String title = params.getString(0);
				boolean removed = movieLib.remove(title);
				if(removed == true)
					movieLib.saveToJsonFile();
				//System.out.println(movieLib.get(title));
				result.put("result", removed);
			} else if (method.equals("addIos")) {
				//System.out.println("add"+params.getJSONObject(0).toString());
				JSONArray ja = new JSONArray();
				String movieJson = params.getString(0);
				Movie movie = new Movie(movieJson);
				boolean added = movieLib.add(movie);
				if(added == true)
					movieLib.saveToJsonFile();
				result.put("result", added);
			} else if (method.equals("add")) {
				//System.out.println("add"+params.getJSONObject(0).toString());
				JSONObject movieJson = params.getJSONObject(0);
				Movie movie = new Movie(movieJson);
				boolean added = movieLib.add(movie);
				if(added == true)
					movieLib.saveToJsonFile();
				result.put("result", added);
			}else if (method.equals("get")) {
				String title = params.getString(0);
				Movie movie = movieLib.get(title);
				result.put("result", movie.toJson());
			} else if (method.equals("getMovieList")) {
				LinkedHashMap lmap = movieLib.getMovieList();
				//System.out.println("In Movie list"+lmap.keySet());
				result.put("result", new JSONObject(lmap));
			} else if (method.equals("getNames")) {
				String[] names = movieLib.getNames();
				JSONArray resArr = new JSONArray();
				for (int i = 0; i < names.length; i++) {
					resArr.put(names[i]);
				}
				result.put("result", resArr);
			}
		} catch (Exception ex) {
			System.out.println("exception in callMethod: " + ex.getMessage());
		}
		System.out.println("returning: " + result.toString());
		return "HTTP/1.0 200 Data follows\nServer:localhost:8080\nContent-Type:text/plain\nContent-Length:"
				+ (result.toString()).length() + "\n\n" + result.toString();
	}
}
