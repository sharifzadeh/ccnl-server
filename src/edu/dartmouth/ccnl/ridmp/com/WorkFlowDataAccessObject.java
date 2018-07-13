package edu.dartmouth.ccnl.ridmp.com;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.persister.EntityPersister;
import net.sf.hibernate.type.IntegerType;
import net.sf.hibernate.type.LongType;
import net.sf.hibernate.type.Type;
import org.apache.log4j.Logger;

public class WorkFlowDataAccessObject 
{
  static final Logger logger = Logger.getLogger( WorkFlowDataAccessObject.class );
  
  DataAccessObjectUtil dataAccessObjectUtil;
  
  public WorkFlowDataAccessObject()
  {}
  
  /**
   * @return Map
   * @param String className
   */
  private Map findStatusField( final String className )  
  {
    Map map = new HashMap();
    EntityPersister entityPersister = TransferObjectMetaData.getInstance().getEntityPersister( className );
    String[] propertyNames = entityPersister.getPropertyNames();    
    try{
      for ( int proCounter = 0 ; proCounter < propertyNames.length ; proCounter++ )
      {
        String[] propertyColumn = entityPersister.getPropertyColumnNames( proCounter );
        System.out.println( "propertyNames :" + propertyNames[proCounter] + " " + entityPersister.getPropertyType( propertyNames[proCounter] ) );
        for ( int propCounter = 0 ; propCounter < propertyColumn.length ; propCounter++ )
        {
          System.out.println( "propertyColumn :" + propertyColumn[ propCounter ].substring( 3 ) );
          if( propertyColumn[ propCounter ].substring( 3 ).equalsIgnoreCase( DataAccessObjectUtil.DataAccessObjectEnum.STATUS_COL_NAME ) )
          {
            map.put( "propertyName" , propertyNames[proCounter] );
            map.put( "propertyType" , entityPersister.getPropertyType( propertyNames[proCounter] ) );
            break;
          }
        }
      }
    }catch( MappingException ex )
    {
      ex.printStackTrace();
    }
    return map;
  }
  /**
   * @return boolean
   * @param Class clazz
   * @param Serializable pk
   * @throws DataAccessObjectException, HibernateException
   */
  public boolean isObjectInWorkflow( final Class clazz, final Serializable pk ) throws DataAccessObjectException, HibernateException
  {    
    final String entityFullQualifyName = clazz.getName();
    logger.info( "Entity qualify name : " + entityFullQualifyName );
    
    dataAccessObjectUtil = new DataAccessObjectUtil( MethodInvoked.getInstance().getMethodName() );
    
    try{
      if( dataAccessObjectUtil.getIsEditMethod() )
      {          
          Session session = PersistenceUtils.currentSession();
          Query query = session.getNamedQuery( entityFullQualifyName );
          
          setStatusField( (Type)findStatusField( entityFullQualifyName ).get( "propertyType" ) , query );          
          query.setLong( DataAccessObjectUtil.DataAccessObjectEnum.PK_NAME , ((Long)pk).longValue() );
          
          List list = query.list();      
          logger.info( "Count of object has been found in workflow is : " + list.size() );
          if( list.size() == 1 )
            throw new DataAccessObjectException();
      }
    }catch( HibernateException ex )
    {
      if( ex instanceof MappingException )
        return true;
      logger.error( ex, ex.getCause() );
      throw ex;
    }
    return true;
  }
  
  /**
   * @param Type type
   * @param Query query
   */
  private void setStatusField( final Type type , Query query )
  {
    if( type instanceof IntegerType )
        query.setInteger( DataAccessObjectUtil.DataAccessObjectEnum.STATUS_NAME , DataAccessObjectUtil.DataAccessObjectEnum.STATUS );
    if( type instanceof LongType )
        query.setLong( DataAccessObjectUtil.DataAccessObjectEnum.STATUS_NAME , DataAccessObjectUtil.DataAccessObjectEnum.STATUS );
  }
}