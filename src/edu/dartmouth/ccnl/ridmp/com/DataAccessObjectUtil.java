package edu.dartmouth.ccnl.ridmp.com;
public final class DataAccessObjectUtil 
{
  
  public interface DataAccessObjectEnum
  {
    public static final int STATUS = 2;
    public static final String STATUS_NAME = "status";
    public static final String STATUS_COL_NAME = "_STATUS";
    public static final String PK_NAME = "pkValue";
  }
  
  public DataAccessObjectUtil( String methodName )
  {
    this.methodName = methodName;
  }


  public void setMethodName(String methodName)
  {
    this.methodName = methodName;
  }
  public String getMethodName()
  {
    return methodName;
  }
  public void setIsEditMethod(boolean isEditMethod)
  {
    this.isEditMethod = isEditMethod;
  }
  /**
   * 
   * @return boolean
   */
  public boolean getIsEditMethod()
  {
    IBundleUtility bundleUtility = new BundleUtility();    
    try{
      setIsEditMethod( bundleUtility.isEditMethod( getMethodName() ) );
    }catch( Exception ex )
    {
      ex.printStackTrace();
    }    
    return isEditMethod;
  }
  private String methodName;
  private boolean isEditMethod;

}