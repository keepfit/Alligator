package me.aartikov.alligator.navigationfactories;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import me.aartikov.alligator.ActivityResult;
import me.aartikov.alligator.NavigationFactory;
import me.aartikov.alligator.Screen;
import me.aartikov.alligator.ScreenResult;
import me.aartikov.alligator.ViewType;
import me.aartikov.alligator.functions.Function;
import me.aartikov.alligator.functions.Function2;
import me.aartikov.alligator.helpers.ScreenClassUtils;
import me.aartikov.alligator.navigationfactories.registry.ActivityRegistry;
import me.aartikov.alligator.navigationfactories.registry.DialogFragmentRegistry;
import me.aartikov.alligator.navigationfactories.registry.FragmentRegistry;
import me.aartikov.alligator.navigationfactories.registry.ScreenForResultRegistry;

/**
 * Date: 11.02.2017
 * Time: 11:41
 *
 * @author Artur Artikov
 */

/**
 * Navigation factory with screen registration methods.
 */
public class RegistryNavigationFactory implements NavigationFactory {
	private ActivityRegistry mActivityRegistry = new ActivityRegistry();
	private FragmentRegistry mFragmentRegistry = new FragmentRegistry();
	private DialogFragmentRegistry mDialogFragmentRegistry = new DialogFragmentRegistry();
	private ScreenForResultRegistry mScreenForResultRegistry = new ScreenForResultRegistry();

	/**
	 * Registers a screen represented by an activity using a custom intent creation function and a custom screen getting function.
	 *
	 * @param <ScreenT>              screen type
	 * @param screenClass            screen class
	 * @param activityClass          class of the activity that represents the screen. It must be the same activity class that was used
	 *                               to create an intent in {@code intentCreationFunction} or {@code null} if an intent is implicit.
	 * @param intentCreationFunction function that returns an intent for starting an activity
	 * @param screenGettingFunction  function that gets a screen from an intent
	 * @throws IllegalArgumentException if the screen is already registered
	 */
	public <ScreenT extends Screen> void registerActivity(Class<ScreenT> screenClass, Class<? extends Activity> activityClass,
	                                                      Function2<Context, ScreenT, Intent> intentCreationFunction, Function<Intent, ScreenT> screenGettingFunction) {
		checkThatNotRegistered(screenClass);
		mActivityRegistry.register(screenClass, activityClass, intentCreationFunction, screenGettingFunction);
	}

	/**
	 * Registers a screen represented by an activity using a custom intent creation function and a not implemented screen getting function.
	 * <p>
	 * A not implemented screen getting function throws {@code RuntimeException} when it is called.
	 *
	 * @param <ScreenT>              screen type
	 * @param screenClass            screen class
	 * @param activityClass          class of the activity that represents the screen. It must be the same activity class that was used
	 *                               to create an intent in {@code intentCreationFunction} or {@code null} if an intent is implicit.
	 * @param intentCreationFunction function that returns an intent for starting an activity
	 * @throws IllegalArgumentException if the screen is already registered
	 */
	public <ScreenT extends Screen> void registerActivity(Class<ScreenT> screenClass, Class<? extends Activity> activityClass, Function2<Context, ScreenT, Intent> intentCreationFunction) {
		registerActivity(screenClass, activityClass, intentCreationFunction, RegistryFunctions.getNotImplementedIntentScreenGettingFunction(screenClass));
	}

	/**
	 * Register a screen represented by an activity using a default intent creation function and a default screen getting function.
	 * <p>
	 * A default intent creation function creates an intent that starts an activity of the class {@code activityClass}. It also puts a screen to the intent's extra if {@code ScreenT} is {@code Serializable} or {@code Parcelable}.
	 * <p>
	 * A default screen getting function gets a screen from the intent's extra if {@code ScreenT} is {@code Serializable} or {@code Parcelable} and throws {@code IllegalArgumentException} otherwise.
	 *
	 * @param <ScreenT>     screen type
	 * @param screenClass   screen class. {@code ScreenT} should be {@code Serializable} or {@code Parcelable} if it contains a data that should be passed to the started activity.
	 * @param activityClass class of the activity that represents the screen
	 * @throws IllegalArgumentException if the screen is already registered
	 */
	public <ScreenT extends Screen> void registerActivity(Class<ScreenT> screenClass, Class<? extends Activity> activityClass) {
		registerActivity(screenClass, activityClass, RegistryFunctions.getDefaultIntentCreationFunction(screenClass, activityClass), RegistryFunctions.getDefaultIntentScreenGettingFunction(screenClass));
	}

	/**
	 * Registers a screen represented by a fragment using a custom fragment creation function and a custom screen getting function.
	 *
	 * @param <ScreenT>                screen type
	 * @param screenClass              screen class
	 * @param fragmentCreationFunction function that returns a new fragment
	 * @param screenGettingFunction    function that gets a screen from a fragment
	 * @throws IllegalArgumentException if the screen is already registered
	 */
	public <ScreenT extends Screen> void registerFragment(Class<ScreenT> screenClass, Function<ScreenT, Fragment> fragmentCreationFunction,
	                                                      Function<Fragment, ScreenT> screenGettingFunction) {
		checkThatNotRegistered(screenClass);
		mFragmentRegistry.register(screenClass, fragmentCreationFunction, screenGettingFunction);
	}

	/**
	 * Registers a screen represented by a fragment using a custom fragment creation function and a not implemented screen getting function.
	 * <p>
	 * A not implemented screen getting function throws {@code RuntimeException} when it is called.
	 *
	 * @param <ScreenT>                screen type
	 * @param screenClass              screen class
	 * @param fragmentCreationFunction function that returns a new fragment
	 * @throws IllegalArgumentException if the screen is already registered
	 */
	public <ScreenT extends Screen> void registerFragment(Class<ScreenT> screenClass, Function<ScreenT, Fragment> fragmentCreationFunction) {
		registerFragment(screenClass, fragmentCreationFunction, RegistryFunctions.getNotImplementedFragmentScreenGettingFunction(screenClass));
	}

	/**
	 * Registers a screen represented by a fragment using a default fragment creation function and a default screen getting function.
	 * <p>
	 * A default fragment creation function creates a fragment of the class {@code fragmentClass}. It also puts a screen to the fragment's arguments if {@code ScreenT} is {@code Serializable} or {@code Parcelable}.
	 * <p>
	 * A default screen getting function gets a screen from the fragment's arguments if {@code ScreenT} is {@code Serializable} or {@code Parcelable} and throws {@code IllegalArgumentException} otherwise.
	 *
	 * @param <ScreenT>     screen type
	 * @param screenClass   screen class. {@code ScreenT} should be {@code Serializable} or {@code Parcelable} if it contains a data that should be passed to the created fragment.
	 * @param fragmentClass class of the fragment that represents the screen
	 * @throws IllegalArgumentException if the screen is already registered
	 */
	public <ScreenT extends Screen> void registerFragment(Class<ScreenT> screenClass, Class<? extends Fragment> fragmentClass) {
		registerFragment(screenClass, RegistryFunctions.getDefaultFragmentCreationFunction(screenClass, fragmentClass), RegistryFunctions.getDefaultFragmentScreenGettingFunction(screenClass));
	}

	/**
	 * Registers a screen represented by a dialog fragment using a custom dialog fragment creation function and a custom screen getting function.
	 *
	 * @param <ScreenT>                      screen type
	 * @param screenClass                    screen class
	 * @param dialogFragmentCreationFunction function that returns a new dialog fragment
	 * @param screenGettingFunction          function that gets a screen from a fragment
	 * @throws IllegalArgumentException if the screen is already registered
	 */
	public <ScreenT extends Screen> void registerDialogFragment(Class<ScreenT> screenClass, Function<ScreenT, DialogFragment> dialogFragmentCreationFunction,
	                                                            Function<DialogFragment, ScreenT> screenGettingFunction) {
		checkThatNotRegistered(screenClass);
		mDialogFragmentRegistry.register(screenClass, dialogFragmentCreationFunction, screenGettingFunction);
	}

	/**
	 * Registers a screen represented by a dialog fragment using a custom dialog fragment creation function and a not implemented screen getting function.
	 * <p>
	 * A not implemented screen getting function throws {@code RuntimeException} when it is called.
	 *
	 * @param <ScreenT>                      screen type
	 * @param screenClass                    screen class
	 * @param dialogFragmentCreationFunction function that returns a new dialog fragment
	 * @throws IllegalArgumentException if the screen is already registered
	 */
	public <ScreenT extends Screen> void registerDialogFragment(Class<ScreenT> screenClass, Function<ScreenT, DialogFragment> dialogFragmentCreationFunction) {
		registerDialogFragment(screenClass, dialogFragmentCreationFunction, RegistryFunctions.getNotImplementedDialogFragmentScreenGettingFunction(screenClass));
	}

	/**
	 * Registers a screen represented by a dialog fragment using a default dialog fragment creation function and a default screen getting function.
	 * <p>
	 * A default dialog fragment creation function creates a dialog fragment of the class {@code dialogFragmentClass}. It also puts a screen to the fragment's arguments if {@code ScreenT} is {@code Serializable} or {@code Parcelable}.
	 * <p>
	 * A default screen getting function gets a screen from the fragment's arguments if {@code ScreenT} is {@code Serializable} or {@code Parcelable} and throws {@code IllegalArgumentException} otherwise.
	 *
	 * @param <ScreenT>           screen type
	 * @param screenClass         screen class. {@code ScreenT} should be {@code Serializable} or {@code Parcelable} if it contains a data that should be passed to the created dialog fragment.
	 * @param dialogFragmentClass class of the dialog fragment that represents the screen
	 * @throws IllegalArgumentException if the screen is already registered
	 */
	public <ScreenT extends Screen> void registerDialogFragment(Class<ScreenT> screenClass, Class<? extends DialogFragment> dialogFragmentClass) {
		registerDialogFragment(screenClass, RegistryFunctions.getDefaultDialogFragmentCreationFunction(screenClass, dialogFragmentClass), RegistryFunctions.getDefaultDialogFragmentScreenGettingFunction(screenClass));
	}

	/**
	 * Register a screen that can return a result using a custom activity result creation function and a custom screen result getting function.
	 * <p>
	 * This method is supported only for a screen that is represented by an activity (a screen should be registered with one of the {@code registerActivity} methods before calling this method).
	 *
	 * @param <ScreenResultT>                screen result type
	 * @param screenClass                    screen class
	 * @param screenResultClass              class of the result that the screen can return
	 * @param activityResultCreationFunction function that converts a {@link ScreenResult} to an {@link ActivityResult}. Passed {@link ScreenResult} can be {@code null} when a screen has finished without no result.
	 * @param screenResultGettingFunction    function that converts an {@link ActivityResult} to a {@link ScreenResult}
	 * @throws IllegalArgumentException if the screen is already registered for result, or if the screen is not represented by an activity
	 */
	public <ScreenResultT extends ScreenResult> void registerScreenForResult(Class<? extends Screen> screenClass, Class<ScreenResultT> screenResultClass,
	                                                                         Function<ScreenResultT, ActivityResult> activityResultCreationFunction,
	                                                                         Function<ActivityResult, ScreenResultT> screenResultGettingFunction) {
		checkThatCanBeRegisteredForResult(screenClass, screenResultClass);
		mScreenForResultRegistry.register(screenClass, screenResultClass, activityResultCreationFunction, screenResultGettingFunction);
	}

	/**
	 * Register a screen that can return a result using a not implemented activity result creation function and a custom screen result getting function.
	 * <p>
	 * This method is supported only for a screen that is represented by an activity (a screen should be registered with one of the {@code registerActivity} methods before calling this method).
	 * <p>
	 * A not implemented activity result creation function throws {@code RuntimeException} when it is called.
	 *
	 * @param <ScreenResultT>             screen result type
	 * @param screenClass                 screen class
	 * @param screenResultClass           class of the result that the screen can return
	 * @param screenResultGettingFunction function that converts an {@link ActivityResult} to a {@link ScreenResult}
	 * @throws IllegalArgumentException if the screen is already registered for result, or if the screen is not represented by an activity
	 */
	public <ScreenResultT extends ScreenResult> void registerScreenForResult(Class<? extends Screen> screenClass, Class<ScreenResultT> screenResultClass,
	                                                                         Function<ActivityResult, ScreenResultT> screenResultGettingFunction) {
		registerScreenForResult(screenClass, screenResultClass, RegistryFunctions.getNotImplementedActivityResultCreationFunction(screenClass, screenResultClass), screenResultGettingFunction);
	}

	/**
	 * Register a screen that can return a result using a default activity result creation function and a default screen result getting function.
	 * <p>
	 * This method is supported only for a screen that is represented by an activity (a screen should be registered with one of the {@code registerActivity} methods before calling this method).
	 * {@code ScreenResultT} must be {@code Sezializable}.
	 * <p>
	 * A default activity result creation function returns {@code ActivityResult(Activity.RESULT_OK, data)} (where {@code data} contains a serialized screen result) if a screen result is not {@code null},
	 * and it returns {@code ActivityResult(Activity.RESULT_CANCELED, null)} otherwise.
	 * <p>
	 * A default screen result getting function returns a deserialized screen result if an activity result has a data and its result code is Activity.RESULT_OK, and it returns {@code null} otherwise.
	 *
	 * @param <ScreenResultT>   screen result type
	 * @param screenClass       screen class
	 * @param screenResultClass class of the result that the screen can return. {@code ScreenT} must be {@code Serializable} or {@code Parcelable}.
	 * @throws IllegalArgumentException if the screen is already registered for result, or if the screen is not represented by an activity, or if the screen result is not {@code Serializable} or {@code Parcelable}
	 */
	public <ScreenResultT extends ScreenResult> void registerScreenForResult(Class<? extends Screen> screenClass, Class<ScreenResultT> screenResultClass) {
		registerScreenForResult(screenClass, screenResultClass, RegistryFunctions.getDefaultActivityResultCreationFunction(screenResultClass), RegistryFunctions.getDefaultScreenResultGettingFunction(screenResultClass));
	}

	@Override
	public ViewType getViewType(Class<? extends Screen> screenClass) {
		if (mActivityRegistry.isRegistered(screenClass)) {
			return ViewType.ACTIVITY;
		} else if (mFragmentRegistry.isRegistered(screenClass)) {
			return ViewType.FRAGMENT;
		} else if (mDialogFragmentRegistry.isRegistered(screenClass)) {
			return ViewType.DIALOG_FRAGMENT;
		} else {
			return ViewType.UNKNOWN;
		}
	}

	@Override
	public Class<? extends Activity> getActivityClass(Class<? extends Screen> screenClass) {
		return mActivityRegistry.getActivityClass(screenClass);
	}

	@Override
	public Intent createActivityIntent(Context context, Screen screen) {
		Intent intent = mActivityRegistry.createActivityIntent(context, screen);
		ScreenClassUtils.putScreenClass(intent, screen.getClass());
		return intent;
	}

	@Override
	public Class<? extends Screen> getScreenClass(Activity activity) {
		Class<? extends Screen> screenClass = ScreenClassUtils.getScreenClass(activity, this);

		if (screenClass == null) {   // May be this activity is a home screen. Try to find it in ActivityRegistry.
			for (Class<? extends Screen> sc : mActivityRegistry.getScreenClasses()) {
				if (mActivityRegistry.getActivityClass(sc) == activity.getClass()) {
					screenClass = sc;
					break;
				}
			}
		}
		return screenClass;
	}

	@Override
	public Screen getScreen(Activity activity) {
		Class<? extends Screen> screenClass = getScreenClass(activity);
		if (screenClass == null) {
			throw new IllegalArgumentException("Failed to get screen class.");
		}
		return mActivityRegistry.getScreen(activity, screenClass);
	}

	@Override
	public Fragment createFragment(Screen screen) {
		Fragment fragment = mFragmentRegistry.createFragment(screen);
		ScreenClassUtils.putScreenClass(fragment, screen.getClass());
		return fragment;
	}

	@Override
	public Class<? extends Screen> getScreenClass(Fragment fragment) {
		return ScreenClassUtils.getScreenClass(fragment);
	}

	@Override
	public Screen getScreen(Fragment fragment) {
		Class<? extends Screen> screenClass = getScreenClass(fragment);
		if (screenClass == null) {
			throw new IllegalArgumentException("Failed to get screen class.");
		}
		return mFragmentRegistry.getScreen(fragment, screenClass);
	}

	@Override
	public DialogFragment createDialogFragment(Screen screen) {
		DialogFragment dialogFragment = mDialogFragmentRegistry.createDialogFragment(screen);
		ScreenClassUtils.putScreenClass(dialogFragment, screen.getClass());
		return dialogFragment;
	}

	@Override
	public Class<? extends Screen> getScreenClass(DialogFragment dialogFragment) {
		return ScreenClassUtils.getScreenClass(dialogFragment);
	}

	@Override
	public Screen getScreen(DialogFragment dialogFragment) {
		Class<? extends Screen> screenClass = getScreenClass(dialogFragment);
		if (screenClass == null) {
			throw new IllegalArgumentException("Failed to get screen class.");
		}
		return mDialogFragmentRegistry.getScreen(dialogFragment, screenClass);
	}

	@Override
	public boolean isScreenForResult(Class<? extends Screen> screenClass) {
		return mScreenForResultRegistry.isRegistered(screenClass);
	}

	@Override
	public int getRequestCode(Class<? extends Screen> screenClass) {
		return mScreenForResultRegistry.getRequestCode(screenClass);
	}

	@Override
	public Class<? extends Screen> getScreenClass(int requestCode) {
		for (Class<? extends Screen> screenClass : mScreenForResultRegistry.getScreenClasses()) {
			if (mScreenForResultRegistry.getRequestCode(screenClass) == requestCode) {
				return screenClass;
			}
		}
		return null;
	}

	@Override
	public Class<? extends ScreenResult> getScreenResultClass(Class<? extends Screen> screenClass) {
		return mScreenForResultRegistry.getScreenResultClass(screenClass);
	}

	@Override
	public ActivityResult createActivityResult(Class<? extends Screen> screenClass, ScreenResult screenResult) {
		return mScreenForResultRegistry.createActivityResult(screenClass, screenResult);
	}

	@Override
	public ScreenResult getScreenResult(Class<? extends Screen> screenClass, ActivityResult activityResult) {
		return mScreenForResultRegistry.getScreenResult(screenClass, activityResult);
	}

	private void checkThatNotRegistered(Class<? extends Screen> screenClass) {
		if (getViewType(screenClass) != ViewType.UNKNOWN) {
			throw new IllegalArgumentException("Screen " + screenClass.getSimpleName() + " is already registered.");
		}
	}

	private void checkThatCanBeRegisteredForResult(Class<? extends Screen> screenClass, Class<? extends ScreenResult> screenResultClass) {
		if (isScreenForResult(screenClass)) {
			throw new IllegalArgumentException("Screen " + screenClass.getSimpleName() + " is already registered for result.");
		}

		ViewType viewType = getViewType(screenClass);
		if (viewType == ViewType.UNKNOWN) {
			throw new IllegalArgumentException("Screen " + screenClass.getSimpleName() + " should be registered with one of the registerActivity methods before registering it for result.");
		}

		if (viewType != ViewType.ACTIVITY) {
			throw new IllegalArgumentException("Can't register a screen " + screenClass.getSimpleName() + " for result. Only a screen represented by an activity can be registered for result.");
		}
	}
}
