package com.wth.constant;

public enum WeightForFunctionEnum {
    SIMPLE_AI(5),
    SUPER_AI(64),
    BASE_MESSAGE(1),
    BT_SEARCH(87),
    COME_BACK_GROUP(14);

    int weight;

    WeightForFunctionEnum(int weight) {
        this.weight = weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
}
