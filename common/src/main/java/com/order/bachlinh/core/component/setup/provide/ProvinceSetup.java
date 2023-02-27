package com.order.bachlinh.core.component.setup.provide;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.order.bachlinh.core.component.client.template.spi.RestTemplate;
import com.order.bachlinh.core.component.setup.spi.AbstractSetup;
import com.order.bachlinh.core.entities.model.Province;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.entities.repositories.ProvinceRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Log4j2
class ProvinceSetup extends AbstractSetup {
    private static final String PROVINCE_API = "https://provinces.open-api.vn/api/p/";
    private EntityFactory entityFactory;
    private RestTemplate restTemplate;
    private ProvinceRepository provinceRepository;

    ProvinceSetup(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    public void beforeExecute() {
        injectDependencies();
    }

    @Override
    public void execute() {
        if (provinceRepository.countProvince() != 0) {
            return;
        }
        JsonNode resp;
        try {
            resp = restTemplate.get(PROVINCE_API, null, null, Collections.emptyMap());
        } catch (JsonProcessingException e) {
            log.warn("Call province api failure");
            return;
        }
        Collection<Province> provinces = new ArrayList<>();
        resp.forEach(province -> provinces.add(parse(province)));
        boolean result = provinceRepository.saveAllProvinces(provinces);
        if (!result) {
            log.warn("Save provinces into database failure");
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
    }

    private Province parse(JsonNode jsonSource) {
        Province province = entityFactory.getEntity(Province.class);
        province.setName(jsonSource.get("name").asText());
        province.setCode(jsonSource.get("code").asInt());
        province.setDivisionType(jsonSource.get("division_type").asText());
        province.setCodeName(jsonSource.get("codename").asText());
        province.setPhoneCode(jsonSource.get("phone_code").asInt());
        return province;
    }
}
