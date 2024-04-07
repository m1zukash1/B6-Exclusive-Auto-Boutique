package com.b6exclusiveautoboutique.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Product {
    public static int lastId = 0;
    public enum Year {
        YEAR_2005("2005"),
        YEAR_2006("2006"),
        YEAR_2007("2007"),
        YEAR_2008("2008"),
        YEAR_2009("2009"),
        YEAR_2010("2010");

        private final String displayValue;

        Year(String displayValue) {
            this.displayValue = displayValue;
        }

        @Override
        public String toString() {
            return displayValue;
        }
    }
    public enum TransmissionType {
        MANUAL_5_SPEED("Manual 5-speed"),
        MANUAL_6_SPEED("Manual 6-speed"),
        AUTOMATIC("Automatic"),
        DSG("DSG"),
        TIPTRONIC("Tiptronic");

        private final String displayValue;

        TransmissionType(String displayValue) {
            this.displayValue = displayValue;
        }

        @Override
        public String toString() {
            return displayValue;
        }
    }

    public enum FuelType {
        PETROL("Petrol"),
        DIESEL("Diesel"),
        NATURAL_GAS("Natural Gas");

        private final String displayValue;

        FuelType(String displayValue) {
            this.displayValue = displayValue;
        }

        @Override
        public String toString() {
            return displayValue;
        }
    }
    public enum EngineType {

        TSI_1_4("1.4 TSI", FuelType.PETROL),
        _1_6("1.6", FuelType.PETROL),
        FSI_1_6("1.6 FSI", FuelType.PETROL),
        TSI_1_8("1.8 TSI", FuelType.PETROL),
        FSI_2_0("2.0 FSI", FuelType.PETROL),
        TURBO_FSI_2_0("2.0 Turbo FSI", FuelType.PETROL),
        TSI_2_0("2.0 TSI", FuelType.PETROL),
        V6_FSI_3_2("3.2 V6 FSI", FuelType.PETROL),
        R36("R36", FuelType.PETROL),
        TSI_EcoFuel_1_4("1.4 TSI EcoFuel", FuelType.NATURAL_GAS),
        TDI_1_6("1.6 TDI", FuelType.DIESEL),
        TDI_1_9("1.9 TDI", FuelType.DIESEL),
        TDI_2_0("2.0 TDI", FuelType.DIESEL),
        BlueTDI_2_0("2.0 BlueTDI", FuelType.DIESEL);
        private final String displayValue;
        public final FuelType fuelType;

        EngineType(String displayValue, FuelType fuelType) {
            this.displayValue = displayValue;
            this.fuelType = fuelType;
        }

        @Override
        public String toString() {
            return displayValue;
        }
    }
    public enum ExteriorColor {
        CANDY_WHITE("Candy White", "B4B4"),
        DEEP_BLACK("Deep Black Pearl", "LC9X"),
        REFLEX_SILVER("Reflex Silver Metallic", "A7W"),
        NIGHT_BLUE("Night Blue Metallic", "Z2Z2"),
        PLATINUM_GRAY("Platinum Gray Metallic", "LD7X"),
        ISLAND_GRAY("Island Gray Pearl", "C9C9"),
        MOCHA_BROWN("Mocha Brown Pearl", "LC8Z"),
        TUNGSTEN_SILVER("Tungsten Silver Metallic", "K5K5"),
        ARCTIC_BLUE_SILVER("Arctic Blue Silver", "S3S3"),
        WHEAT_BEIGE("Wheat Beige", "1W1W");

        private final String displayValue;
        private final String colorCode;

        ExteriorColor(String displayValue, String colorCode) {
            this.displayValue = displayValue;
            this.colorCode = colorCode;
        }

        @Override
        public String toString() {
            return displayValue + " (" + colorCode + ")";
        }
    }


    public enum InteriorType {
        LEATHER("Leather"),
        LEATHERETTE("Leatherette"),
        CLOTH("Cloth");

        private final String displayValue;

        InteriorType(String displayValue) {
            this.displayValue = displayValue;
        }

        @Override
        public String toString() {
            return displayValue;
        }
    }
    protected int id;
    protected Year year;
    protected float price;
    protected int mileageKm;
    protected TransmissionType transmissionType;
    protected FuelType fuelType;
    protected EngineType engineType;
    protected ExteriorColor exteriorColor;
    protected InteriorType interiorType;
    protected String description;

    public Product(int id, Year year, float price, int mileageKm, TransmissionType transmissionType, FuelType fuelType, EngineType engineType, ExteriorColor exteriorColor, InteriorType interiorType, String description) {
        this.id = id;
        this.year = year;
        this.price = price;
        this.mileageKm = mileageKm;
        this.transmissionType = transmissionType;
        this.fuelType = fuelType;
        this.engineType = engineType;
        this.exteriorColor = exteriorColor;
        this.interiorType = interiorType;
        this.description = description;
    }
    public Product(Year year, float price, int mileageKm, TransmissionType transmissionType, FuelType fuelType, EngineType engineType, ExteriorColor exteriorColor, InteriorType interiorType, String description) {
        this.id = UUID.randomUUID().toString().hashCode();;
        this.year = year;
        this.price = price;
        this.mileageKm = mileageKm;
        this.transmissionType = transmissionType;
        this.fuelType = fuelType;
        this.engineType = engineType;
        this.exteriorColor = exteriorColor;
        this.interiorType = interiorType;
        this.description = description;
    }

    @Override
    public String toString() {
        return year + " " + mileageKm + "Km " + transmissionType + " " +
                fuelType + " " + engineType + " " + exteriorColor + " " +
                interiorType + " " + price + "€";
    }
}
