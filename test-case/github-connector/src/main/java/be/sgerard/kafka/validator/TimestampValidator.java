package be.sgerard.kafka.validator;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;

import java.time.format.DateTimeParseException;

import static be.sgerard.kafka.utils.TimeUtils.toInstant;

public class TimestampValidator implements ConfigDef.Validator {

    @Override
    public void ensureValid(String name, Object value) {
        try {
            toInstant((String) value);
        } catch (DateTimeParseException e) {
            throw new ConfigException(name, value, "Wasn't able to parse the timestamp [" + e.getMessage() + "], make sure it is formatted according to ISO-8601 standards.");
        }
    }
}
