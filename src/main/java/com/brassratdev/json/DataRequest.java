package com.brassratdev.json;

import com.google.api.client.util.Key;

public class DataRequest<T> extends RequestBody {
	@Key
	T data;
}
