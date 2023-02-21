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
	        implementation 'com.github.Mayur907:inAppUpdate:1.1.1'
	}
```

### In Your Code
```
java
Intent intent = new Intent(this, InAppUpdate.class);
        intent.putExtra("key", Type.IMMEDIATE);
      //intent.putExtra("key", Type.FLEXIBLE);
        startActivityForResult(intent, 123);

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
## About Me
Follow me at [Mayur907](https://github.com/Mayur907).
