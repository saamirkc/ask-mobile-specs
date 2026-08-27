package com.dto;
import org.springframework.stereotype.Service;

@Service
public class PhoneEmbeddingTextBuilder {

    public String build(PhoneSpecResponse spec) {
        StringBuilder sb = new StringBuilder();

        var details = spec.phoneDetails();
        if (details != null) {
            sb.append(details.brandValue()).append(" ").append(details.modelValue());
            if (details.yearValue() != null) {
                sb.append(" (").append(details.yearValue()).append(")");
            }
            sb.append(". ");
        }

        if (spec.gsmDisplayDetails() != null) {
            var d = spec.gsmDisplayDetails();
            sb.append("Display: ");
            appendIfPresent(sb, d.displayType());
            appendIfPresent(sb, d.displaySize());
            appendIfPresent(sb, d.displayResolution());
            sb.append(". ");
        }

        if (spec.gsmPlatformDetails() != null) {
            var p = spec.gsmPlatformDetails();
            sb.append("Platform: ");
            appendIfPresent(sb, p.platformChipset());
            appendIfPresent(sb, p.platformCpu());
            appendIfPresent(sb, p.platformGpu());
            appendIfPresent(sb, p.platformOs());
            sb.append(". ");
        }

        if (spec.gsmMemoryDetails() != null) {
            var m = spec.gsmMemoryDetails();
            sb.append("Memory: ");
            appendIfPresent(sb, m.memoryInternal());
            appendIfPresent(sb, m.memoryCardSlot());
            sb.append(". ");
        }

        if (spec.gsmMainCameraDetails() != null) {
            var c = spec.gsmMainCameraDetails();
            sb.append("Main camera: ");
            appendIfPresent(sb, c.mainCameraDual());
            appendIfPresent(sb, c.mainCameraFeatures());
            appendIfPresent(sb, c.mainCameraVideo());
            sb.append(". ");
        }

        if (spec.gsmSelfieCameraDetails() != null) {
            var c = spec.gsmSelfieCameraDetails();
            sb.append("Selfie camera: ");
            appendIfPresent(sb, c.selfieCameraSingle());
            appendIfPresent(sb, c.selfieCameraVideo());
            sb.append(". ");
        }

        if (spec.gsmBatteryDetails() != null) {
            var b = spec.gsmBatteryDetails();
            sb.append("Battery: ");
            appendIfPresent(sb, b.batteryType());
            appendIfPresent(sb, b.batteryCharging());
            sb.append(". ");
        }

        if (spec.gsmBodyDetails() != null) {
            var b = spec.gsmBodyDetails();
            sb.append("Body: ");
            appendIfPresent(sb, b.bodyDimensions());
            appendIfPresent(sb, b.bodyWeight());
            appendIfPresent(sb, b.bodyBuild());
            appendIfPresent(sb, b.bodySim());
            sb.append(". ");
        }

        if (spec.gsmNetworkDetails() != null) {
            var n = spec.gsmNetworkDetails();
            sb.append("Network: ");
            appendIfPresent(sb, n.networkTechnology());
            appendIfPresent(sb, n.networkSpeed());
            sb.append(". ");
        }

        if (spec.gsmCommunicationsDetails() != null) {
            var c = spec.gsmCommunicationsDetails();
            sb.append("Connectivity: ");
            appendIfPresent(sb, c.communicationsWlan());
            appendIfPresent(sb, c.communicationsBluetooth());
            appendIfPresent(sb, c.communicationsUsb());
            appendIfPresent(sb, c.communicationsNfc());
            sb.append(". ");
        }

        if (spec.gsmSoundDetails() != null) {
            var s = spec.gsmSoundDetails();
            sb.append("Sound: ");
            appendIfPresent(sb, s.soundLoudspeaker());
            appendIfPresent(sb, s.sound35MmJack());
            sb.append(". ");
        }

        if (spec.gsmLaunchDetails() != null) {
            var l = spec.gsmLaunchDetails();
            sb.append("Launch: ");
            appendIfPresent(sb, l.launchAnnounced());
            appendIfPresent(sb, l.launchStatus());
            sb.append(". ");
        }

        if (spec.gsmMiscDetails() != null) {
            var m = spec.gsmMiscDetails();
            sb.append("Colors and pricing: ");
            appendIfPresent(sb, m.miscColors());
            appendIfPresent(sb, m.miscPrice());
            sb.append(". ");
        }

        return sb.toString().trim();
    }

    private void appendIfPresent(StringBuilder sb, String value) {
        if (value != null && !value.isBlank()) {
            sb.append(value).append(", ");
        }
    }
}