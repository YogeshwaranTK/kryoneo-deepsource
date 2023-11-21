package com.kjms.service.mapper;

import com.kjms.domain.EntityCountry;
import com.kjms.service.dto.Country;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Mapper class responsible for converting {@link EntityCountry} to {@link Country}
 */
@Service
public class CountryMapper {

    public Country entityCountryToCountry(EntityCountry entityCountry) {
        Country countryDTO = new Country();

        countryDTO.setId(entityCountry.getId());
        countryDTO.setName(entityCountry.getName());
        countryDTO.setIso(entityCountry.getIso());
        countryDTO.setIso3(entityCountry.getIso3());
        countryDTO.setNiceName(entityCountry.getNiceName());
        countryDTO.setNumberCode(entityCountry.getNumberCode());
        countryDTO.setPhoneCode(entityCountry.getPhoneCode());

        return countryDTO;
    }

    public Page<Country> entityCountriesToCountryList(Page<EntityCountry> countries) {
        return countries.map(this::entityCountryToCountry);
    }
}
