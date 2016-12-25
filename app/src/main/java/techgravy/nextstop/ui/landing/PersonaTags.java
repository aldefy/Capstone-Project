package techgravy.nextstop.ui.landing;

/**
 * Created by aditlal on 16/12/16 - 16.
 */
public class PersonaTags {
    String actionName;

    boolean checked;

    public PersonaTags(String name) {
        this.actionName = name;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "PersonaTags{" +
                "actionName='" + actionName + '\'' +
                ", checked=" + checked +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonaTags that = (PersonaTags) o;

        if (checked != that.checked) return false;
        return actionName != null ? actionName.equals(that.actionName) : that.actionName == null;

    }

    @Override
    public int hashCode() {
        int result = actionName != null ? actionName.hashCode() : 0;
        result = 31 * result + (checked ? 1 : 0);
        return result;
    }
}
