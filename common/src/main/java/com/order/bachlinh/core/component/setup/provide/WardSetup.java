package com.order.bachlinh.core.component.setup.provide;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.order.bachlinh.core.component.client.template.spi.RestTemplate;
import com.order.bachlinh.core.component.setup.spi.AbstractSetup;
import com.order.bachlinh.core.entities.model.District;
import com.order.bachlinh.core.entities.model.Ward;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.repositories.DistrictRepository;
import com.order.bachlinh.core.repositories.WardRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Log4j2
class WardSetup extends AbstractSetup {
    private static final String WARD_API = "https://provinces.open-api.vn/api/w/";
    private EntityFactory entityFactory;
    private RestTemplate restTemplate;
    private DistrictRepository districtRepository;
    private WardRepository wardRepository;

    WardSetup(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    public void beforeExecute() {
        injectDependencies();
    }

    @Override
    public void execute() {
        if (wardRepository.countAllWards() != 0) {
            return;
        }
        JsonNode resp;
        try {
            resp = restTemplate.get(WARD_API, null, null, Collections.emptyMap());
        } catch (JsonProcessingException e) {
            log.warn("Failure when process district response");
            return;
        }
        List<District> districts = districtRepository.getAllDistrict()
                .stream()
                .sorted(Comparator.comparingInt(District::getCode))
                .toList();
        Collection<Ward> wards = new ArrayList<>();
        resp.forEach(ward -> {
            int provinceCode = ward.get("province_code").asInt();
            wards.add(parse(ward, districts.get(provinceCode - 1)));
        });
        boolean result = wardRepository.saveAllWard(wards);
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
        if (districtRepository == null) {
            districtRepository = getApplicationContext().getBean(DistrictRepository.class);
        }
        if (wardRepository == null) {
            wardRepository = getApplicationContext().getBean(WardRepository.class);
        }
    }

    private Ward parse(JsonNode jsonSource, District district) {
        Ward ward = entityFactory.getEntity(Ward.class);
        ward.setName(jsonSource.get("name").asText());
        ward.setCode(jsonSource.get("code").asInt());
        ward.setDivisionType(jsonSource.get("division_type").asText());
        ward.setCodeName(jsonSource.get("codename").asText());
        ward.setDistrict(district);
        return ward;
    }
}
