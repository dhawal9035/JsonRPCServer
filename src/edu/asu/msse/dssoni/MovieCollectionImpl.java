package edu.asu.msse.dssoni;

import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;

import org.json.JSONObject;
import org.json.JSONTokener;

public class MovieCollectionImpl extends Object implements MovieCollection {
	public Hashtable<String, Movie> movies;
	private static final boolean debugOn = false;
	private static final String movieJsonFileName = "movies.json";
	public LinkedHashMap<String, List<String>> map = new LinkedHashMap<>();

	public MovieCollectionImpl() {
		debug("creating a new movie collection");
		movies = new Hashtable<String, Movie>();
		this.resetFromJsonFile();
	}

	private void debug(String message) {
		if (debugOn)
			System.out.println("debug: " + message);
	}

	public boolean resetFromJsonFile() {
		boolean ret = true;
		try {
			movies.clear();
			String fileName = movieJsonFileName;
			File f = new File(fileName);
			FileInputStream is = new FileInputStream(f);
			JSONObject movieMap = new JSONObject(new JSONTokener(is));
			Iterator<String> it = movieMap.keys();
			while (it.hasNext()) {
				String mType = it.next();
				JSONObject movieJson = movieMap.optJSONObject(mType);
				Movie movie = new Movie(movieJson);
				movies.put(movie.title, movie);
				debug("added " + movie.title + " : " + movie.toJsonString() +
						"\nstudents.size() is: " + movies.size());
			}
		} catch (Exception ex) {
			System.out.println("Exception reading json file: " + ex.getMessage());
			ret = false;
		}
		return ret;
	}

	public boolean saveToJsonFile() {
		boolean ret = true;
		try {
			String jsonStr;
			JSONObject obj = new JSONObject();
			for (Enumeration<String> e = movies.keys(); e.hasMoreElements(); ) {
				Movie movie = movies.get((String) e.nextElement());
				obj.put(movie.title, movie.toJson());
			}
			PrintWriter out = new PrintWriter(movieJsonFileName);
			out.println(obj.toString(2));
			out.close();
		} catch (Exception ex) {
			ret = false;
		}
		return ret;
	}

	public boolean add(Movie movie) {
		boolean ret = true;
		debug("adding movie named: " + ((movie == null) ? "" : movie.title));
		try {
			movies.put(movie.title, movie);
		} catch (Exception ex) {
			ret = false;
		}
		return ret;
	}

	public boolean remove(String title) {
		debug("removing movie named: " + title);
		movies.remove(title);
		for(String genre:map.keySet()){
			List<String> list = map.get(genre);
			if(list.contains(title)){
				list.remove(title);
			}
			if(list.isEmpty()){
				map.remove(genre);
			}
		}
		this.saveToJsonFile();
		//System.out.println(map.get(title));
		//System.out.println(movies.get(title));
		return true;
		//return ((movies.remove(title) == null) ? false : true);
	}

	public String[] getNames() {

		String[] ret = {};
		debug("getting " + movies.size() + " movie names.");
		if (movies.size() > 0) {
			ret = (String[]) (movies.keySet()).toArray(new String[0]);
		}
		return ret;
	}

	public Movie get(String title) {
		Movie ret = new Movie("", "", "", "", "", "", "", "");
		Movie movie = movies.get(title);
		if (movie != null) {
			ret = movie;
		}
		return ret;
	}

	public LinkedHashMap<String, List<String>> getMovieList() {

		for (String movie : movies.keySet()) {
			Movie m = movies.get(movie);
			System.out.println(m.title);
			String[] genre = m.genre.split(",");
			for (int i = 0; i < genre.length; i++) {
				genre[i].trim();
			}
			for (int i = 0; i < genre.length; i++) {
				if (!map.containsKey(genre[i])) {
					List<String> list = new ArrayList<>();
					list.add(m.title);
					map.put(genre[i], list);
				} else {
					List<String> list = map.get(genre[i]);
					if(!list.contains(m.title)){
						list.add(m.title);
						map.put(genre[i], list);
					}
				}
			}
		}
		return map;


	}
}
