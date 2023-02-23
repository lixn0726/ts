package indl.lixn.ts.common.annotation;

import java.lang.annotation.*;

/**
 * @author lixn
 * @description
 * @date 2023/02/15 14:33
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TsJob {

    String id() default "";

    String timeExpression() default "";

    String expressionFormat() default "";

}
