//package ExternalDevices
package kotlinGUI

class SettingsCOM
{
	var baudRate : Int = 9600;
	var dataBits : Int = 8;
/* 		public Parity parity = Parity.None;
		public StopBits stopBits = StopBits.One;
		public bool dtrEnable = false;
		public int readTimeout = 750;
		public int WriteTimeout = 750; */

	var name : String = "";

	var description : String = ""
    get() = name

	fun CheckName() : Boolean
    {
        return true
			/* if ( String.IsNullOrEmpty(name) )
			{
				return false;
			}
			return name.Contains( "COM" );
            } */
    }
}