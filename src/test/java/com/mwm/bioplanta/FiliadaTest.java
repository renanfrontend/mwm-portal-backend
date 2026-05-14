package com.mwm.bioplanta;

import com.mwm.bioplanta.controller.FiliadaController;
import com.mwm.bioplanta.dto.FiliadaResponseDTO;
import com.mwm.bioplanta.service.FiliadaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FiliadaTest {

    @Test
    public void testListarFiliadasAtivas() {
        // Criar mock do service
        FiliadaService filiadaService = Mockito.mock(FiliadaService.class);
        
        // Criar dados de teste
        FiliadaResponseDTO filiada1 = new FiliadaResponseDTO();
        filiada1.setId(1L);
        filiada1.setCodigoFiliada("FIL001");
        filiada1.setNome("PRIMATO");
        filiada1.setEstado("SP");
        filiada1.setCidade("São Paulo");

        FiliadaResponseDTO filiada2 = new FiliadaResponseDTO();
        filiada2.setId(2L);
        filiada2.setCodigoFiliada("FIL002");
        filiada2.setNome("UNIDADE TESTE");
        filiada2.setEstado("RJ");
        filiada2.setCidade("Rio de Janeiro");

        List<FiliadaResponseDTO> filiadas = Arrays.asList(filiada1, filiada2);

        // Mock do service
        Mockito.when(filiadaService.findAllAtivas()).thenReturn(filiadas);

        // Criar controller e testar
        FiliadaController controller = new FiliadaController(filiadaService);
        ResponseEntity<List<FiliadaResponseDTO>> response = controller.listarFiliadasAtivas();

        // Validações
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().size() == 2;
        assert response.getBody().get(0).getId().equals(1L);
        assert response.getBody().get(0).getCodigoFiliada().equals("FIL001");
        assert response.getBody().get(0).getNome().equals("PRIMATO");
        assert response.getBody().get(0).getEstado().equals("SP");
        assert response.getBody().get(0).getCidade().equals("São Paulo");

        // Verificar que o service foi chamado
        Mockito.verify(filiadaService, Mockito.times(1)).findAllAtivas();
    }
}