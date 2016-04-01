package edu.asu.msse.dssoni;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;
import java.util.Arrays;

public class Movie {
	public String title;
	public String year;
	public String rated;
	public String released;
	public String runTime;
	public String genre;
	public String actors;
	public String plot;
	
	Movie(String title, String year, String rated, String released, String runTime, String genre, String actors, String plot){
	      this.title = title;
	      this.year = year;
	      this.rated = rated;
	      this.released = released;
	      this.runTime = runTime;
	      this.genre = genre;
	      this.actors = actors;
	      this.plot = plot;
	   }

	Movie(String jsonString) {
		try {
			JSONObject jo = new JSONObject(jsonString);
			title = jo.getString("Title");
			year = jo.getString("Year");
			rated = jo.getString("Rated");
			released = jo.getString("Released");
			runTime = jo.getString("Runtime");
			genre = jo.getString("Genre");
			actors = jo.getString("Actors");
			plot = jo.getString("Plot");

		} catch (Exception ex) {
			System.out.println(this.getClass().getSimpleName() + ": error converting from json string");
		}
	}

	public Movie(JSONObject jsonObj) {
		try {
			title = jsonObj.getString("Title");
			year = jsonObj.getString("Year");
			rated = jsonObj.getString("Rated");
			released = jsonObj.getString("Released");
			runTime = jsonObj.getString("Runtime");
			genre = jsonObj.getString("Genre");
			actors = jsonObj.getString("Actors");
			plot = jsonObj.getString("Plot");
		} catch (Exception ex) {
			System.out.println(this.getClass().getSimpleName() + ": error converting from json string");
		}
	}

	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		try {			jo.put("Title", title);
			jo.put("Year", year);
			jo.put("Rated", rated);
			jo.put("Released", released);
			jo.put("Runtime", runTime);
			jo.put("Genre", genre);
			jo.put("Actors", actors);
			jo.put("Plot", plot);
		} catch (Exception ex) {
			System.out.println(this.getClass().getSimpleName() + ": error converting to json");
		}
		return jo;
	}

	public String toJsonString() {
		String ret = "";
		try {
			ret = this.toJson().toString();
		} catch (Exception ex) {
			System.out.println(this.getClass().getSimpleName() + ": error converting to json string");
		}
		return ret;
	}
}
