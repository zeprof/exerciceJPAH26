package al420445.airport;


import jakarta.persistence.*;

@Embeddable
public class Address {
    private String street;
    private String city;
    private String province;
    private String codePostal;

    @Transient
    private String menFouBinRaide;

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

}
