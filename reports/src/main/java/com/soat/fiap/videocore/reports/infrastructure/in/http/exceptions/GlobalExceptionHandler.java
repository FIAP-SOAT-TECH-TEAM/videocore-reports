package com.soat.fiap.videocore.reports.infrastructure.in.http.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.NotAuthorizedException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.ReportException;
import com.soat.fiap.videocore.reports.core.domain.exceptions.VideoImageDownloadUrlNotFound;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler global de exceções
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Trata exceções genéricas
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
		var errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				ex.getMessage(), request.getServletPath());

		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

    /**
     * Trata exceções genéricas
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestParam(MissingServletRequestParameterException ex, HttpServletRequest request) {
        var message = String.format("O parâmetro: %s é obrigatório", ex.getParameterName());

        var errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                message, request.getServletPath());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

	/**
	 * Trata erros de validação
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult()
				.getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

		ValidationErrorResponse errorResponse = new ValidationErrorResponse(LocalDateTime.now(),
				HttpStatus.BAD_REQUEST.value(), "Erro de validação", request.getServletPath(), errors);

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Trata erros de regra de negócio
	 */
	@ExceptionHandler(ReportException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(ReportException ex, HttpServletRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
				ex.getMessage(), request.getServletPath());

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

    /**
     * Trata erros de url de download do arquivo de imagens de um vídeo não encontrada
     */
    @ExceptionHandler(VideoImageDownloadUrlNotFound.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(VideoImageDownloadUrlNotFound ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(),
                ex.getMessage(), request.getServletPath());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Trata erros de falha na autenticação
     */
    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(NotAuthorizedException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(), request.getServletPath());

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

	/**
	 * Trata erros de integridade de dados (ex: violação de chave estrangeira)
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
			HttpServletRequest request) {

		var message = "Operação não permitida: viola regras de integridade de dados";

		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.CONFLICT.value(), message,
				request.getServletPath());

		return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	}

	/**
	 * Trata erros de formato inválido
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleInvalidFormatException(HttpMessageNotReadableException ex,
			HttpServletRequest request) {

		var message = "Erro de formato inválido na requisição";

		Throwable cause = ex.getCause();
		if (cause instanceof InvalidFormatException invalidFormatEx) {
			Class<?> targetType = invalidFormatEx.getTargetType();

			if (targetType.isEnum()) {
				String[] valoresAceitos = getEnumNames(targetType);
                message = String.format("Valor inválido: %s. Valores aceitos para o campo: %s",
						invalidFormatEx.getValue(), String.join(", ", valoresAceitos));
			} else {
                message = "Formato inválido para o campo: " + invalidFormatEx.getValue();
			}
		}

		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), message,
				request.getServletPath());

		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

    /**
     * Retorna os nomes de todas as constantes de um enum.
     *
     * @param enumType Classe do enum
     * @return Array com os nomes das constantes
     */
	private String[] getEnumNames(Class<?> enumType) {
		Object[] enumConstants = enumType.getEnumConstants();
		String[] names = new String[enumConstants.length];
		for (int i = 0; i < enumConstants.length; i++) {
			names[i] = enumConstants[i].toString();
		}
		return names;
	}

}
