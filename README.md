# RateUsLibrary
[![](https://jitpack.io/v/Mayur907/inAppUpdate.svg)](https://jitpack.io/#Mayur907/inAppUpdate)

>The simplest InAppUpdate library, create your own design in a few steps!
 

## How To Use
### Install
from JitPack:

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):
```
gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

```
Then, add the library to your module `build.gradle`
```
gradle
dependencies {
	        implementation 'com.github.Mayur907:inAppUpdate:1.0.0'
	}
```

### In Your Code
```
java
Intent intent = new Intent(this, InAppUpdate.class);
        intent.putExtra("key", Type.IMMEDIATE);
//        intent.putExtra("key", Type.FLEXIBLE);
        startActivityForResult(intent, 123);

get result like this
@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("update", "splash requestCode :: " + requestCode);
        Log.e("update", "splash resultCode :: " + resultCode);
        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                createTimer(COUNTER_TIME);
            } else /*resultCode == RESULT_CANCELED*/ {
                if (data != null) {
                    Log.e("update", "splash from :: " + data.getIntExtra("from", 0));
                    /*check return from immediate or flexible
                    * if return from immediate so app close */
                    if(data.getIntExtra("from", 0) == 123456){
                        finish();
                    } else {
                        createTimer(COUNTER_TIME);
                    }
                } else {
                    createTimer(COUNTER_TIME);
                }
            }
        }
    }
```
## About Me
Follow me at [Mayur907](https://github.com/Mayur907).
