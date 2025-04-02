
package acme.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UniqueRegistrationNumberValidator.class)
public @interface ValidUniqueRegistrationNumber {

	String message() default "El registrationNumber debe ser único.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

}
