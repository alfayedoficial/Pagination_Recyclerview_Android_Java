package com.alialfayed.pagination.java.model;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import static com.alialfayed.pagination.java.utils.Constants.BASE_URL_IMG;

public class ResultsItem{

	@SerializedName("overview")
	private String overview;

	@SerializedName("original_language")
	private String originalLanguage;

	@SerializedName("original_title")
	private String originalTitle;

	@SerializedName("video")
	private boolean video;

	@SerializedName("title")
	private String title;

	@SerializedName("genre_ids")
	private List<Integer> genreIds;

	@SerializedName("poster_path")
	private String posterPath;

	@SerializedName("backdrop_path")
	private String backdropPath;

	@SerializedName("release_date")
	private String releaseDate;

	@SerializedName("popularity")
	private double popularity;

	@SerializedName("vote_average")
	private double voteAverage;

	@SerializedName("id")
	private int id;

	@SerializedName("adult")
	private boolean adult;

	@SerializedName("vote_count")
	private int voteCount;

	public String getOverview(){
		return overview;
	}

	public String getOriginalLanguage(){
		return originalLanguage;
	}

	public String getOriginalTitle(){
		return originalTitle;
	}

	public boolean isVideo(){
		return video;
	}

	public String getTitle(){
		return title;
	}

	public List<Integer> getGenreIds(){
		return genreIds;
	}

	public String getPosterPath(){
		return posterPath;
	}

	public String getBackdropPath(){
		return backdropPath;
	}

	public String getReleaseDate(){
		return releaseDate;
	}

	public double getPopularity(){
		return popularity;
	}

	public double getVoteAverage(){
		return voteAverage;
	}

	public int getId(){
		return id;
	}

	public boolean isAdult(){
		return adult;
	}

	public int getVoteCount(){
		return voteCount;
	}

	@BindingAdapter(value = {"app:imageMovie" , "app:progressMovie"})
	public static void setImageMovie(ImageView image, String imageUrl  , ProgressBar progressMovie ) {
		String  url = BASE_URL_IMG + imageUrl;
		Glide.with(image.getContext())
				.load(url)
				.listener(new RequestListener<Drawable>() {
					@Override
					public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
						progressMovie.setVisibility(View.GONE);
						return false;
					}

					@Override
					public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
						progressMovie.setVisibility(View.GONE);
						return false;
					}
				})
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.centerCrop()
				.into(image);
	}

	@BindingAdapter(value = {"app:yearMovie" ,  "app:originalLanguage"})
	public static void setYearMovie(TextView year,String  releaseDate , String originalLanguage  ) {

		if (releaseDate != null && originalLanguage != null){
			year.setText(releaseDate.substring(0, 4) + " | " + originalLanguage.toUpperCase());
		}else if (releaseDate != null){
			originalLanguage = "EN";
			year.setText(releaseDate.substring(0, 4) + " | " + originalLanguage.toUpperCase());
		}else if (originalLanguage != null){
			releaseDate = "----";
			year.setText(releaseDate + " | " + originalLanguage.toUpperCase());
		}else {
			year.setVisibility(View.GONE);
		}

	}
}

