package edu.dartmouth.ccnl.ridmp.com;

public interface IMethodInvoked 
{
  void release();
  String getMethodName();
  void setMethodName(final String methodName);
}
