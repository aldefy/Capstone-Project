package techgravy.nextstop.ui.about;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import techgravy.nextstop.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by aditlal on 06/01/17.
 */

public class AboutActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appBar)
    AppBarLayout mAppBar;
    @BindView(R.id.content)
    FrameLayout mContent;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.ic_launcher)
                .setDescription(getString(R.string.about_desc))
                .addEmail("aditlal90@gmail.com")
                .addWebsite("http://aditlal.net")
                .addGitHub("aldefy/Capstone-Project")
                .addItem(new Element().setTitle("Version 1.00"))
                .create();
        mContent.addView(aboutPage);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
