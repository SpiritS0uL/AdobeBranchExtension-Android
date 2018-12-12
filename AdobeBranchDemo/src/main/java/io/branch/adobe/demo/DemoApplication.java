package io.branch.adobe.demo;

import android.app.Application;
import android.util.Log;

import com.adobe.marketing.mobile.AdobeCallback;
import com.adobe.marketing.mobile.ExtensionError;
import com.adobe.marketing.mobile.ExtensionErrorCallback;
import com.adobe.marketing.mobile.Identity;
import com.adobe.marketing.mobile.InvalidInitException;
import com.adobe.marketing.mobile.Lifecycle;
import com.adobe.marketing.mobile.LoggingMode;
import com.adobe.marketing.mobile.MobileCore;
import com.adobe.marketing.mobile.Signal;
import com.adobe.marketing.mobile.UserProfile;

import io.branch.adobe.extension.AdobeBranch;
import io.branch.adobe.extension.AdobeBranchExtension;
import io.branch.referral.*;

public class DemoApplication extends Application {
    private static final String TAG = "DemoApplication::";
    private static final String ADOBE_APP_ID = "launch-ENf4e5fbcc0c5945de846341e9332df247-development";

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize
        initBranch();
        initAdobeBranch();
        registerAdobeBranchExtension();
    }

    private void initBranch() {
        Branch.enableLogging();

        // TODO: Revisit.  This is how we should encourage customers to initialize Branch using Branch.
        // Branch.getAutoInstance(this);
    }

    private void initAdobeBranch() {
        Log.d(TAG, "initAdobeBranch()");

        // TODO: Revisit.  We should encourage customers to initialize Branch using Branch.
        AdobeBranch.getAutoInstance(this);

        MobileCore.setApplication(this);
        MobileCore.setLogLevel(LoggingMode.DEBUG);

        try {
            UserProfile.registerExtension();
            Identity.registerExtension();
            Lifecycle.registerExtension();
            Signal.registerExtension();
            MobileCore.start(new AdobeCallback () {
                @Override
                public void call(Object o) {
                    MobileCore.configureWithAppID(ADOBE_APP_ID);
                }
            });
        } catch (InvalidInitException e) {
            Log.e(TAG, "InitException", e);
        }
    }

    private void registerAdobeBranchExtension() {
        MobileCore.setApplication(this);

        ExtensionErrorCallback<ExtensionError> errorCallback = new ExtensionErrorCallback<ExtensionError>() {
            @Override
            public void error(final ExtensionError extensionError) {
                Log.e(TAG, String.format("An error occurred while registering the AdobeBranchExtension %d %s", extensionError.getErrorCode(), extensionError.getErrorName()));
            }
        };

        if (!MobileCore.registerExtension(AdobeBranchExtension.class, errorCallback)) {
            Log.e(TAG, "Failed to register the AdobeBranchExtension extension");
        }
    }
}
