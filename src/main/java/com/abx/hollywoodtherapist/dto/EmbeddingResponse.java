package com.abx.hollywoodtherapist.dto;

import org.immutables.value.Value;

@Value.Immutable
public interface EmbeddingResponse {
    double[][] getEmbeddings();
}
