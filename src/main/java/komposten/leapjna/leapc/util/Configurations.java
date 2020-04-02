package komposten.leapjna.leapc.util;

/**
 * Constants for all configuration keys supported by the Leap Motion API and service.
 * 
 * @see <a href=
 *      "https://developer.leapmotion.com/documentation/v4/configuration.html">LeapC Guide
 *      - Configuration</a>
 */
public class Configurations
{
	/**
	 * The tracking settings control the tracking data available to an application.
	 */
	public static class Tracking
	{
		/**
		 * <p>
		 * Enables or disables image data. Set to a value of 0 to disable images; set to a
		 * value of 2 to enable images.
		 * </p>
		 * <p>
		 * Since this setting is controlled by the user, you should ask for permission before
		 * changing its value.
		 * </p>
		 * <p>
		 * Note that applications must still invoke the image policy API at runtime to receive
		 * images. Simply enabling images on the service is not sufficient.
		 * </p>
		 * <p>
		 * <b>Value type:</b> integer
		 * </p>
		 * <p>
		 * <b>Valid values:</b> 0 (disable), 1 (enable)
		 * </p>
		 */
		public static final String IMAGES_MODE = "images_mode";

		/**
		 * <p>
		 * When enabled, the Leap Motion device can be used with either the long side of the
		 * device with the green LED facing toward or away from the user. The service detects
		 * the orientation and flips the images from the device before analyzing them. When
		 * disabled, the images are never flipped and tracking recognizes hands much better
		 * when the green LED side of the device is facing the user (or downward in a HMD
		 * mount).
		 * </p>
		 * <p>
		 * Since this setting is controlled by the user, you should ask for permission before
		 * changing its value.
		 * </p>
		 * <p>
		 * <b>Value type:</b> boolean
		 * </p>
		 */
		public static final String IMAGE_PROCESSING_AUTO_FLIP = "image_processing_auto_flip";

		private Tracking()
		{}
	}

	/**
	 * The service control settings change how the Leap Motion service (Windows) or daemon
	 * (OS X and Linux) operates.
	 */
	public static class Service
	{
		/**
		 * <p>
		 * Robust mode improves tracking when excessive ambient IR light is present. However,
		 * it also lowers the framerate significantly. If the resulting framerate is too low,
		 * turning on robust mode might cause worse tracking performance than not enabling it
		 * in the first place.
		 * </p>
		 * <p>
		 * Robust mode can be enabled in the control panel and by setting the
		 * <code>robust_mode_enabled</code> configuration parameter. When enabled, the system
		 * enters robust mode when excessive ambient IR light is detected.
		 * </p>
		 * <p>
		 * Set this parameter to <code>true</code> to enable; <code>false</code> to disable.
		 * </p>
		 * <p>
		 * Since this setting is controlled by the user, you should ask for permission before
		 * changing its value.
		 * </p>
		 * <p>
		 * <b>Value type:</b> boolean
		 * </p>
		 */
		public static final String ROBUST_MODE_ENABLED = "robust_mode_enabled";

		/**
		 * <p>
		 * Since this setting is controlled by the user, you should ask for permission before
		 * changing its value.
		 * </p>
		 * 
		 * <p>
		 * <b>Value type:</b> boolean
		 * </p>
		 */
		public static final String AVOID_POOR_PERFORMANCE = "avoid_poor_performance";

		/**
		 * <p>
		 * Determines whether applications should receive tracking data when not the focused,
		 * foreground application.
		 * </p>
		 * <p>
		 * Since this setting is controlled by the user, you should ask for permission before
		 * changing its value.
		 * </p>
		 * <p>
		 * <b>Value type:</b> integer
		 * </p>
		 */
		public static final String BACKGROUND_APP_MODE = "background_app_mode";

		/**
		 * <p>
		 * Processor affinity controls which CPU cores that a process will run on in a
		 * multi-core system. You can control the processor affinity of the Leap Motion
		 * service using the cpu_affinity_mask configuration parameter. This parameter takes
		 * an integer value that is converted into a bitmask that specifies which CPU cores
		 * the threads of the service will run on (i.e. 3, which is 0011 in binary, would
		 * specify that the threads will run on cores 1 and 2). A value of 0 means that the
		 * default OS-defined behavior is unchanged.
		 * </p>
		 * <p>
		 * The mask is passed to the Windows SetProcessorAffinityMask() API. This
		 * configuration parameter is not supported on other operating systems.
		 * </p>
		 * <p>
		 * Changes to this value only take affect when the service is restarted. Generally, it
		 * makes sense to set this value in the config.json file or on the command line to
		 * LeapSvc.
		 * </p>
		 * <p>
		 * <b>Value type:</b> integer
		 * </p>
		 * <p>
		 * <b>Default value:</b> 0
		 * </p>
		 */
		public static final String CPU_AFFINITY_MAST = "cpu_affinity_mask";

		/**
		 * <p>
		 * Thread priority controls the priority of threads spawned by the Leap Motion
		 * service. Thread priority is controlled by the process_niceness configuration
		 * parameter. Specifying 0 means that the OS default priority is assigned; a value of
		 * 1-9 assigns a specific priority (1 = lowest, 9 = highest).
		 * </p>
		 * <p>
		 * Changes to this value only take affect when the service is restarted. Generally, it
		 * makes sense to set this value in the config.json file or on the command line to
		 * LeapSvc. This setting is used on Windows only.
		 * </p>
		 * <p>
		 * <b>Value type:</b> integer
		 * </p>
		 * <p>
		 * <b>Valid values:</b> 0-9 (0 = OS default, 1 = lowest, 9 = highest)
		 * </p>
		 * <p>
		 * <b>Default value:</b> 0
		 * </p>
		 */
		public static final String PROCESS_NICENESS = "process_niceness";

		/**
		 * <p>
		 * Detemines whether the Leap Motion Control panel application launches when the
		 * computer boots up. Set to <code>true</code> to suppress automatically launching the
		 * control panel; <code>false</code> (the default) to allow.
		 * </p>
		 * <p>
		 * <b>Value type:</b> boolean
		 * </p>
		 * <p>
		 * <b>Default value:</b> false
		 * </p>
		 */
		public static final String NO_CP_STARTUP = "no_cp_startup";

		/**
		 * <p>
		 * Determines whether the Leap Motion service will automatically check for software
		 * updates.
		 * </p>
		 * <p>
		 * Since this setting is controlled by the user, you should ask for permission before
		 * changing its value.
		 * </p>
		 * <p>
		 * <b>Value type:</b> boolean
		 * </p>
		 */
		public static final String AUTO_CHECK_UPDATES = "auto_check_updates";

		/**
		 * <p>
		 * Determines whether the Leap Motion service will automatically install software
		 * updates. The <code>auto_check_updates</code> setting must be <code>true</code> for
		 * the service to discover updates in the first place.
		 * </p>
		 * <p>
		 * <b>Value type:</b> boolean
		 * </p>
		 */
		public static final String AUTO_INSTALL_UPDATES = "auto_install_updates";

		/**
		 * <p>
		 * Allow notification area messages to be displayed by the service/daemon icon. Set
		 * <code>true</code> (the default) to allow; <code>false</code> to suppress messages.
		 * </p>
		 * <p>
		 * This setting should only be used on computers you control.
		 * </p>
		 * <p>
		 * <b>Value type:</b> boolean
		 * </p>
		 * <p>
		 * <b>Default value:</b> true
		 * </p>
		 */
		public static final String SHOW_TRAY_MESSAGES = "show_tray_messages";

		/**
		 * <p>
		 * Allows metric collection.
		 * </p>
		 * <p>
		 * This setting should only be used on computers you control.
		 * </p>
		 * <p>
		 * <b>Value type:</b> boolean
		 * </p>
		 */
		public static final String METRICS_ENABLED = "metrics_enabled";

		private Service()
		{}
	}

	/**
	 * <p>
	 * Power saving options include the Control Panel Power Saving and Low Resource Mode
	 * settings. Power savings for computers running on battery power is on by default, but
	 * can be turned off using a configuration parameter.
	 * </p>
	 * <p>
	 * Setting a value of <code>false</code> for one of these options disables it; a value
	 * of <code>true</code> enables it.
	 * </p>
	 * <p>
	 * The effects of these options are not additive. If you enable both power saving and
	 * low resource modes, the resulting framerate will be the minimum value set by either
	 * option alone.
	 * </p>
	 * <p>
	 * The power saving configuration parameters are ignored when images are enabled. (Low
	 * resource mode is still effective.)
	 * </p>
	 */
	public static class Power
	{
		/**
		 * <p>
		 * Limits the frame rate to save power, even when the computer is plugged into AC
		 * power.
		 * </p>
		 * <p>
		 * <b>Value type:</b> boolean
		 * </p>
		 */
		public static final String POWER_SAVING_ADAPTER = "power_saving_adapter";

		/**
		 * <p>
		 * Limits the frame rate to save power when the computer is running on battery power.
		 * </p>
		 * <p>
		 * <b>Value type:</b> boolean
		 * </p>
		 */
		public static final String POWER_SAVING_BATTERY = "power_saving_battery";

		/**
		 * <p>
		 * Limits the frame rate to reduce USB bandwidth.
		 * </p>
		 * <p>
		 * <b>Value type:</b> boolean
		 * </p>
		 */
		public static final String LOW_RESOURCE_MODE_ENABLED = "low_resource_mode_enabled";

		private Power()
		{}
	}


	/**
	 * <p>
	 * The WebSocket options control how programs can connect to the WebSocket server
	 * provided by the Leap Motion service.
	 * </p>
	 * <p>
	 * <b>Important</b>: If you change these values, other programs on the client computer
	 * that expect the default WebSocket settings will fail to connect.
	 * </p>
	 * <p>
	 * Note that changes to the port numbers do not take effect until the Leap Motion
	 * service or daemon restarts.
	 * </p>
	 */
	public static class WebSocket
	{
		/**
		 * <p>
		 * Enables or disables all connections.
		 * </p>
		 * <p>
		 * <b>Value type:</b> boolean
		 * </p>
		 * <p>
		 * <b>Default value:</b> false
		 * </p>
		 */
		public static final String WEBSOCKETS_ENABLED = "";

		/**
		 * <p>
		 * Allows non-localhost clients to connect.
		 * </p>
		 * <p>
		 * <b>Value type:</b> boolean
		 * </p>
		 * <p>
		 * <b>Default value:</b> false
		 * </p>
		 */
		public static final String WEBSOCKETS_ALLOW_REMOTE = "";

		/**
		 * <p>
		 * Port used for connections from HTTP clients.
		 * </p>
		 * <p>
		 * <b>Value type:</b> integer
		 * </p>
		 * <p>
		 * <b>Default value:</b> 6437
		 * </p>
		 */
		public static final String WS_PORT = "";

		/**
		 * <p>
		 * Port used for connections from HTTPS clients.
		 * </p>
		 * <p>
		 * <b>Value type:</b> integer
		 * </p>
		 * <p>
		 * <b>Default value:</b> 6436
		 * </p>
		 */
		public static final String WSS_PORT = "";
	}

	private Configurations()
	{}
}
