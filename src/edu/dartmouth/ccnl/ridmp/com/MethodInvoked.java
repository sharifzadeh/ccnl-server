package edu.dartmouth.ccnl.ridmp.com;

public class MethodInvoked implements IMethodInvoked
{
  private static MethodInvoked _instance;
  
  static{
    _instance = new MethodInvoked();
  }
  private MethodInvoked()
  { release(); }
  
  
  static final String RESET = "";
  public void release()
  {
    methodName = RESET;
  }
  
  /**
   * 
   * @return MethodInvoked
   */
  static public MethodInvoked getInstance() {  return _instance;  }
  
  public void setMethodName(final String methodName)
  {
    this.methodName = methodName;
  }
  public String getMethodName()
  {
    String methodNameInvoked = methodName;
    release();
    return methodNameInvoked;
  }
  private String methodName;
}