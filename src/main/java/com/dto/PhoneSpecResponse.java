package com.dto;

public record PhoneSpecResponse(
        PhoneDetails phoneDetails,
        GsmNetworkDetails gsmNetworkDetails,
        GsmLaunchDetails gsmLaunchDetails,
        GsmBodyDetails gsmBodyDetails,
        GsmDisplayDetails gsmDisplayDetails,
        GsmPlatformDetails gsmPlatformDetails,
        GsmMemoryDetails gsmMemoryDetails,
        GsmMainCameraDetails gsmMainCameraDetails,
        GsmSelfieCameraDetails gsmSelfieCameraDetails,
        GsmSoundDetails gsmSoundDetails,
        GsmCommunicationsDetails gsmCommunicationsDetails,
        GsmBatteryDetails gsmBatteryDetails,
        GsmMiscDetails gsmMiscDetails
) {

    public record PhoneDetails(
            Long customId,
            String yearValue,
            String brandValue,
            String modelValue
    ) {}

    public record GsmNetworkDetails(
            String networkTechnology,
            String network2GBands,
            String network3GBands,
            String network4GBands,
            String networkSpeed
    ) {}

    public record GsmLaunchDetails(
            String launchAnnounced,
            String launchStatus
    ) {}

    public record GsmBodyDetails(
            String bodyDimensions,
            String bodyWeight,
            String bodySim,
            String bodyBuild
    ) {}

    public record GsmDisplayDetails(
            String displayType,
            String displaySize,
            String displayResolution
    ) {}

    public record GsmPlatformDetails(
            String platformChipset,
            String platformCpu,
            String platformGpu,
            String platformOs
    ) {}

    public record GsmMemoryDetails(
            String memoryCardSlot,
            String memoryInternal
    ) {}

    public record GsmMainCameraDetails(
            String mainCameraDual,
            String mainCameraFeatures,
            String mainCameraVideo
    ) {}

    public record GsmSelfieCameraDetails(
            String selfieCameraSingle,
            String selfieCameraVideo
    ) {}

    public record GsmSoundDetails(
            String sound35MmJack,
            String soundLoudspeaker
    ) {}

    public record GsmCommunicationsDetails(
            String communicationsBluetooth,
            String communicationsNfc,
            String communicationsPositioning,
            String communicationsRadio,
            String communicationsUsb,
            String communicationsWlan
    ) {}

    public record GsmBatteryDetails(
            String batteryCharging,
            String batteryType
    ) {}

    public record GsmMiscDetails(
            String miscColors,
            String miscModels,
            String miscPrice,
            String miscSar,
            String miscSarEu
    ) {}
}