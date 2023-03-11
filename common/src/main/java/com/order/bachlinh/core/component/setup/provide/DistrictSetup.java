package com.order.bachlinh.core.component.setup.provide;

import com.fasterxml.jackson.databind.JsonNode;
import com.order.bachlinh.core.component.client.template.spi.RestTemplate;
import com.order.bachlinh.core.component.setup.spi.AbstractSetup;
import com.order.bachlinh.core.entities.model.District;
import com.order.bachlinh.core.entities.model.Province;
import com.order.bachlinh.core.entities.repositories.DistrictRepository;
import com.order.bachlinh.core.entities.repositories.ProvinceRepository;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Log4j2
class DistrictSetup extends AbstractSetup {
    private static final String DISTRICT_API = "https://provinces.open-api.vn/api/d/";
    private EntityFactory entityFactory;
    private RestTemplate restTemplate;
    private ProvinceRepository provinceRepository;
    private DistrictRepository districtRepository;

    DistrictSetup(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    public void beforeExecute() {
        injectDependencies();
    }

    @Override
    public void execute() {
        if (districtRepository.countDistrict() != 0) {
            return;
        }
        JsonNode resp;
        try {
            resp = restTemplate.get(DISTRICT_API, (Object) null, null, Collections.emptyMap());
        } catch (IOException e) {
            log.warn("Failure when process district response");
            return;
        }
        List<Province> provinces = provinceRepository.getAllProvinces()
                .stream()
                .sorted(Comparator.comparingInt(Province::getCode))
                .toList();
        Collection<District> districts = new ArrayList<>();
        resp.forEach(district -> {
            int provinceCode = district.get("province_code").asInt();
            districts.add(parse(district, provinces.get(provinceCode - 1)));
        });
        boolean result = districtRepository.saveAllDistrict(districts);
        if (!result) {
            log.warn("Saving district to database failure");
        }
    }

    @Override
    public void afterExecute() {
        // Do nothing
    }

    private void injectDependencies() {
        if (entityFactory == null) {
            entityFactory = getApplicationContext().getBean(EntityFactory.class);
        }
        if (restTemplate == null) {
            restTemplate = getApplicationContext().getBean(RestTemplate.class);
        }
        if (provinceRepository == null) {
            provinceRepository = getApplicationContext().getBean(ProvinceRepository.class);
        }
        if (districtRepository == null) {
            districtRepository = getApplicationContext().getBean(DistrictRepository.class);
        }
    }

    private District parse(JsonNode jsonSource, Province province) {
        District district = entityFactory.getEntity(District.class);
        district.setName(jsonSource.get("name").asText());
        district.setCode(jsonSource.get("code").asInt());
        district.setDivisionType(jsonSource.get("division_type").asText());
        district.setCodeName(jsonSource.get("codename").asText());
        district.setProvince(province);
        return district;
    }
}
