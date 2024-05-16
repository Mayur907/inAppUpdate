# InAppUpdateLibrary
[![](https://jitpack.io/v/Mayur907/inAppUpdate.svg)](https://jitpack.io/#Mayur907/inAppUpdate)

>The simplest InAppUpdate library, set IMMEDIATE or FLEXIBLE in a few steps!
 

## How To Use
### Install
from JitPack:

Add it in your root build.gradle at the end of repositories
```
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}

```
Then, add the library to your module `build.gradle`
Add the dependency
```
dependencies {
	       implementation 'com.github.Mayur907:inAppUpdate:1.1.10'
	}
```

### In Your Code
```
/* start InAppUpadateActivity to check if an update is available or not in your splash screen on create() Method*/
        startInAppUpdateActivity();
	
private void startInAppUpdateActivity() {
        Intent intent = new Intent(this, InAppUpdate.class);
        intent.putExtra("key", Type.IMMEDIATE); /* Type.IMMEDIATE, Type.FLEXIBLE */
	inAppActivity.launch(intent)
    }

get a result like this
ActivityResultLauncher<Intent> inAppActivity = registerForActivityResult(
    new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // handle callback
                if (result.getResultCode() == RESULT_OK) {
                    createTimer();
                } else {
                    Intent data = result.getData();
                    /* check the below condition for if it's an immediate update and the user cancels the update so can't move the user to the next screen
                    * and finish activity */
                    if (data != null && data.getIntExtra("from", -1) == Type.IMMEDIATE.getCode()) {
                        finish();
                    } else {
                        createTimer();
                    }
                }
            });
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
        <item name="android:windowAnimationStyle">@null</item>
        <item name="android:statusBarColor">@android:color/transparent</item>
        <item name="android:navigationBarColor">@android:color/transparent</item>
    </style>


<!-- Change the theme in value-v26 and update this line: "The problem causing crashes is that API 26 doesn't support windowIsTranslucent."-->
	<item name="android:windowIsTranslucent">false</item>

```

## About Me
Follow me at [Mayur907](https://github.com/Mayur907).
