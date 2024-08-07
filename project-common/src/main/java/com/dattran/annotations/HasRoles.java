package com.dattran.annotations;

import java.lang.annotation.*;

//@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
//@Constraint(validatedBy = RolesValidator.class)
public @interface HasRoles {
    String[] roles() default {};
//    String message() default "Forbidden";
//    Class<?>[] groups() default {};
//    Class<? extends Payload>[] payload() default {};
}
