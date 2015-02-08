package stravajava.util.impl.gson.serializer;

import java.lang.reflect.Type;

import stravajava.api.v3.model.reference.StravaStreamType;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author Dan Shannon
 *
 */
public class StreamTypeSerializer implements JsonSerializer<StravaStreamType>, JsonDeserializer<StravaStreamType>{

	/**
	 * @see com.google.gson.JsonDeserializer#deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext)
	 */
	@Override
	public StravaStreamType deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException {
		return (json == null ? null : StravaStreamType.create(json.getAsString()));
	}

	/**
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(StravaStreamType streamType, Type type, JsonSerializationContext context) {
		return (streamType == null ? null : context.serialize(streamType.getValue()));	}

}