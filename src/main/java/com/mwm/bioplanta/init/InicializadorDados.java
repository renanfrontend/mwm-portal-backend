package com.mwm.bioplanta.init;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.mwm.bioplanta.model.BioPlanta;
import com.mwm.bioplanta.model.BioTransportadora;
import com.mwm.bioplanta.model.BioVeiculoTipo;
import com.mwm.bioplanta.model.BioVeiculoCombustivel;
import com.mwm.bioplanta.model.BioFiliada;
import com.mwm.bioplanta.repository.cadastro.BioFiliadaRepository;
import com.mwm.bioplanta.repository.cadastro.BioPlantaRepository;
import com.mwm.bioplanta.repository.cadastro.BioTransportadoraRepository;
import com.mwm.bioplanta.repository.cadastro.BioVeiculoCombustivelRepository;
import com.mwm.bioplanta.repository.cadastro.BioVeiculoTipoRepository;

@Component
@Profile("!test")
public class InicializadorDados implements CommandLineRunner {

    @Autowired
    private BioPlantaRepository bioPlantaRepository;

    @Autowired
    private BioTransportadoraRepository bioTransportadoraRepository;

    @Autowired
    private BioVeiculoTipoRepository bioVeiculoTipoRepository;

    @Autowired
    private BioVeiculoCombustivelRepository bioVeiculoCombustivelRepository;

    @Autowired
    private BioFiliadaRepository bioFiliadaRepository;

    @Override
    public void run(String... args) throws Exception {
        // Inicialização de dados básicos
        inicializarTiposVeiculo();
        inicializarCombustiveisVeiculo();
        inicializarPlantasEFiliadas();
    }

    private void inicializarPlantasEFiliadas() {
        // Verifica se existem plantas, senão cria a padrão
        BioPlanta planta = null;
        if (bioPlantaRepository.count() == 0) {
            planta = new BioPlanta();
            planta.setNome("Planta Toledo");
            planta.setCodigoInterno("001");
            planta.setCriadoEm(LocalDateTime.now());
            planta.setAtualizadoEm(LocalDateTime.now());
            planta = bioPlantaRepository.save(planta);
        } else {
            planta = bioPlantaRepository.findAll().get(0);
        }

        // Verifica se existem filiadas, senão cria as padrões
        if (bioFiliadaRepository.count() == 0 && planta != null) {
            List<BioFiliada> filiadas = Arrays.asList(
                createFiliada(planta, "Primato", "FIL001", "Toledo", "PR"),
                createFiliada(planta, "Agrocampo", "FIL002", "Cascavel", "PR")
            );
            bioFiliadaRepository.saveAll(filiadas);
        }
    }

    private BioFiliada createFiliada(BioPlanta planta, String nome, String codigo, String cidade, String estado) {
        BioFiliada filiada = new BioFiliada();
        filiada.setBioPlanta(planta);
        filiada.setNome(nome);
        filiada.setCodigoFiliada(codigo);
        filiada.setCidade(cidade);
        filiada.setEstado(estado);
        filiada.setCriadoEm(LocalDateTime.now());
        filiada.setAtualizadoEm(LocalDateTime.now());
        return filiada;
    }

    private void inicializarTiposVeiculo() {
        if (bioVeiculoTipoRepository.count() == 0) {
            List<BioVeiculoTipo> tipos = Arrays.asList(
                createVeiculoTipo(1L, "Truck", "truck"),
                createVeiculoTipo(2L, "Carreta", "carreta"),
                createVeiculoTipo(3L, "Bitrem", "bitrem"),
                createVeiculoTipo(4L, "VUC", "vuc"),
                createVeiculoTipo(5L, "Utilitário", "utilitario"),
                createVeiculoTipo(6L, "Empilhadeira", "empilhadeira")
            );
            bioVeiculoTipoRepository.saveAll(tipos);
        }
    }

    private void inicializarCombustiveisVeiculo() {
        if (bioVeiculoCombustivelRepository.count() == 0) {
            List<BioVeiculoCombustivel> combustiveis = Arrays.asList(
                createVeiculoCombustivel(1L, "Diesel", "diesel"),
                createVeiculoCombustivel(2L, "Biomethane", "biomethane")
            );
            bioVeiculoCombustivelRepository.saveAll(combustiveis);
        }
    }

    private BioVeiculoTipo createVeiculoTipo(Long id, String label, String value) {
        BioVeiculoTipo tipo = new BioVeiculoTipo();
        // tipo.setId(id); // ID gerado automaticamente
        tipo.setLabel(label);
        tipo.setValue(value);
        tipo.setCriadoEm(LocalDateTime.now());
        tipo.setAtualizadoEm(LocalDateTime.now());
        return tipo;
    }

    private BioVeiculoCombustivel createVeiculoCombustivel(Long id, String label, String value) {
        BioVeiculoCombustivel combustivel = new BioVeiculoCombustivel();
        // combustivel.setId(id); // ID gerado automaticamente
        combustivel.setLabel(label);
        combustivel.setValue(value);
        combustivel.setCriadoEm(LocalDateTime.now());
        combustivel.setAtualizadoEm(LocalDateTime.now());
        return combustivel;
    }
}
