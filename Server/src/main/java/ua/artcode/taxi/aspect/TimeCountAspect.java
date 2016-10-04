package ua.artcode.taxi.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
//@Component
public class TimeCountAspect {

    @Before(value = "publicMethodsPointCut()")
    public void logginPublicMethodsAdvice() {

        System.out.println("public method was called");
    }

    @Pointcut(value = "execution(public * ua.artcode.taxi..*(..))")
    public void publicMethodsPointCut() {}

    @Around(value = "publicMethodsPointCut()")
    public Object countTimeMethod(ProceedingJoinPoint joinPoint) throws Throwable {

        Object output = null;
        try {
            long start = System.currentTimeMillis();
            System.out.println(String.format("method begin: %s",
                    joinPoint.getSignature().toShortString()));

            output = joinPoint.proceed();

            long time = System.currentTimeMillis() - start;
            System.out.println(String.format("method end: %s, time= %d ms",
                    joinPoint.getSignature().toShortString(), time));

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        }

        return output;
    }
}