package ua.artcode.taxi.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class LoggerAspect {

    private static final Logger LOG = Logger.getLogger(LoggerAspect.class);

    @Around("execution(* ua.artcode.taxi..*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        Object result = null;
        try{
            String methodName = point.getSignature().toShortString();
            long start = System.currentTimeMillis();

            LOG.info(String.format("start method >>> %s",
                    methodName));

            result = point.proceed();

            LOG.info(String.format("<<< end method %s in %d ms: %s",
                    methodName,
                    System.currentTimeMillis() - start,
                    result));

        } catch (Throwable throwable) {
            LOG.error(throwable.toString());
            throwable.printStackTrace();
            throw throwable;
        }
        return result;
    }
}