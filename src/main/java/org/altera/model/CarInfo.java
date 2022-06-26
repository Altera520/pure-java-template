package org.altera.model;

public class CarInfo {
    private final String vin;
    private final String corp;
    private final String model;
    private final String productYear;
    private final String owner;
    private final String gender;

    public CarInfo(String vin, String corp, String model, String productYear, String owner, String gender) {
        this.vin = vin;
        this.corp = corp;
        this.model = model;
        this.productYear = productYear;
        this.owner = owner;
        this.gender = gender;
    }

    public String getVin() {
        return vin;
    }

    public String getCorp() {
        return corp;
    }

    public String getModel() {
        return model;
    }

    public String getProductYear() {
        return productYear;
    }

    public String getOwner() {
        return owner;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public boolean equals(Object obj) {
        CarInfo other = (CarInfo) obj;
        return this.getVin().equals(other.getVin()) &&
            this.getGender().equals(other.getGender()) &&
            this.getCorp().equals(other.getCorp()) &&
            this.getModel().equals(other.getModel()) &&
            this.getProductYear().equals(other.getProductYear()) &&
            this.getOwner().equals(other.getOwner());
    }
}
