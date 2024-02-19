package com.abx.hollywoodtherapist.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableEmbeddingRequestDto.class)
@JsonDeserialize(as = ImmutableEmbeddingRequestDto.class)
public interface EmbeddingRequestDto {
    @Value.Default
    default String getModel() {
        return "text-embedding-3-small";
    }

    List<String> getInput();
}
