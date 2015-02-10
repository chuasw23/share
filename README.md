
PhoneGap Toast Plugin

Author: munnadroid
License: The MIT License
Tested on PhoneGap/Cordova 3.1.0
Installation Instruction If You Like :D

Copy \plugins\org.apache.cordova.plugin\www\toast.js to \assets\www\ folder

Copy ToastPlugin.java to your src folder in package org.apache.cordova.plugin or add all \org\apache\cordova\plugin\ToastPlugin.java if you don't already have the folders created.

Edit res\xml\config.xml (or wherever your config.xml is), add

<feature name="ToastPlugin">
    <param name="android-package" value="org.apache.cordova.plugin.ToastPlugin" />
</feature>
at the end of the file, before </widget>

Edit assets\www\cordova_plugins.js, add
{
    "file": "plugins/org.apache.cordova.plugin/www/toast.js",
    "id": "org.apache.cordova.plugin.ToastPlugin",
    "runs": true
}
as an element of the array module.exports

How To Use

Use Toast.longshow
Toast.longshow(Message [,SuccessCallback, FailureCallback]);
Use Toast.shortshow
Toast.shortshow(Message [,SuccessCallback, FailureCallback]);
Example

Toast.longshow("Hello, I am long Toast. ");

Toast.shortshow("Hello, I am short Toast. ");
PULL REQUEST are damn welcome !!!
