# LeapJna

[![Java CI](https://github.com/Komposten/LeapJna/workflows/Java%20CI/badge.svg?branch=unit-testing)](https://github.com/Komposten/LeapJna/actions?query=workflow%3A"Java+CI") [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Komposten_LeapJna&metric=alert_status)](https://sonarcloud.io/dashboard?id=Komposten_LeapJna) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Komposten_LeapJna&metric=coverage)](https://sonarcloud.io/dashboard?id=Komposten_LeapJna) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Komposten_LeapJna&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=Komposten_LeapJna)

**Java bindings for the Ultraleap SDK**

LeapJna is a library that provides Java bindings for the [Ultraleap](https://www.ultraleap.com) (formerly Leap Motion) [SDK](https://docs.ultraleap.com). The official Java bindings were deprecated in the Orion SDK 3 and can not be used at all with Orion SDK 4. LeapJna uses [Java Native Access](https://github.com/java-native-access/jna) to create an interface for communicating with the Ultraleap SDK's LeapC API to fetch tracking data from an Ultraleap Tracking device.

**Note**: Neither LeapJna nor I (Komposten) am associated with [Ultraleap Ltd](https://ultraleap.com/).

## Supported API features
LeapJna includes Java bindings for all functionality in the following Ultraleap C API versions:

Ultraleap SDK | LeapJna version
------------ | -------------
4.0.0+52173 | 1.0.2
4.1.0+52211 | 1.0.2
5.0.0-preview+52386 | 1.1.0
5.6.1.0 | 1.2.0


### Untested API bindings
LeapJna's bindings for some of the API features have not been tested with an actual UltraLeap Tracking device. Those are as follows:
* Device failure events (I don't have a failing device to test this)
* Point mappings (I'm not sure what they are and how to enable them; deprecated in LeapJna 1.2.0)
* Eye events (I don't think the Leap Motion Controller peripheral device supports this)
* IMU events (I don't think the Leap Motion Controller peripheral device supports this)

These _should_ still work, as I have tested the bindings using a mock Leap C API, but I can't guarantee it.

### Broken API features
Some features specified in the Ultraleap SDK are broken or non-functional in the C API as far as I can see, which means that they won't work as expected in LeapJna either.

A list of these features can be found here: [Broken API Features](BROKEN_API_FEATURES.md).

## Using LeapJna in your project
1. Add LeapJna to your classpath.
    * If you don't use Maven/Gradle you will need to add JNA as well.
2. Download the UltraLeap SDK, put LeapC.dll in a folder "win32-x86-64" and add that folder to your classpath.
    * If you want to use a different location for the DLL, or if you're using a different operating system than 64-bit Windows, refer to [JNA's documentation for library loading](http://java-native-access.github.io/jna/5.5.0/javadoc/com/sun/jna/NativeLibrary.html) for options.
3. Import LeapC in your Java class: `import komposten.leapjna.leapc.LeapC;`
4. Obtain the LeapC instance using `LeapC.INSTANCE`.
    * Use this instance to access the LeapC API's functions.
    * All related data structures can be found in the `komposten.leapjna.leapc` packages.
5. Refer to the official SDK guide for the basics of using the LeapC API.
    * Refer to the documentation of each method and data structure in LeapJna for information on how to use them.

## 2D visualiser example
LeapJna has an example application which takes the form of a simple visualiser. To launch this:
1. Clone LeapJna.
2. Open LeapJna as a Maven project in your preferred IDE.
3. Launch `komposten.leapjna.example.VisualiserExample`.

## License
The license for LeapJna's source code can be found in the [LICENSE](LICENSE) file.

Since most of the documentation for the methods in [LeapC.java](src/main/java/komposten/leapjna/leapc/LeapC.java) and classes in [komposten.leapjna.leapc.*](src/main/java/komposten/leapjna/leapc) originally comes from the official [UltraLeap API Reference](https://docs.ultraleap.com/tracking-api/api-reference.html), it falls under the same license as the official reference.

The Ultraleap SDK (not included in this repository) is licensed according to the [Ultraleap Tracking SDK Agreement](https://central.leapmotion.com/agreements/SdkAgreement).
