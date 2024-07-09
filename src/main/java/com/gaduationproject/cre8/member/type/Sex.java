package com.gaduationproject.cre8.member.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum Sex {
    M("남"),W("여");

    private String name;
    Sex(String name){
        this.name = name;
    }
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
