package org.hibernate.entity;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class TestEntity {

    public static class SetConverter implements AttributeConverter<Set<String>, String> {

        @Override
        public String convertToDatabaseColumn(final Set<String> attribute) {
            if (attribute != null && !attribute.isEmpty()) {
                return String.join(",", attribute);
            }
            return null;
        }

        @Override
        public Set<String> convertToEntityAttribute(final String dbData) {
            if (dbData != null) {
                return Arrays.stream(dbData.split(",")).collect(Collectors.toSet());
            }
            return null;
        }
    }

    @Id
    @GeneratedValue
    public Long id;

    @Convert(converter = SetConverter.class)
    public Set<String> descriptions;
}
