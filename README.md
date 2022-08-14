# LeapJna

[![Java CI](https://github.com/Komposten/LeapJna/workflows/Java%20CI/badge.svg?branch=unit-testing)](https://github.com/Komposten/LeapJna/actions?query=workflow%3A"Java+CI") [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Komposten_LeapJna&metric=alert_status)](https://sonarcloud.io/dashboard?id=Komposten_LeapJna) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Komposten_LeapJna&metric=coverage)](https://sonarcloud.io/dashboard?id=Komposten_LeapJna) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Komposten_LeapJna&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=Komposten_LeapJna)

**Java bindings for the Leap Motion SDK**

LeapJna is a library that provides Java bindings for the [Leap Motion](https://www.leapmotion.com/) [SDK](https://developer.leapmotion.com/get-started). The official Java bindings were deprecated in SDK version 3 and can not be used at all with SDK version 4. LeapJna uses [Java Native Access](https://github.com/java-native-access/jna) to create an interface for communicating with the Leap Motion SDK's LeapC API to fetch tracking data from a Leap Motion Controller.

**Note**: Neither LeapJna nor I (Komposten) am associated with [Ultraleap Ltd](https://ultraleap.com/).

## Supported API features
LeapJna includes Java bindings for all functionality in the following Leap Motion C API versions:

Leap SDK | LeapJna version
------------ | -------------
4.0.0+52173 | 1.0.2
4.1.0+52211 | 1.0.2
5.0.0-preview+52386 | 1.1.0


### Untested API bindings
LeapJna's bindings for some of the API features have not been tested with an actual Leap Motion device. Those are as follows:
* Device failure events (I don't have a failing device to test this)
* Point mappings (I'm not sure what they are and how to enable them)

These _should_ still work, as I have tested the bindings using a mock Leap C API, but I can't guarantee it.

### Broken API features
Some features specified in the Leap Motion API are broken or non-functional in the C API as far as I can see, which means that they won't work as expected in LeapJna either.

A list of these features can be found here: [Broken API Features](BROKEN_API_FEATURES.md).

## Using LeapJna in your project
1. Add LeapJna to your classpath.
    * If you don't use Maven/Gradle you will need to add JNA as well.
2. Download the Leap Motion SDK, put LeapC.dll in a folder "win32-x86-64" and add that folder to your classpath.
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

Since most of the documentation for the methods in [LeapC.java](src/main/java/komposten/leapjna/leapc/LeapC.java) and classes in [komposten.leapjna.leapc.*](src/main/java/komposten/leapjna/leapc) originally comes from the official [Leap Motion API Reference](https://developer.leapmotion.com/documentation/v4/modules.html), it falls under the same license as the official reference.

The Leap Motion SDK (not included in this repository) is licensed according to the [Leap Motion SDK Agreement](https://central.leapmotion.com/agreements/SdkAgreement).
