package ua.artcode.taxi.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
//@Component
public class LogginAspect {

    @Before(value = "publicMethodsPointCut()")
    public void logginPublicMethodsAdvice() {
        System.out.println(" -----------------public method was called");
    }

    @Pointcut(value = "execution(public * ua.artcode.taxi..*(..))")
    public void publicMethodsPointCut() {}

    @Around(value = "publicMethodsPointCut()")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        String methodName = proceedingJoinPoint.getSignature().getName();
        System.out.printf("before method %s", methodName);

        Object value = null;

        try{
            return proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        System.out.printf("after method %s", methodName);

        return value;
    }

}