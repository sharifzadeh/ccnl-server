package edu.dartmouth.ccnl.ridmp.com;

import java.io.FileNotFoundException;

import java.io.IOException;

import java.util.StringTokenizer;

public interface IBundleUtility 
{
  boolean isEditMethod(final String methodName) throws Exception;
  StringTokenizer getKeyValue() throws FileNotFoundException, IOException;
}
