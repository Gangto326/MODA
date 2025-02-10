package com.moda.moda_api.card.infrastructure.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class VectorConverter implements AttributeConverter<float[], String> {
    @Override
    public String convertToDatabaseColumn(float[] attribute) {
        if (attribute == null) return null;

        StringBuilder vectorToString = new StringBuilder("[");
        for (int index=0, end=attribute.length; index < end; index++) {
            if (index > 0) vectorToString.append(",");
            vectorToString.append(attribute[index]);
        }
        return vectorToString.append("]").toString();
    }

    @Override
    public float[] convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        String cleaned = dbData.substring(1, dbData.length() - 1);
        String[] values = cleaned.split(",");

        float[] result = new float[values.length];
        for (int index=0, end=values.length; index < end; index++) {
            result[index] = Float.parseFloat(values[index].trim());
        }
        return result;
    }
}
