package com.gaduationproject.cre8.common.response.error.checkEnumManOrWoman;

import com.gaduationproject.cre8.member.type.Sex;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.EnumMap;

public class CheckManOrWomanValidator implements ConstraintValidator<CheckManOrWoman, Sex> {

    @Override
    public boolean isValid(Sex sex, ConstraintValidatorContext context) {
        if(sex==null || sex!=Sex.M && sex!= Sex.W){
            return false;
        }

        return true;
    }
}
