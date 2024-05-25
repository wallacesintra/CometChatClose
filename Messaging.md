# Messaging Comet Chat

## Send a message

can send:

* text message
* media message
* custom message: sending completely custom data using JSON structure
* interactive messages: sending end-user interactive messages of type form,card and custom interactive

### Text message

send a text message to a user/group, use `sendMessage()` method and pass a `TextMessage` object to it.

`TextMessage` class constructor takes:

* receiveID - UID of the user/ GUID of the group receiveing the message(required)
* messageText - text message(required)
* receiverType - type of the receiver [`CometChatConstants.RECEIVER_TYPE_USER` or `CometChatConstants.RECEIVER_TYPE_GROUP`] (required)

#### add metadata

send custom data along with a text message, use `setMetadata` method and pass a `JSONObject` to it.

```kotlin
val metadata = JSONObject()
metadata.put("latitude", "50.6192171633316")
metadata.put("longitude", "-72.68182268750002")
textMessage.setMetadata(metadata)
```

#### add tags

add tag to a message use `setTags()` method and pass a `JSONObject` to it

```kotlin
val tags: MutableList<String> = ArrayList()
tags.add("pinned")
textMessage.setTags(tags)
```

#### send text message to a user

```kotlin

private val receiverID = "UID"
private val messageText = "Hello CometChat!"
private val receiverType = CometChatConstants.RECEIVER_TYPE_USER

val textMessage = TextMessage(receiverID, messageText, receiverType)
CometChat.sendMessage(textMessage, object : CallbackListener<TextMessage>() {
    override fun onSuccess(textMessage: TextMessage) {
        Log.d(TAG, "Message sent successfully: $textMessage")
    }

    override fun onError(e: CometChatException) {
        Log.d(TAG, "Message sending failed with exception: " + e.message)
    }
})

```

#### send text message to a group

```kotlin

private val receiverID = "GUID"
private val messageText = "Hello CometChat!"
private val receiverType = CometChatConstants.RECEIVER_TYPE_GROUP

val textMessage = TextMessage(receiverID, messageText, receiverType)
CometChat.sendMessage(textMessage, object : CallbackListener<TextMessage>() {
    override fun onSuccess(textMessage: TextMessage) {
        Log.d(TAG, "Message sent successfully: $textMessage")
    }

    override fun onError(e: CometChatException) {
        Log.d(TAG, "Message sending failed with exception: " + e.message)
    }
})

```

## Receive Messages

### Real-time messages

receiving messages when the app is running

register a message listener, you need to add `MessageListener` using `addMessageListener()` method

add the listener in the `onResume()` method of the activity or the fragment that receive the messages in.

```kotlin
val listenerID:String = "UNIQUE_LISTENER_ID"

CometChat.addMessageListener(listenerID, object : MessageListener() {
    override fun onTextMessageReceived(textMessage: TextMessage) {
        Log.d(TAG, "Text message received successfully: $textMessage")
    }

    override fun onMediaMessageReceived(mediaMessage: MediaMessage) {
        Log.d(TAG, "Media message received successfully: $mediaMessage")
    }

    override fun onCustomMessageReceived(customMessage: CustomMessage) {
        Log.d(TAG, "Custom message received successfully: $customMessage")
    }
})
```

`listenerID` - an ID that uniquely identifies listener(recommended the activity/fragment name)

remove the listener once activity/fragment is not in use.

in the `onPause()` method:

```kotlin
val listenerID:String = "UNIQUE_LISTENER_ID"

CometChat.removeMessageListener(listenerID)

```

### Missed messages

receiving messages when the app is not running, when the user was offline.

use `MessageRequest` class and filters provided by `MessagesRequestBuilder` class

need the ID of the last message received, use `getLastDeliveredMessageId()` method, pass the ID to `setMessageId()` method of the builder class

Then use `fetchNext()` method to fetch all messages that were sent to the user when they were offline.

Calling the `fetchNext()` method on the same object repeatedly allows you to fetch all the offline messages for the logged in user in a paginated manner

#### fetch missed messages of a particular one on one conversation

```kotlin
lateinit var messagesRequest: MessagesRequest
val latestId = CometChat.getLastDeliveredMessageId()
val limit: Int = 30
val UID: String = "superhero1"

val messagesRequest = MessagesRequestBuilder()
    .setMessageId(latestId)
    .setLimit(limit)
    .setUID(UID)
    .build()

messagesRequest.fetchNext(object : CallbackListener<List<BaseMessage?>>() {
    override fun onSuccess(list: List<BaseMessage?>) {
        for (message in list) {
            if (message is TextMessage) {
                Log.d(
                    TAG, "Text message received successfully: " +
                            message.toString()
                )
            } else if (message is MediaMessage) {
                Log.d(
                    TAG, "Media message received successfully: " +
                            message.toString()
                )
            }
        }
    }

    override fun onError(e: CometChatException) {
        Log.d(TAG, "Message fetching failed with exception: " + e.message)
    }
})
```

#### fetch missed messages of a particular group conversation

```kotlin
lateinit var messagesRequest: MessagesReques
val latestId = CometChat.getLastDeliveredMessageId()
val limit: Int = 30
val GUID: String = "superhero1"

messagesRequest = MessagesRequestBuilder()
    .setMessageId(latestId)
    .setLimit(limit)
    .setGUID(GUID)
    .build()

messagesRequest.fetchNext(object : CallbackListener<List<BaseMessage?>>() {
    override fun onSuccess(list: List<BaseMessage?>) {
        for (message in list) {
            if (message is TextMessage) {
                Log.d(
                    TAG, "Text message received successfully: " +
                            message.toString()
                )
            } else if (message is MediaMessage) {
                Log.d(
                    TAG, "Media message received successfully: " +
                            message.toString()
                )
            }
        }
    }

    override fun onError(e: CometChatException) {
        Log.d(TAG, "Message fetching failed with exception: " + e.message)
    }
})
```
