package com.mwm.bioplanta.controller.portaria;

import com.mwm.bioplanta.dto.portaria.exclusao.PortariaErrorResponseDTO;
import com.mwm.bioplanta.service.portaria.exclusao.exception.PortariaExclusaoConflitoException;
import com.mwm.bioplanta.service.portaria.exclusao.exception.PortariaExclusaoNaoEncontradaException;
import com.mwm.bioplanta.service.portaria.exclusao.exception.PortariaExclusaoValidacaoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {
        PortariaAbastecimentoController.class,
        PortariaEntregaDejetosController.class
})
public class PortariaExclusaoExceptionHandler {

    @ExceptionHandler(PortariaExclusaoNaoEncontradaException.class)
    public ResponseEntity<PortariaErrorResponseDTO> handleNotFound(PortariaExclusaoNaoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new PortariaErrorResponseDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
    }

    @ExceptionHandler({PortariaExclusaoValidacaoException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<PortariaErrorResponseDTO> handleBadRequest(Exception ex) {
        String mensagem = ex instanceof HttpMessageNotReadableException
                ? "Payload inválido para a operação de exclusão."
                : ex.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new PortariaErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), mensagem));
    }

    @ExceptionHandler(PortariaExclusaoConflitoException.class)
    public ResponseEntity<PortariaErrorResponseDTO> handleConflict(PortariaExclusaoConflitoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new PortariaErrorResponseDTO(HttpStatus.CONFLICT.value(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<PortariaErrorResponseDTO> handleUnexpected(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new PortariaErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Erro interno ao processar a exclusão."));
    }
}