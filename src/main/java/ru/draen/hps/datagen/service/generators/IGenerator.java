package ru.draen.hps.datagen.service.generators;

public interface IGenerator<REQ, RES> {
    RES generate(REQ request);
}
