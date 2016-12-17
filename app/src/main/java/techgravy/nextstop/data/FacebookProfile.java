package techgravy.nextstop.data;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by aditlal on 17/12/16 - 17.
 */
@JsonObject
public class FacebookProfile
{
    @JsonField
    private String id;
    @JsonField
    private FacebookPicture picture;
    @JsonField
    private String first_name;
    @JsonField
    private String name;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public FacebookPicture getPicture ()
    {
        return picture;
    }

    public void setPicture (FacebookPicture picture)
    {
        this.picture = picture;
    }

    public String getFirst_name ()
    {
        return first_name;
    }

    public void setFirst_name (String first_name)
    {
        this.first_name = first_name;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", picture = "+picture+", first_name = "+first_name+", name = "+name+"]";
    }
}
