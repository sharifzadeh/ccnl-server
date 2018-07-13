package edu.dartmouth.ccnl.ridmp.com;

import java.io.InputStream;

import java.util.PropertyResourceBundle;

public class ParentEXC extends Exception
{
  public String    errorMessage = null;
  public int       errorNumber = 0;
  public Throwable errorThrowable = null;
  public String    customerError = null;
  private static final String   technical = "technical";
  private static final String   customer  = "customer";
     
 // private static final String   resrcbun  = ParentEXC.class.getClassLoader().getResource("ir/mfa/com/Exception/excRB.properties");
  private InputStream inputStr;      
  private PropertyResourceBundle resrceBundle;
  
  public ParentEXC()
  {
    printStackTrace();    
  }
  public ParentEXC(int errNum)
  {    
    this.errorNumber    = errNum;    
  }
  public ParentEXC(String errMsg)
  {    
    super(errMsg);
    this.errorMessage   = errMsg;
  }
  public ParentEXC(String errMsg,int errNum)
  {
    this.errorMessage   = errMsg;
    this.errorNumber    = errNum;    
  }
  public ParentEXC(String errMsg,int errNum,Throwable errThrow)
  {
    this.errorMessage   = errMsg;
    this.errorNumber    = errNum;
    this.errorThrowable = errThrow;
  }
  private void getResourceBundle()
  {
   /*   try
  {
  FileInputStream file = new FileInputStream(resrcbun);
    inputStr = file;
    this.resrceBundle = new PropertyResourceBundle(inputStr);   
  }
  catch(FileNotFoundException filenot)
  {
    System.out.println("Resource Bundle Not Found : " + filenot.getMessage());
  }
  catch(IOException io)
  {
    System.out.println("Resource Bundle Not Found : " + io.getMessage());
  }
   */
  }
  public void printStackTrace()
  {
    super.printStackTrace();
    if( errorThrowable != null )
    {
      System.out.println("Caused by:");
      errorThrowable.printStackTrace();
    }
  }
  public void setErrorMessage(String errMsg)
  {
    this.errorMessage = errMsg;
  }
  public String getLocalMessage()
  {
    return this.errorMessage;
  }

  public String getErrorMessage()
  {
    getResourceBundle();
    if ( errorNumber != 0 )
      errorMessage = resrceBundle.getString(technical + errorNumber);
    else
      errorMessage = resrceBundle.getString("invalid.technical.message");
    return this.errorMessage;    
    
  }  
  public void setCustomerError(String errMsg)
  {
    this.customerError = errMsg;
  }

  public String getCustomerError()
  {
    getResourceBundle();
    if ( errorNumber != 0 )
      errorMessage = resrceBundle.getString(customer + errorNumber);
    else
      errorMessage = resrceBundle.getString("invalid.customer.message");        
    return this.errorMessage;
  }
  public void setErrorNumber(int errNum)
  {
    this.errorNumber = errNum;
  }

  public int getErrorNumber()
  {
    return this.errorNumber;
  }
  public void setErrorThrow(Throwable errThrow)
  {
    this.errorThrowable = errThrow;
  }

  public Throwable getErrorThrow()
  {
    return this.errorThrowable;
  }
}
