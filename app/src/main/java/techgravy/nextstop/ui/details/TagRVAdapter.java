package techgravy.nextstop.ui.details;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
 * Created by aditlal on 12/01/17 - 12.
 */

public class TagRVAdapter extends RecyclerView.Adapter<TagRVAdapter.ViewHolder> {

    private Context context;
    private List<String> tagList;
    private LayoutInflater layoutInflater;
    private int textColor;

    public TagRVAdapter(Context context, List<String> tagList) {
        this.context = context;
        this.tagList = tagList;
        this.layoutInflater = LayoutInflater.from(context);
        textColor = ContextCompat.getColor(context,R.color.primary_text);
    }

    @Override
    public TagRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_persona, parent, false);
        return new TagRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TagRVAdapter.ViewHolder holder, int position) {
        holder.mCheckedTextView.setTextColor(textColor);
        holder.mCheckedTextView.setTextSize(10);
        holder.mCheckedTextView.setText(tagList.get(position));
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.checkedTextView)
        Button mCheckedTextView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mCheckedTextView.setEnabled(false);
        }
    }
}
