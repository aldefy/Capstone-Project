package techgravy.nextstop.utils;

import android.app.SharedElementCallback;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import techgravy.nextstop.R;

/**
 * Created by aditlal on 25/12/16.
 */

public class TransitionsUtil {

    /**
     * Returns a modified enter transition that excludes the navigation bar and status
     * bar as targets during the animation. This ensures that the navigation bar and
     * status bar won't appear to "blink" as they fade in/out during the transition.
     */
    public static Transition makeEnterTransition() {
        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        return fade;
    }





    public static SharedElementCallback createSharedElementReenterCallback(
            @NonNull Context context) {
        final String shotTransitionName = context.getString(R.string.transition_shot);
        final String shotBackgroundTransitionName =
                context.getString(R.string.transition_shot_background);
        return new SharedElementCallback() {

            /**
             * We're performing a slightly unusual shared element transition i.e. from one view
             * (image in the grid) to two views (the image & also the background of the details
             * view, to produce the expand effect). After changing orientation, the transition
             * system seems unable to map both shared elements (only seems to map the shot, not
             * the background) so in this situation we manually map the background to the
             * same view.
             */
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                if (sharedElements.size() != names.size()) {
                    // couldn't map all shared elements
                    final View sharedShot = sharedElements.get(shotTransitionName);
                    if (sharedShot != null) {
                        // has shot so add shot background, mapped to same view
                        sharedElements.put(shotBackgroundTransitionName, sharedShot);
                    }
                }
            }
        };
    }

    public static
    @Nullable
    Transition findTransition(
            @NonNull TransitionSet set, @NonNull Class<? extends Transition> clazz) {
        for (int i = 0; i < set.getTransitionCount(); i++) {
            Transition transition = set.getTransitionAt(i);
            if (transition.getClass() == clazz) {
                return transition;
            }
            if (transition instanceof TransitionSet) {
                Transition child = findTransition((TransitionSet) transition, clazz);
                if (child != null) return child;
            }
        }
        return null;
    }

    public static
    @Nullable
    Transition findTransition(
            @NonNull TransitionSet set,
            @NonNull Class<? extends Transition> clazz,
            @IdRes int targetId) {
        for (int i = 0; i < set.getTransitionCount(); i++) {
            Transition transition = set.getTransitionAt(i);
            if (transition.getClass() == clazz) {
                if (transition.getTargetIds().contains(targetId)) {
                    return transition;
                }
            }
            if (transition instanceof TransitionSet) {
                Transition child = findTransition((TransitionSet) transition, clazz, targetId);
                if (child != null) return child;
            }
        }
        return null;
    }

    public static List<Boolean> setAncestralClipping(@NonNull View view, boolean clipChildren) {
        return setAncestralClipping(view, clipChildren, new ArrayList<Boolean>());
    }

    private static List<Boolean> setAncestralClipping(
            @NonNull View view, boolean clipChildren, List<Boolean> was) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            was.add(group.getClipChildren());
            group.setClipChildren(clipChildren);
        }
        ViewParent parent = view.getParent();
        if (parent != null && parent instanceof ViewGroup) {
            setAncestralClipping((ViewGroup) parent, clipChildren, was);
        }
        return was;
    }

    public static void restoreAncestralClipping(@NonNull View view, List<Boolean> was) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            group.setClipChildren(was.remove(0));
        }
        ViewParent parent = view.getParent();
        if (parent != null && parent instanceof ViewGroup) {
            restoreAncestralClipping((ViewGroup) parent, was);
        }
    }

    public static class TransitionListenerAdapter implements Transition.TransitionListener {

        @Override
        public void onTransitionStart(Transition transition) {
        }

        @Override
        public void onTransitionEnd(Transition transition) {
        }

        @Override
        public void onTransitionCancel(Transition transition) {
        }

        @Override
        public void onTransitionPause(Transition transition) {
        }

        @Override
        public void onTransitionResume(Transition transition) {
        }
    }
}
