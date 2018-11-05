package de.niekrenz.intelhex.analyze.memmodel;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import java.io.IOException;

public class AddressDeserializer extends StdScalarDeserializer<Object> {

    public AddressDeserializer() {
        super(Number.class);
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        switch (p.getCurrentTokenId()) {
            case JsonTokenId.ID_NUMBER_INT:
                if (ctxt.hasSomeOfFeatures(F_MASK_INT_COERCIONS)) {
                    return _coerceIntegral(p, ctxt);
                }
                return p.getNumberValue();
            case JsonTokenId.ID_STRING:
                String text = p.getText().trim();
                if ((text.length() == 0)) {
                    return getNullValue(ctxt);
                }
                if (_hasTextualNull(text)) {
                    return getNullValue(ctxt);
                }
                _verifyStringForScalarCoercion(ctxt, text);
                try {
                    long value;
                    if (text.startsWith("0x")) {
                        value = Long.parseLong(text.substring(2), 16);
                    } else if (text.startsWith("b")) {
                        value = Long.parseLong(text.substring(1), 2);
                    } else {
                        value = Long.parseLong(text);
                    }
                    if (!ctxt.isEnabled(DeserializationFeature.USE_LONG_FOR_INTS)) {
                        if (value <= Integer.MAX_VALUE && value >= Integer.MIN_VALUE) {
                            return (int) value;
                        }
                    }
                    return value;
                } catch (IllegalArgumentException iae) {
                    return ctxt.handleWeirdStringValue(_valueClass, text, "not a valid number");
                }
            case JsonTokenId.ID_START_ARRAY:
                return _deserializeFromArray(p, ctxt);
        }
        return ctxt.handleUnexpectedToken(_valueClass, p);
    }
}
