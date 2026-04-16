package com.mwm.bioplanta.service.portaria;

import com.mwm.bioplanta.model.PortariaItem;
import com.mwm.bioplanta.repository.portaria.PortariaItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortariaItemService {

    private final PortariaItemRepository portariaItemRepository;

    public PortariaItemService(PortariaItemRepository portariaItemRepository) {
        this.portariaItemRepository = portariaItemRepository;
    }

    public List<PortariaItem> listarTodos() {
        return portariaItemRepository.findAll();
    }

    public PortariaItem criar(PortariaItem portariaItem) {
        return portariaItemRepository.save(java.util.Objects.requireNonNull(portariaItem));
    }
}