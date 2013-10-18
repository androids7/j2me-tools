/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.j2me.utils;

/**
 *
 * @author willian
 */
public class Phone {
    private static String imei = "";
    private static String appName = "";
    private static String appVendor = "";
    private static String appVersion = "";
    private static String smsc = "";

    /**
	 * get the cell id in the phone
	 *
	 * @return
	 */
    public static String CELL_ID(){
        if(smsc == null || smsc.equals("")){
            smsc = System.getProperty("com.nokia.mid.cellid");

            if(smsc == null || smsc.equals("")){
                smsc = System.getProperty("CellID");
            }

            if(smsc == null || smsc.equals("")){
                smsc = System.getProperty("Cell-ID");
            }

            if(smsc == null || smsc.equals("")){
                smsc = System.getProperty("com.nokia.mid.networkid");
            }

            if(smsc == null || smsc.equals("")){
                smsc = System.getProperty("com.sonyericsson.sim.subscribernumber");
            }

            if (smsc == null || smsc.equals("")) {
                smsc = System.getProperty("MSISDN");
            }

            if (smsc == null || smsc.equals("")) {
                smsc = System.getProperty("phonenumber");
            }

			if(smsc== null ||smsc.equals("null")|| smsc.equals(""))
				System.getProperty("phone.cid");
			//polish.Vendor == Nokia
			if(smsc== null ||smsc.equals("null")|| smsc.equals(""))
				smsc = System.getProperty("com.nokia.mid.cellid");
			//polish.Vendor == Sony-Ericsson
			if(smsc== null ||smsc.equals("null")|| smsc.equals(""))
				smsc = System.getProperty("com.sonyericsson.net.cellid");
			//polish.Vendor == Motorola
			if(smsc== null ||smsc.equals("null")|| smsc.equals(""))
				smsc = System.getProperty("phone.cid");//System.getProperty("CellID");
			//polish.Vendor == Samsung
			if(smsc== null ||smsc.equals("null")|| smsc.equals(""))
				smsc = System.getProperty("com.samsung.cellid");
			//polish.Vendor == Siemens
			if(smsc== null ||smsc.equals("null")|| smsc.equals(""))
				smsc = System.getProperty("com.siemens.cellid");
			//polish.Vendor == BlackBerry
			//if(smsc== null ||smsc.equals("null")|| smsc.equals(""))
			//	smsc = GPRSInfo.getCellInfo().getCellId();
			if(smsc== null ||smsc.equals("null")|| smsc.equals(""))
				smsc = System.getProperty("cid");

            /*
            if (smsc == null || smsc.equals("")) {
                smsc = System.getProperty("wireless.messaging.mms.mmsc");
            }*/

            /*
            if(smsc == null || smsc.equals("")){
                smsc = System.getProperty("wireless.messaging.sms.smsc");
            }*/

            if(smsc != null){
                if(smsc.length() > 10){
                    smsc = smsc.substring(smsc.length()-10);
                }
            }
        }

        return smsc;
    }

    /**
	 * not used now
	 * get the IMEI (International Mobile Equipment Identity (IMEI)) in the phone
	 * 
	 * @return
	 */
    public static String IMEI() {
        //*===================================================================*/
        //  IMEI Device Siemens
        //*===================================================================*/
        //  Siemens: System.getProperty("com.siemens.IMEI");
        //*===================================================================*/

        //*===================================================================*/
        //  IMEI Device Samsung
        //*===================================================================*/
        //  Samsung: System.getProperty("com.samsung.imei");
        //*===================================================================*/

        //*===================================================================*/
        //  IMEI Device SonyEricsson
        //*===================================================================*/
        //  SonyEricsson: System.getProperty("com.sonyericsson.imei");
        //*===================================================================*/

        //*===================================================================*/
        //  IMEI Device Motorola
        //*===================================================================*/
        //  Motorola: System.getProperty("IMEI");
        //                 System.getProperty("com.motorola.IMEI");
        //*===================================================================*/

        //*===================================================================*/
        //  IMEI Device Nokia
        //*===================================================================*/
        //  Nokia: System.getProperty("phone.imei");
        //            System.getProperty("com.nokia.IMEI");
        //            System.getProperty("com.nokia.mid.imei");
        //*===================================================================*/

        try{

			imei = System.getProperty("com.imei");
			//Vendor == Nokia
			if(imei== null ||imei.equals("null")|| imei.equals(""))
				imei = System.getProperty("phone.imei");
			if(imei== null ||imei.equals("null")|| imei.equals(""))
				imei = System.getProperty("com.nokia.IMEI");
			if(imei== null ||imei.equals("null")|| imei.equals(""))
				imei = System.getProperty("com.nokia.mid.imei");
			if(imei== null ||imei.equals("null")|| imei.equals(""))
				imei = System.getProperty("com.nokia.mid.imei");
			//Vendor == Sony-Ericsson
			if(imei== null ||imei.equals("null")|| imei.equals(""))
				imei = System.getProperty("com.sonyericsson.imei");
			//Vendor == Motorola
			if(imei== null ||imei.equals("null")|| imei.equals(""))
				imei = System.getProperty("IMEI");
			if(imei== null ||imei.equals("null")|| imei.equals(""))
				imei = System.getProperty("com.motorola.IMEI");
			//Vendor == Samsung
			if(imei== null ||imei.equals("null")|| imei.equals(""))
				imei = System.getProperty("com.samsung.imei");
			//Vendor == Siemens
			if(imei== null ||imei.equals("null")|| imei.equals(""))
				imei = System.getProperty("com.siemens.imei");

			if(imei== null ||imei.equals("null")|| imei.equals(""))
				imei = System.getProperty("imei");

		}catch(Exception e){
			e.printStackTrace();
		}

        if(imei == null)
            imei = "";

        return imei;
    }

    /**
	 * get the lac sring from phone
	 */
	public static String getLAC(){
		String out = "";
		try{

			out = System.getProperty("phone.lac");

			//Vendor == Nokia
			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("com.nokia.mid.lac");
			//Vendor == Sony-Ericsson
			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("com.sonyericsson.net.lac");
			//Vendor == Motorola
			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("LocAreaCode");
			//Vendor == Samsung
			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("com.samsung.cellid");
			//Vendor == Siemens
			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("com.siemens.cellid");

			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("cid");


		}catch(Exception e){
			return out==null?"":out;
		}

		return out==null?"":out;
	}

	/**
	 *  Example IMSI (O2 UK): 234103530089555
		String mcc = imsi.substring(0,3); // 234 (UK)
		String mnc = imsi.substring(3,5); // 10 (O2)
	 * @return
	 */
	public static String getIMSI(){
		String out = "";
		try{

			out = System.getProperty("IMSI");
			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("phone.imsi") ;
			//Vendor == Nokia
			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("com.nokia.mid.mobinfo.IMSI");
			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("com.nokia.mid.imsi");
			//Vendor == Sony-Ericsson
			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("com.sonyericsson.imsi");
			//Vendor == Motorola
			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("IMSI");
			//Vendor == Samsung
			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("com.samsung.imei");
			//Vendor == Siemens
			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("com.siemens.imei");

			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("imsi");


		}catch(Exception e){
			return out==null?"":out;
		}

		return out==null?"":out;
	}

	/**
	 *
	 *  For moto, Example IMSI (O2 UK): 234103530089555
		String mcc = imsi.substring(0,3); // 234 (UK)
	 * @return
	 */
	public static String getMCC(){
		String out = "";
		try{

			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("phone.mcc") ;
			//Vendor == Nokia
			if(out== null ||out.equals("null")|| out.equals(""))
				//out = System.getProperty("com.nokia.mid.mobinfo.IMSI");
			//Vendor == Sony-Ericsson
			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("com.sonyericsson.net.mcc");
			//Vendor == Motorola
			if(out== null ||out.equals("null")|| out.equals("")){
				out = getIMSI().equals("")?"": getIMSI().substring(0,3);
			}
			//Vendor == Samsung
			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("com.samsung.imei");
			//Vendor == Siemens
			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("com.siemens.imei");

			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("mcc");


		}catch(Exception e){
			return out==null?"":out;
		}

		return out==null?"":out;
	}

	/**
	 *
	 *  For moto, Example IMSI (O2 UK): 234103530089555
		String mnc = imsi.substring(3,5); // 10 (O2)
	 * @return
	 */
	public static String getMNC(){
		String out = "";
		try{

			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("phone.mnc") ;
			//Vendor == Nokia
			if(out== null ||out.equals("null")|| out.equals(""))
				out = getIMSI().equals("")?"": getIMSI().substring(3,5);
			//Vendor == Sony-Ericsson
			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("com.sonyericsson.net.mnc");
			//Vendor == Motorola
			if(out== null ||out.equals("null")|| out.equals("")){
				out = getIMSI().equals("")?"": getIMSI().substring(3,5);
			}
			//Vendor == Samsung
			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("com.samsung.imei");
			//Vendor == Siemens
			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("com.siemens.imei");
			if(out== null ||out.equals("null")|| out.equals(""))
				out = System.getProperty("mnc");


		}catch(Exception e){
			return out==null?"":out;
		}

		return out==null?"":out;
	}

    /**
     * 
     * @return Retorna o nome da Aplicação
     */
    public static String AppName(){
        if ( appName == null || appName.equals( "" ) )
            appName = System.getProperty( "MIDlet-Name" );

        if(appName == null)
            appName = "";

        return appName;
    }

    /**
     * 
     * @return Retorna o fornecedor da Aplicação
     */
    public static String AppVendor(){
        if ( appVendor == null || appVendor.equals( "" ) )
            appVendor = System.getProperty( "MIDlet-Vendor" );

        if(appVendor == null)
            appVendor = "";

        return appVendor;
    }

    /**
     * 
     * @return Retorna a versão da Aplicação
     */
    public static String AppVersion(){
        if ( appVersion == null || appVersion.equals( "" ) )
            appVersion = System.getProperty( "APP-Version" );

        if(appVersion == null)
            appVersion = "";

        return appVersion;
    }
}
