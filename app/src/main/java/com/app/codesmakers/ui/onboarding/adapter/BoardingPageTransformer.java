package com.app.codesmakers.ui.onboarding.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.app.codesmakers.R;


/**
 * Created by nmillward on 7/12/16.
 */
public class BoardingPageTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View page, float position) {

        // Get page index from tag
        int pagePosition = (int) page.getTag();

        int pageWidth = (int) page.getWidth();
        float pageWidthTimesPosition = pageWidth * position;
        float absPosition = Math.abs(position);

        if (position <= -1.0f || position >= 1.0f) {

            // Page is not visible -- stop any running animations

        } else if (position == 0.0f) {

            // Page is selected -- reset any views if necessary

        } else {
/*

            // Page is currently being swiped -- perform animations here

            // Fragment 1 -- Settings
            final View tv_settings_title = page.findViewById(R.id.tv_settings_title);
            if (tv_settings_title != null) tv_settings_title.setAlpha(1.0f - absPosition * 2);

            final View tv_brightness = page.findViewById(R.id.tv_brightness);
            if (tv_brightness != null) tv_brightness.setTranslationX(pageWidthTimesPosition * 0.5f);

            final View sb_brightness = page.findViewById(R.id.sb_brightness);
            if (sb_brightness != null) sb_brightness.setTranslationX(pageWidthTimesPosition * 0.5f);

            final View tv_size = page.findViewById(R.id.tv_size);
            if (tv_size != null) tv_size.setTranslationX(pageWidthTimesPosition * 1.2f);

            final View sb_size = page.findViewById(R.id.sb_size);
            if (sb_size != null) sb_size.setTranslationX(pageWidthTimesPosition * 1.2f);

            final View tv_background = page.findViewById(R.id.tv_background);
            if (tv_background != null) tv_background.setTranslationX(pageWidthTimesPosition * 0.9f);

            final View switch_background = page.findViewById(R.id.switch_background);
            if (switch_background != null) switch_background.setTranslationX(pageWidthTimesPosition * 0.9f);

            final View cb_save = page.findViewById(R.id.cb_save);
            if (cb_save != null) cb_save.setTranslationX(pageWidthTimesPosition * 0.3f);


            // Fragment 2 -- Social Card
            final View tv_profile_title = page.findViewById(R.id.tv_social_card_title);
            if (tv_profile_title != null) tv_profile_title.setAlpha(1.0f - absPosition * 2);

            final ImageView profile = (ImageView) page.findViewById(R.id.iv_profile);
            if (profile != null) profile.setTranslationX(pageWidthTimesPosition * 1.2f);

            final View card = page.findViewById(R.id.ll_profile_bg);
            if (card != null) card.setTranslationX(pageWidthTimesPosition * 0.7f);

            final View button = page.findViewById(R.id.btn_follow);
            if (button != null) button.setTranslationX(pageWidthTimesPosition * 0.2f);
*/
            // Fragment 3 -- Feature
            final View tv_feature_title = page.findViewById(R.id.title_tv);
            if (tv_feature_title != null) {
                tv_feature_title.setScaleX(1.0f - absPosition * 2);
                tv_feature_title.setScaleY(1.0f - absPosition * 2);
                tv_feature_title.setAlpha(1.0f - absPosition * 2);
            }
            final View tvNext = page.findViewById(R.id.logo_imageview);
            if (tvNext != null) {
                tvNext.setScaleX(1.0f - absPosition * 2);
                tvNext.setScaleY(1.0f - absPosition * 2);
                tvNext.setAlpha(1.0f - absPosition * 2);
            }

        }
    }
}
