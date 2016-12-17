package techgravy.nextstop.landing;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import techgravy.nextstop.R;

/**
 * Created by aditlal on 16/12/16 - 16.
 */

class PersonaGridRecyclerAdapter extends RecyclerView.Adapter<PersonaGridRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<PersonaTags> tagList;
    private LayoutInflater layoutInflater;
    private PersonaInterface personaInterface;

    PersonaGridRecyclerAdapter(Context context, List<PersonaTags> tagList) {
        this.context = context;
        this.tagList = tagList;
        this.personaInterface = (PersonaInterface) context;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_persona, parent, false);
        return new ViewHolder(view, personaInterface);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PersonaTags item = tagList.get(position);
        holder.checkedTextView.setTag(item);
        holder.checkedTextView.setText(item.getActionName());
        holder.checkedTextView.setActivated(item.isChecked());
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.checkedTextView)
        Button checkedTextView;

        ViewHolder(View view, PersonaInterface personaInterface) {
            super(view);
            ButterKnife.bind(this, view);
            checkedTextView.setOnClickListener(view1 -> {
                PersonaTags personaTag = (PersonaTags) view1.getTag();
                view1.setActivated(!personaTag.isChecked());
                personaTag.setChecked(!personaTag.isChecked());
                if (personaInterface != null)
                    personaInterface.itemClicked(personaTag);
            });
        }
    }

    interface PersonaInterface {
        void itemClicked(PersonaTags personaTags);
    }
}
