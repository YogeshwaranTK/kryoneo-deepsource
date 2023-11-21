package com.kjms.service.dto;

import com.kjms.domain.EntityCountry;
import java.io.Serializable;

/**
 * A DTO representing a {@link EntityCountry} country.
 */
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String iso;

    private String name;

    private String niceName;

    private String iso3;

    private String numberCode;

    private String phoneCode;

    public Country() {
        // Empty constructor needed for Jackson.
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNiceName() {
        return niceName;
    }

    public void setNiceName(String niceName) {
        this.niceName = niceName;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getNumberCode() {
        return numberCode;
    }

    public void setNumberCode(String numberCode) {
        this.numberCode = numberCode;
    }
}
