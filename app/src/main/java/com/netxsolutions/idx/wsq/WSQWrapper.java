/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.5
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.netxsolutions.idx.wsq;

public class WSQWrapper {
  public static int DecodeWSQFile(String WSQFilePath, String RAWFilePath) {
    return WSQWrapperJNI.DecodeWSQFile(WSQFilePath, RAWFilePath);
  }

  public static int EncodeWSQFile(String RAWFilePath, String WSQFilePath, float r_bitrate, int width, int height, String comment) {
    return WSQWrapperJNI.EncodeWSQFile(RAWFilePath, WSQFilePath, r_bitrate, width, height, comment);
  }

  public static int DecodeWSQToFile(String WSQFilePath, String RAWFilePath, int outputformat) {
    return WSQWrapperJNI.DecodeWSQToFile(WSQFilePath, RAWFilePath, outputformat);
  }

  public static int AuthenticateLicence(String licencedTo, String licence) {
    return WSQWrapperJNI.AuthenticateLicence(licencedTo, licence);
  }

}
