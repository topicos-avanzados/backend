package com.topicos.backend.services;

import com.topicos.backend.dto.IndicatorValueDTO;
import com.topicos.backend.dto.request.CompositeIndicatorTypeDTO;
import com.topicos.backend.dto.request.CompositeIndicatorValueRequestDTO;
import com.topicos.backend.dto.request.IndicatorValueRequestDTO;
import com.topicos.backend.persistence.model.Company;
import com.topicos.backend.persistence.model.CompositeIndicatorValue;
import com.topicos.backend.persistence.model.Indicator;
import com.topicos.backend.persistence.model.IndicatorValue;
import com.topicos.backend.persistence.repository.CompanyRepository;
import com.topicos.backend.persistence.repository.CompositeIndicatorValueRespository;
import com.topicos.backend.persistence.repository.IndicatorRepository;
import com.topicos.backend.utils.Mappers;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CompositeIndicatorValueService {

    private final IndicatorRepository indicatorRepository;

    private final CompositeIndicatorValueRespository compositeIndicatorRepository;

    private final CompanyRepository companyRepository;


    public IndicatorValueDTO addIndicatorValue(CompositeIndicatorValueRequestDTO indicator) {
//        Optional<Indicator> ind = this.indicatorRepository.findById(indicator.getIndicatorId());
//        Optional<Company> company = this.companyRepository.findById(indicator.getCompanyId());
        if (ind.isPresent() && company.isPresent()) {

//            CompositeIndicatorValue indicatorValue = this.compositeIndicatorRepository.save();
//            indicator.setId(indicatorValue.getId());
//            return (Mappers.buildCompositeIndicatorValue(indicatorValue));
        }
        return null;
    }

    private Float calculateValue(List<CompositeIndicatorTypeDTO> list) {
        Float value = 00.0f;
        Optional<String> indicator = Optional.ofNullable(list.stream().findFirst().get().getFirst_value());
        if (indicator.isPresent()) {
            Optional<Indicator> ind = this.indicatorRepository.findById(Long.parseLong(indicator.get()));
            if (ind.isPresent()) {
//            value = ind.get().
                for (CompositeIndicatorTypeDTO composite : list) {
                    switch (composite.getOperator()) {
                        case "+":
                            value += Float.parseFloat((composite.getFirst_value()) + Float.parseFloat(composite.getSecond_value()));
                        case "-":
                            value += Float.parseFloat((composite.getFirst_value())) - Float.parseFloat(composite.getSecond_value());
                        case "*":
                            value += Float.parseFloat((composite.getFirst_value())) * Float.parseFloat(composite.getSecond_value());
                        case "/":
                            value += Float.parseFloat((composite.getFirst_value())) / Float.parseFloat(composite.getSecond_value());
                        default:
                    }
                }
            }
        }
        return value;
    }
}
