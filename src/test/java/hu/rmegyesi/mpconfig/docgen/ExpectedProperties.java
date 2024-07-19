package hu.rmegyesi.mpconfig.docgen;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface ExpectedProperties {
    String[] value();
}
