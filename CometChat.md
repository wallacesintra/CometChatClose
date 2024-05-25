# Comet Chat

## Comet chat dependencies

in project level build.gradle file:

```kotlin
allprojects {
    repositories {
        maven {
            url = uri("https://dl.cloudsmith.io/public/cometchat/cometchat/maven/")
        }
    }
}
```

in the app level build.gradle file:

```kotlin
android {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("com.cometchat:chat-sdk-android:4.0.6")
}
```

## Initialise CometChat

`init()` - setting for CometChat. Parameters:

1. appID - CometChat App ID
2. appSetting - object of AppSetting class:
    * Region: region where app was created (mandatory)
    * Presence Subscription: represents the subscription type for user presence(real-time/offline status)
    * autoEstablishSocketConnection(boolean value): when set to true informs the SDK to manage the web-socket connection internally, set to false, it infrom the SDK that web-socket connection will be managed manually. Default value is `true`
    * overrideAdminHost(adminHost: string): takes the admin URL as input and uses this admin URL instead of the default admin URL. This can be used in case of dedicated deployment of CometChat.
    * overrideClientHost(clientHost: string): takes the client URL as input and uses this client URL instead of the default client URL. This can be used in case of dedicated deployment of CometChat.

```kotlin
val appID:String="APP_ID" // Replace with your App ID
val region:String="REGION" // Replace with your App Region ("eu" or "us")

val appSettings = AppSettings.AppSettingsBuilder()
  .subscribePresenceForAllUsers()
  .setRegion(region)
  .autoEstablishSocketConnection(true)
  .build()  

CometChat.init(this,appID,appSettings, object : CometChat.CallbackListener<String>() {
 override fun onSuccess(p0: String?) {
    Log.d(TAG, "Initialization completed successfully")
  }

  override fun onError(p0: CometChatException?) {
    Log.d(TAG, "Initialization failed with exception: " + p0?.message)
  }
  
 })
```

### Register and Login user

use `createUser()`, takes an User object and the Auth key as input parameters.
UID and name are mandatory

```kotlin
val authKey = "AUTH_KEY" // Replace with your App Auth Key
val user = User()
user.uid = "user1" // Replace with the UID for the user to be created
user.name = "Kevin" // Replace with the name of the user

CometChat.createUser(user, authKey, object : CometChat.CallbackListener<User>() {
  override fun onSuccess(user: User) {
    Log.d("createUser", user.toString())
  }

  override fun onError(e: CometChatException) {
    Log.e("createUser", e.message)
  }
})
```

after creating the user successfully, log in the user using `login()` method once.

```kotlin
val UID:String="user1" // Replace with the UID of the user to login
val authKey:String="AUTH_KEY" // Replace with your App Auth Key

if (CometChat.getLoggedInUser() == null) {   
CometChat.login(UID,authKey, object : CometChat.CallbackListener<User>() {
     override fun onSuccess(p0: User?) {
         Log.d(TAG, "Login Successful : " + p0?.toString())        
      }

     override fun onError(p0: CometChatException?) {
         Log.d(TAG, "Login failed with exception: " +  p0?.message)        
      }

   })
}else{
   // User already logged in
 }
```
