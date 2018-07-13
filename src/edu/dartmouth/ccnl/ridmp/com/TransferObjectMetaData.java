package edu.dartmouth.ccnl.ridmp.com;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.persister.EntityPersister;

import org.apache.log4j.Logger;

public class TransferObjectMetaData
{
  private static TransferObjectMetaData _instance;  
  static{
    _instance = new TransferObjectMetaData();
  }  
  static public TransferObjectMetaData getInstance(){ return _instance; }

  static final Logger logger = Logger.getLogger( TransferObjectMetaData.class );
  private TransferObjectMetaData()
  {
    getAllHibernateEntityObjects();
  }
  
  public EntityPersister getEntityPersister( final String className )
  {
    logger.info( "finding specific EntityPersister for " + className );
    return (EntityPersister) getMappedEntity().get( className );
  }
  
  private void getAllHibernateEntityObjects()
  {
    Map mappedEntity = new HashMap();    
    try{
      Collection allMappedObjects = PersistenceUtils.currentSession().getSessionFactory().getAllClassMetadata().values();
      System.out.println( "The number of all mapped objects is : " + allMappedObjects.size() );
      Iterator itr = allMappedObjects.iterator();
      
      while( itr.hasNext() ){
        EntityPersister entityPersister = ( EntityPersister ) itr.next();
        if (entityPersister.getIdentifierPropertyName() != null)
          mappedEntity.put( entityPersister.getClassName() , entityPersister );
      }
    }catch( HibernateException ex )
    {
      logger.error( ex , ex.getCause() ); 
      ex.printStackTrace();
    }
    setMappedEntity( mappedEntity );
  }
  
  
  public void setMappedEntity(Map mappedEntity){
    this.mappedEntity = mappedEntity;
  }
  public Map getMappedEntity(){
    return mappedEntity;
  }
  private Map mappedEntity;
}