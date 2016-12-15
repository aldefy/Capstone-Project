package techgravy.nextstop.utils;

import android.support.v4.view.ViewPager;
import android.view.View;

import techgravy.nextstop.R;



public class ParallaxPagerTransformer implements ViewPager.PageTransformer {




    public void transformPage(View view, float position) {

        int pageWidth = view.getWidth();


        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0); //1
            view.findViewById(R.id.descriptionText).setAlpha(0);
        } else if (position <= 1) { // [-1,1]
            view.findViewById(R.id.descriptionText).setAlpha(1);
            view.findViewById(R.id.containerImageView).setTranslationX(-position * (pageWidth / 2)); //Half the normal speed
            view.findViewById(R.id.appNameTextView).setTranslationX(-position*pageWidth);
            view.findViewById(R.id.descriptionText).setTranslationX(-position * (pageWidth ));
            view.findViewById(R.id.subDescriptionText).setTranslationX(-position * (pageWidth /5));
            view.findViewById(R.id.subDescriptionText).setTranslationY(1);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(1);
            view.findViewById(R.id.descriptionText).setAlpha(0);
        }


    }
}