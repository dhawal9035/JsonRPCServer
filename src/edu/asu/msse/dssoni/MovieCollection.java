package edu.asu.msse.dssoni;

import java.util.LinkedHashMap;
import java.util.List;

public interface MovieCollection {
	public boolean saveToJsonFile();

	public boolean resetFromJsonFile();

	public boolean add(Movie movie);

	public boolean remove(String title);

	public Movie get(String title);

	public String[] getNames();

	public LinkedHashMap<String,List<String>> getMovieList();
}
