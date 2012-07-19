package com.w3i.torch.publisher;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.w3i.advertiser.W3iAdvertiser;
import com.w3i.advertiser.W3iConnect;
import com.w3i.common.Log;
import com.w3i.offerwall.ApplicationInputs;
import com.w3i.offerwall.DialogInputs;
import com.w3i.offerwall.W3iClickListener;
import com.w3i.offerwall.W3iListener;
import com.w3i.offerwall.W3iPublisher;
import com.w3i.offerwall.business.OfferBasic;
import com.w3i.offerwall.manager.BannerManager;

public class OfferwallManager {
	private static OfferwallManager instance;
	private W3iPublisher publisher;
	private W3iConnect advertiser;
	private W3iListener currencyListener = null;
	private static boolean appWasRunExecuted = false;
	private static boolean actionTakenExecuted = false;
	private static final int appId = 11103;

	private OfferwallManager(Activity activity, W3iAdvertiser listener) {
		ApplicationInputs inputs = new ApplicationInputs();
		inputs.setAppId(appId); // Application ID provided by W3i
		inputs.setApplicationName("W3i's Replica Island"); // Sets the display
															// name for your app
		inputs.setPackageName("com.w3i.torch"); // The package
												// name for your
												// app
		publisher = new W3iPublisher(activity, inputs);
		advertiser = new W3iConnect(activity, false, listener);
	}

	private static OfferwallManager getInstance() {
		if (instance == null) {
			throw new IllegalStateException("OfferwallManager not initialized. Call OfferwallManager.initialize(Activity) before calling any other method");
		}
		return instance;
	}

	public static synchronized void initialize(
			Activity activity,
			W3iAdvertiser listener) {
		if (instance != null) {
			Log.d("OfferwallManager already initialized.");
			return;
		}
		instance = new OfferwallManager(activity, listener);
	}

	public static void initialize(
			Activity activity) {
		initialize(activity, null);
	}

	public static void appWasRun() {
		if (appWasRunExecuted == false) {
			getInstance().advertiser.appWasRun(appId);
			appWasRunExecuted = true;
		} else {
			Log.d("OfferwallManager: AppWasRun already executed.");
		}

	}

	public static void setAdvertiserListener(
			W3iAdvertiser listener) {
		getInstance().advertiser.setW3iAdvertiser(listener);
	}

	public static void actionTaken(
			int actionId) {
		if (actionTakenExecuted == false) {
			getInstance().advertiser.actionTaken(actionId);
			actionTakenExecuted = true;
		} else {
			Log.d("OfferwallManager: ActionTaken already executed.");
		}
	}

	public static void createSession() {
		getInstance().publisher.createSession();
	}

	public static void showFeaturedOffer(
			Activity activity) {
		getInstance().publisher.showFeaturedOffer(activity);
	}

	public static void showUpgradeDialog(
			Activity activity,
			DialogInputs inputs,
			W3iClickListener clickListener) {
		OfferwallManager manager = getInstance();
		manager.publisher.upgradeMyApp(activity, inputs, clickListener);
	}

	public static void showRateAppDialog(
			Activity activity,
			DialogInputs inputs,
			W3iClickListener clickListener) {
		OfferwallManager manager = getInstance();
		manager.publisher.rateMyApp(activity, inputs, clickListener);
	}

	public static void showOfferwall() {
		getInstance().publisher.showOfferWall();
	}

	public static void enableLogging(
			boolean isEnabled) {
		getInstance().publisher.enableLogging(isEnabled);

	}

	public static void redeemCurrency(
			Activity activity) {
		OfferwallManager manager = getInstance();
		manager.publisher.redeemCurrency(activity, manager.currencyListener);
	}

	public static void setCurrencyRedemptionListener(
			W3iListener listener) {
		getInstance().currencyListener = listener;
	}

	public static void endSession() {
		getInstance().publisher.endSession();
	}

	public static void release() {
		OfferwallManager manager = getInstance();
		BannerManager.release();
		manager.publisher.release();
		manager.currencyListener = null;
		manager.advertiser = null;
		instance = null;
	}

	public static void showFeaturedOfferBanner(
			ViewGroup parent,
			LayoutParams params) {
		OfferwallManager manager = getInstance();
		manager.publisher.showFeaturedOfferBanner(parent, params, -1);
	}

	public static void dismissFeaturedOfferBaner() {
		OfferwallManager manager = getInstance();
		manager.publisher.dismissFeaturedOfferBanner();
	}

	public static void testBanner(
			ViewGroup parent) {
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.BOTTOM;

		OfferBasic offer = new OfferBasic();
		offer.setDisplayName("Angry Birds");
		offer.setShortConversionActionMessage("Very long convertion action message: asdasdasdasda sd asd sdas das das das das da dasdasdsadas das dasdasdasda das dasda sd asd as");
		offer.setSmallIconUrl("http://thecustomizewindows.com/wp-content/uploads/2011/11/angry-birds-games.png");

		BannerManager.createBanner(parent, params);
		BannerManager.setBannerOffer(offer);
		BannerManager.setBannerVisibility(View.VISIBLE);

	}
}
