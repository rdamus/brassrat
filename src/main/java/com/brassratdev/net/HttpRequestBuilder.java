package com.brassratdev.net;

import com.brassratdev.json.Album;
import com.brassratdev.json.Date;
import com.brassratdev.json.DateRange;
import com.brassratdev.json.Filters;
import com.brassratdev.json.MediaItemRequestBody;
import com.brassratdev.json.Filters.DateFilter;

public class HttpRequestBuilder{
	MediaItemRequestBody req = null;
	
	public MediaItemRequestBody buildMediaItemRequest() {
		req = new MediaItemRequestBody();
		return req;
	}
	
	public MediaItemRequestBody buildMediaItemRequest(Album a) {
		req = new MediaItemRequestBody();
		req.setAlbumId(a.getId());
		return req;
	}
	
	public MediaItemRequestBody addDateRangeFilter(Date start, Date end) {
		Filters filters = new Filters();
		DateFilter df = filters.newDateFilter();
		DateRange range = new DateRange(start, end);
		
		df.getRanges().add(range);
		filters.setDateFilter(df);
		
		req.setFilters(filters);
		return req;
	} 
}
