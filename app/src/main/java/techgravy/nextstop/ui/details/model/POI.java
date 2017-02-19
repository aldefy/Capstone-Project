package techgravy.nextstop.ui.details.model;

import auto.json.AutoJson;

@AutoJson
public abstract class POI{

	@AutoJson.Field(name="city")
	public abstract String city();

	@AutoJson.Field(name="description")
	public abstract String description();

	@AutoJson.Field(name="photo")
	public abstract String photo();

	@AutoJson.Field(name="place")
	public abstract String place();

	@AutoJson.Field(name="place_id")
	public abstract String placeId();

}