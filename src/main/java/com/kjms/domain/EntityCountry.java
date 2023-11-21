package com.kjms.domain;

import javax.persistence.*;

/**
 * Entity class representing a countries in the journal management system.
 */
@Entity
@Table(name = "jm_country")
public class EntityCountry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "iso", length = 2, nullable = false)
    private String iso;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "nice_name", length = 200, nullable = false)
    private String niceName;

    @Column(name = "iso3", length = 3)
    private String iso3;

    @Column(name = "number_code", length = 11)
    private String numberCode;

    @Column(name = "phone_code", length = 11, nullable = false)
    private String phoneCode;

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

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }
}
