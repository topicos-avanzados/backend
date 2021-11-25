package com.topicos.backend.controller;

import com.topicos.backend.dto.IndicatorValueDTO;
import com.topicos.backend.dto.request.CompositeIndicatorValueRequestDTO;
import com.topicos.backend.dto.request.IndicatorValueRequestDTO;
import com.topicos.backend.services.CompositeIndicatorValueService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@AllArgsConstructor
public class CompositeIndicatorValueController {

    private final CompositeIndicatorValueService compositeIndicatorValueService;

    //CREATE
    @PostMapping("/composite_indicator_value/create")
    public IndicatorValueDTO addIndicatorValue(@RequestBody CompositeIndicatorValueRequestDTO indicator) {
        return this.compositeIndicatorValueService.addIndicatorValue(indicator);
    }

}
