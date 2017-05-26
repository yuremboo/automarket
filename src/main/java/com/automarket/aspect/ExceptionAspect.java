package com.automarket.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class ExceptionAspect {

	//@Around("execution(* com.automarket.service..*(..))")
	public Object handleServiceException(ProceedingJoinPoint point) throws Throwable {
		try {
			return point.proceed();
		} catch (Throwable throwable) {
			throw throwable;
		}
	}

}
