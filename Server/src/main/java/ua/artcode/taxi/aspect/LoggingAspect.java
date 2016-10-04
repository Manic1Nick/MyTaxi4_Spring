package ua.artcode.taxi.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
//@Component
public class LoggingAspect {

    @Before(value = "publicMethodsPointCut()")
    public void logginPublicMethodsAdvice() {

        System.out.println("public method was called");
    }

    @Pointcut(value = "execution(public * ua.artcode.taxi..*(..))")
    public void publicMethodsPointCut() {}

    @Around(value = "publicMethodsPointCut()")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodName = proceedingJoinPoint.getSignature().getName();
        System.out.println(String.format("before method %s", methodName));

        try{
            Object proceed = proceedingJoinPoint.proceed();
            System.out.println(String.format("after method %s", methodName));
            return proceed;

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        }
    }
}