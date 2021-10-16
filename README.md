# chatfeed
A simple Jetpack Compose Desktop application that renders messages coming from a Twitch chat channel

## Building
Building requires JDK 11. Consider using [SDK Manager](https://sdkman.io) to manage your JDK versions.

## Running

## Short comings
Currently the Jetpack Compose Desktop preview window doesn't work when using resources from the classpath of an application. 

As a result, preview doesn't work on any of the views using fonts located in src/app/main/resources/fonts.

## What works
### Join/Part messages
When someone joins the chat or someone leaves, _eventually_ a message will be displayed.

### Private Messages
When someone sends a message, it'll be displayed. The user's configured nickname colour will be used as the basis 
for the background of the card their message will be displayed on.

