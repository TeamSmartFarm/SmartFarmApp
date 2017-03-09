package smartfarm.team.smartfarmapp.Crop;

import java.io.Serializable;

/**
 * Created by AK on 02-Jan-17.
 */
public class Crop  implements Serializable {
    public String cropName;
    public String cropImageUrl;
    public String waterConsumption;
    public String moisture;
    public String temperature;

    public Crop(String cropName, String cropImageUrl, String waterConsumption, String moisture, String temperature) {
        this.cropName = cropName;
        this.cropImageUrl = cropImageUrl;
        this.waterConsumption = waterConsumption;
        this.moisture = moisture;
        this.temperature = temperature;
    }

    public Crop(String cropName, String cropImageUrl) {
        this.cropName = cropName;
        this.cropImageUrl = cropImageUrl;

    }

    public String getWaterConsumption() {
        return waterConsumption;
    }

    public void setWaterConsumption(String waterConsumption) {
        this.waterConsumption = waterConsumption;
    }

    public String getMoisture() {
        return moisture;
    }

    public void setMoisture(String moisture) {
        this.moisture = moisture;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getCropImageUrl() {
        return cropImageUrl;
    }

    public void setCropImageUrl(String cropImageUrl) {
        this.cropImageUrl = cropImageUrl;
    }
}
