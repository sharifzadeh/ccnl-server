package edu.dartmouth.ccnl.ridmp.com;

import java.io.FileNotFoundException;

import java.io.IOException;

import java.io.InputStream;

import java.util.PropertyResourceBundle;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

final class BundleUtility implements IBundleUtility
{
  
  static final String DELIM    = ","; 
  static final String METHOD_NAMES = "preEdit,preHeaderEdit,preDetailEdit";

  private InputStream inputStr;      
  private PropertyResourceBundle resourceBundle;

  private static final Logger logger = Logger.getLogger( BundleUtility.class );
  
  public BundleUtility()
  {}
  
  /**
   * 
   * @param methodName
   * @return boolean
   * @throws Exception
   */
  public final boolean isEditMethod( final String methodName ) throws Exception
  {
    boolean result = false;    
    StringTokenizer stringTokenizer = null;
    
    try{
        stringTokenizer = getKeyValue();
        while( stringTokenizer.hasMoreTokens() )
          if( stringTokenizer.nextToken().equals( methodName ) )
            return true;        
    }catch( FileNotFoundException ex )
    {
      logger.error( ex , ex.getCause() );
      throw ex;
    }catch( IOException ex )
    {
      logger.error( ex , ex.getCause() );
      throw ex;
    }
    return result;
  }
  /**
   * 
   * @return StringTokenizer
   * @throws java.io.FileNotFoundException
   * @throws java.io.IOException
   */
  public final StringTokenizer getKeyValue() throws FileNotFoundException, IOException
  {
    StringTokenizer stringTokenizer = null;
    logger.info( "Key had been read which is :".concat( METHOD_NAMES ) );    
    stringTokenizer = new StringTokenizer( METHOD_NAMES , DELIM );
    return stringTokenizer;
  }
  
  public static void main( String[] args )
  {
    IBundleUtility bundleUtility = new BundleUtility();
    
    try{
      System.out.println( "wrongMethodName : " +  bundleUtility.isEditMethod( "wrongMethodName" ) );
      System.out.println( "correctMethodName : " +  bundleUtility.isEditMethod( "preEdit" ) );
      
    }catch( Exception ex )
    {
      ex.printStackTrace();
    }
  }
}