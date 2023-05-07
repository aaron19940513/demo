package com.demo;

import lombok.Getter;

@Getter
public enum DBEnum {
    DB1("coffee", "cooffee1"),
    DB2("coffee", "cooffee2");
    private String originTableName;
    private String newTableName;

    DBEnum(String s1, String s2) {
        this.originTableName = s1;
        this.newTableName = s2;
    }

}
