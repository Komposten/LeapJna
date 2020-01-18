# LeapJna

**Java bindings for the Leap Motion Orion SDK**

LeapJna is a library that provides Java bindings for the [Leap Motion](https://www.leapmotion.com/) [Orion SDK](https://developer.leapmotion.com/get-started). The official Java bindings were deprecated in SDK version 3 and can not be used at all with SDK version 4. LeapJna uses [Java Native Access](https://github.com/java-native-access/jna) to create an interface for communicating with the Leap Motion SDK's LeapC API to fetch tracking data from a Leap Motion Controller.

## Supported API features
* Create and open device connections
* Read current frame time
* Poll the device for events
* Read full tracking data from tracking events