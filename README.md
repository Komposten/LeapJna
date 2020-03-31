# LeapJna

**Java bindings for the Leap Motion Orion SDK**

LeapJna is a library that provides Java bindings for the [Leap Motion](https://www.leapmotion.com/) [Orion SDK](https://developer.leapmotion.com/get-started). The official Java bindings were deprecated in SDK version 3 and can not be used at all with SDK version 4. LeapJna uses [Java Native Access](https://github.com/java-native-access/jna) to create an interface for communicating with the Leap Motion SDK's LeapC API to fetch tracking data from a Leap Motion Controller.

## Supported API features
* Create and open device connections
* Read current frame time
* Poll the device for events
* Read full tracking data from tracking events
* Frame interpolation

## Using LeapJNA
1. Add LeapJNA to your classpath.
    * If you don't use Maven/Gradle you will need to add JNA as well.
2. Download the Leap Motion SDK, put LeapC.dll and LeapC.lib in a folder "win32-x86-64" and add that folder to your classpath.
    * If you want to use a different location for the DLL, or if you're using a different operating system than 64-bit Windows, refer to [JNA's documentation for library loading](http://java-native-access.github.io/jna/5.5.0/javadoc/com/sun/jna/NativeLibrary.html) for options.
3. Import LeapC in your Java class: `import komposten.leapjna.LeapC;`
4. Obtain an instance of LeapC using `LeapC.INSTANCE`.
    * Use this instance to access the LeapC API's functions.
    * All related data structures can be found in the `komposten.leapjna.leapc` packages.
5. Refer to the official SDK guide for the basics of using the LeapC API.