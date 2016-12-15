package techgravy.nextstop.landing;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import techgravy.nextstop.R;
import techgravy.nextstop.base.PagerAdapter;
import techgravy.nextstop.utils.ParallaxPagerTransformer;

/**
 * Created by aditlal on 13/12/16.
 */

public class LandingActivity extends AppCompatActivity {

    @BindView(R.id.pagerContainer)
    ViewPager pagerContainer;
    @BindView(R.id.radioBtnOne)
    RadioButton radioBtnOne;
    @BindView(R.id.radioBtnTwo)
    RadioButton radioBtnTwo;
    @BindView(R.id.radioBtnThree)
    RadioButton radioBtnThree;
    @BindView(R.id.viewPagerCountDots)
    RadioGroup viewPagerCountDots;
    @BindView(R.id.startBtn)
    Button startBtn;
    private PagerAdapter pagerAdapter;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            // Set the status bar to dark-semi-transparentish
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }
        setContentView(R.layout.activity_landing);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        fragmentList = new ArrayList<>();
        GettingStartedFragment page1 = GettingStartedFragment.newInstance(0);
        GettingStartedFragment page2 = GettingStartedFragment.newInstance(1);
        GettingStartedFragment page3 = GettingStartedFragment.newInstance(2);
        fragmentList.add(page1);
        fragmentList.add(page2);
        fragmentList.add(page3);

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragmentList);
        pagerContainer.setAdapter(pagerAdapter);
        pagerContainer.setPageTransformer(false, new ParallaxPagerTransformer());
        pageSwitcher(4);
        pagerContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                viewPagerCountDots.check(viewPagerCountDots.getChildAt(position).getId());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    Timer timer;
    int page = 0;

    public void pageSwitcher(int seconds) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000);
    }

    class RemindTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(() -> {
                if (page > 3) {
                    timer.cancel();
                } else {
                    pagerContainer.setCurrentItem(page++);
                }
            });

        }
    }
}
