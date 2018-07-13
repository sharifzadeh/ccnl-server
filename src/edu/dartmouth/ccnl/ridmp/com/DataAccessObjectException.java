package edu.dartmouth.ccnl.ridmp.com;

import net.sf.hibernate.HibernateException;

public final class DataAccessObjectException extends HibernateException
{
  public DataAccessObjectException()
  {
    super( "Object is attached for workflow " );
  }
  
  public static final int OBJECT_IN_WORKFLOW_CODE = 999;
  public static final String OBJECT_IN_WORKFLOW_DESC = "Object is in workflow";
}