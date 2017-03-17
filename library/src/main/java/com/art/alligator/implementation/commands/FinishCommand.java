package com.art.alligator.implementation.commands;

import android.app.Activity;

import com.art.alligator.ActivityResult;
import com.art.alligator.AnimationData;
import com.art.alligator.Command;
import com.art.alligator.NavigationContext;
import com.art.alligator.NavigationFactory;
import com.art.alligator.Screen;
import com.art.alligator.ScreenResult;
import com.art.alligator.TransitionAnimation;
import com.art.alligator.TransitionType;
import com.art.alligator.implementation.CommandUtils;
import com.art.alligator.implementation.ScreenUtils;

/**
 * Date: 12.02.2017
 * Time: 0:53
 *
 * @author Artur Artikov
 */

public class FinishCommand implements Command {
	private ScreenResult mScreenResult;
	private AnimationData mAnimationData;

	public FinishCommand(ScreenResult screenResult, AnimationData animationData) {
		mScreenResult = screenResult;
		mAnimationData = animationData;
	}

	@Override
	public boolean execute(NavigationContext navigationContext, NavigationFactory navigationFactory) {
		Activity activity = navigationContext.getActivity();
		if(mScreenResult != null) {
			ActivityResult activityResult = navigationFactory.createActivityResult(mScreenResult);
			activity.setResult(activityResult.getResultCode(), activityResult.getData());
		}
		activity.finish();
		CommandUtils.applyActivityAnimation(activity, getActivityAnimation(navigationContext, navigationFactory));
		return false;
	}

	private TransitionAnimation getActivityAnimation(NavigationContext navigationContext, NavigationFactory navigationFactory) {
		Class<? extends Screen> screenClassFrom = ScreenUtils.getScreenClass(navigationContext.getActivity(), navigationFactory);
		Class<? extends Screen> screenClassTo = ScreenUtils.getPreviousScreenClass(navigationContext.getActivity());
		return navigationContext.getAnimationProvider().getAnimation(TransitionType.BACK, screenClassFrom, screenClassTo, true, mAnimationData);
	}
}
