package com.arquiteturahexagonal.dominio.dtos;

import java.util.List;
import java.util.logging.Logger;

public class TransactionValidator {

    private static final Logger log = Logger.getLogger(TransactionValidator.class.getName());

    private static final String BIT_02 = "02";
    private static final String START_PROCCESS = "Iniciando validateInformations com Bit02: ";
    private static final String VALUES_NOT_FILLED = "Valores não preenchidos";
    private static final String VALIDATION_FAIL = "Validacao falhou";
    private static final String VALIDATED_TRANSACTION = "Transação validada para Bit02: ";
    private static final List<String> listValues = List.of("02", "03", "04", "05", "12");

    public void validateInformations(ISOModel m) {
        log.info(START_PROCCESS, m.getBit02());

        try {
            validate(m);
        } catch (Exception e) {
            log.severe("Error: " + e.getMessage());
        }
    }

    private void validate(ISOModel m) {
        Boolean isNotFilled = checkNotFilled(m);
        Boolean isValidateAux = checkValidateAux(m);
        Boolean isAuxValidation = checkAuxValidation(isValidateAux, m);
        String strValueFilled = getValueFilled(isNotFilled);

        if (isValid(isNotFilled, isValidateAux, isAuxValidation, strValueFilled)
                && m.getBit03() != null
                && m.getBit04() != null
                && m.getBit05() != null
                && m.getBit12() != null
                && listValues.contains("10")) {
            save(m, isAuxValidation);
        } else {
            log.severe(VALUES_NOT_FILLED);
            throw new IllegalArgumentException(VALUES_NOT_FILLED);
        }
    }

    private boolean isValid(boolean validaPreenchido, boolean validaVazio, boolean validaAux, String str) {
        return validaPreenchido || validaVazio && !validaAux && str.equals("01");
    }

    private Boolean checkNotFilled(ISOModel m) {
        return m.getBit02() == null;
    }

    private Boolean checkValidateAux(ISOModel m) {
        return m.getBit02() != null && m.getBit02().getValue().isEmpty();
    }

    private Boolean checkAuxValidation(boolean isValidateAux, ISOModel m) {
        return isValidateAux && m.getBit03() == null;
    }

    private String getValueFilled(Boolean isNotFilled) {
        return isNotFilled ? "01" : "02";
    }

    private void save(ISOModel m, boolean auxValidacao) {
        if (auxValidacao) {
            log.severe(VALIDATION_FAIL + m.getBit02());
            throw new IllegalArgumentException(VALIDATION_FAIL);
        }
        log.info(VALIDATED_TRANSACTION + m.getBit02().getValue());
    }
}