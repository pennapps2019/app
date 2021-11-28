![](readme-img/logo-with-text.png)

# Android Application

Android application which shows study space "hotspots" and current usage, crowdsourced from students who are currently studying in the area.

## Setup

### Android

Install [Android Studio](https://developer.android.com/studio/).

#### Google Maps API Key (for development builds)

Generate a [Google Maps SDK API key (step 1)](https://developers.google.com/maps/documentation/android-sdk/get-api-key).

In the file `app/src/debug/res/values/google_maps_api.xml`, replace the value of the placeholder key with your API key.

Build and run the Android application on a physical or virtual device.


#### Creating a Production Release

##### Step 1: Building a Release Variant

Create a new file in the root directory to hold the secure keystore details:
```shell
cp keystore.properties.template keystore.properties
```

This file will be ignored by Git. Set your keystore information in this file.

Under _Build_, click _Select Build Variant_.

In the _Build Variants_ view which appears, change the **Active Build Variant** from _debug_ to _release_.

Build the application normally to your phone. The keystore credentials will be verified and applied.

##### Step 2: Creating the Signed App

Under _Build_, click _Generate Signed Bundle / APK_.

Follow the instructions, inputting your keystore path and credentials, to create a signed app which can be uploaded to the app store.

