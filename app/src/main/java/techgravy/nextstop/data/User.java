package techgravy.nextstop.data;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.util.List;

import techgravy.nextstop.ui.landing.PersonaTags;

/**
 * Created by aditlal on 17/12/16 - 17.
 */
@IgnoreExtraProperties
public class User {
    @PropertyName("photoUrl")
    public String photoUrl;
    @PropertyName("fullName")
    public String fullName;
    @PropertyName("personaTags")
    public List<PersonaTags> personaTagsList;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String photoUrl, String fullName, List<PersonaTags> personaTagsList) {
        this.photoUrl = photoUrl;
        this.fullName = fullName;
        this.personaTagsList = personaTagsList;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<PersonaTags> getPersonaTagsList() {
        return personaTagsList;
    }

    public void setPersonaTagsList(List<PersonaTags> personaTagsList) {
        this.personaTagsList = personaTagsList;
    }
}