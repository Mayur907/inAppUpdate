# InAppUpdateLibrary
[![](https://jitpack.io/v/Mayur907/inAppUpdate.svg)](https://jitpack.io/#Mayur907/inAppUpdate)

>The simplest InAppUpdate library, set IMMEDIATE or FLEXIBLE in few steps!
 

## How To Use
### Install
from JitPack:

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

```
Then, add the library to your module `build.gradle`
```
dependencies {
	       implementation 'com.github.Mayur907:inAppUpdate:1.1.5'
	}
```

### In Your Code
```
/* check if update is available or not */
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                startInAppUpdateActivity();
            } else {
                createTimer(COUNTER_TIME);
            }
            Log.e("update", " addOnSuccessListener ");
        });

        appUpdateInfoTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.e("update", " addOnFailureListener ");
                createTimer(COUNTER_TIME);
            }
        });
	
private void startInAppUpdateActivity() {
        Intent intent = new Intent(this, InAppUpdate.class);
        intent.putExtra("key", Type.IMMEDIATE); /* Type.IMMEDIATE, Type.FLEXIBLE */
        startActivityForResult(intent, 123);
    }

get result like this
@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);       
        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                //start your activity
            } else /*resultCode == RESULT_CANCELED*/ {
                if (data != null) {
                    //get data from other activity if pass
		    
                    /*check return from immediate or flexible
                    * if return from immediate so app close */
                    if(data.getIntExtra("from", 0) == 123456){
                        finish();
                    } else {
                        //start your activity
                    }
                } else {
                   //start your activity
                }
            }
        }
    }
```

### In Your Manifest.xml
```
<activity android:name="inappupdate.updateimmediate.updateflexible.InAppUpdate"
            android:theme="@style/Theme.AppCompat.Translucent" />
```	

### In Your theme.xml
```
<!--add below code in themes.xml file-->
    <!--below is the style for transparent activity and here we are using no action bar.-->
    <style name="Theme.AppCompat.Translucent" parent="Theme.AppCompat.NoActionBar">
        <!--on below line we are setting background as transparent color-->
        <item name="android:background">@android:color/transparent</item>
        <!--on below line we are displaying the windowNotitle as true as we are not displaying our status bar-->
        <item name="android:windowNoTitle">true</item>
        <!--on below line we are setting our window background as transparent color-->
        <item name="android:windowBackground">@android:color/transparent</item>
        <!--on below line we are setting color background cache hint as null-->
        <item name="android:colorBackgroundCacheHint">@null</item>
        <!--on below line we are adding a window translucent as true-->
        <item name="android:windowIsTranslucent">true</item>
        <!--on below line we are adding a window animationstyle-->
        <item name="android:windowAnimationStyle">@android:style/Animation</item>
    </style>
```

## About Me
Follow me at [Mayur907](https://github.com/Mayur907).
