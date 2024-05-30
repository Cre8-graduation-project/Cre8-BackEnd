package com.gaduationproject.cre8.member.type;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Sex {
    M,W;
    @JsonCreator
    public static Sex from(String val){
        for(Sex sex : Sex.values()){
            if(sex.name().equals(val)){
                return sex;
            }
        }
        return null;
    }
}
