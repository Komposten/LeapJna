Some of the features specified in the LeapC API reference are broken or non-existent in the current Leap SDK (v. 4.0.0+52173). These things are not issues with LeapJNA, but the Leap SDK itself. A list of these features can be found below.

## Broken features
These are features in the API which do not seem to work in the expected way.
- Device statuses are set to `eLeapDeviceStatus.None` instead of `eLeapDeviceStatus.Paused` when the service is paused (verified by dumping the memory of `LEAP_DEVICE_STATUS_CHANGE_EVENT`s).
- If the service is paused and `LeapPollConnection()` runs out of messages, it will never return, even if a timeout is specified (verified by pausing the service while running the SDK's CallbackSample).
    - This means that calling `LeapPollConnection()` when the service is paused will block the service indefinitely. This makes it impossible to resume tracking using `LeapSetPause(hConnection, false)`, even if it's called in a separate thread.
- Clock rebasing does some weird stuff (verified using the SDK's InterpolationSample).
     1. Let's say that you create a clock rebaser and update it with the current application time and Leap Motion time.
     2. Now you wait 0.1 seconds and then pass the current application time to `LeapRebaseClock()` along with your rebaser.
     3. You would expect the result after rebasing the current application time to be close to the current Leap Motion time, yet the rebased time will be 0.1 seconds in the past (but not identical to the time you used when updating the rebaser in step 1).
     - If you had waited 1 second in step 2, the rebasing in step 3 would give you a rebased time 1 second in the past instead.

## Missing features
These are features listed in the API reference and the leapc.h file in the SDK but do not exist in the SDK binaries (and can therefore not be used).
- `LeapCameraMatrix()`
- `LeapDistortionCoeffs()`
- `eLeapEventType.ImageComplete` and `.ImageRequestError` (as far as I know there is no function to request an image)
