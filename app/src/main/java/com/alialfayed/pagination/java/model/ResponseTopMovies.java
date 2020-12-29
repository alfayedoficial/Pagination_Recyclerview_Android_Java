package com.alialfayed.pagination.java.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseTopMovies{

	@SerializedName("page")
	private int page;

	@SerializedName("total_pages")
	private int totalPages;

	@SerializedName("results")
	private List<ResultsItem> results;

	@SerializedName("total_results")
	private int totalResults;

	public int getPage(){
		return page;
	}

	public int getTotalPages(){
		return totalPages;
	}

	public List<ResultsItem> getResults(){
		return results;
	}

	public int getTotalResults(){
		return totalResults;
	}
}