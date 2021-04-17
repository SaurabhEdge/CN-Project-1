//package networks;

/*
 * 
 * 
 * 

 * @author      Saurabh Shukla
 *
 * Version:  1.0
 *     
 *     
 * pktanalyzer.java
 * 
 This class performs the packet analyzing process where as per data it calculates the various aspects of
 networking components namely header length, source and destination , TTL , TOS etc.


It has 5 classes mentioned below:

a) EthernetHeader
b) IPHeader
c) TCPFile
d) UDPFile
e) ICMPfile


 */



import java.io.File;


/*
 * 
 * Class pktanalyzer reads file as command line argument and displays the binary data in console
 * 
 *  Once data is read from file it calls class EthernetHeader to process and start analyzing
 *  the data present in file. 
 *  
 *  @MethodName - Main method
 *  @param - Command line argument (file location)
 * 
 * 
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class pktanalyzer {

	public static void main(String[] args) {
		
		if (args.length==1) {
		String FilePath = args[0];		
		File file = new File(FilePath);
		int flag = 0;
		
		byte[] data = new byte[(int) file.length()];
		int count = 0;
		try {
			
			FileInputStream fileStrame = new FileInputStream(file);
			try {
				fileStrame.read(data);
				
			} catch (IOException e) {
				
				System.out.println(" Error while reading file --> cursor in First inside catch block !");
				e.getMessage();
				
				
			}
			System.out.println();
			System.out.println("############     File Data      #############");

			System.out.println("______________________________________________________");
			System.out.println();
			for (int i = 0; i < data.length; i++) {
				count++;
				System.out.printf("%02X ", data[i]);
				if (i % 16 == 0) {
					if (i == 0) {
					} else
						System.out.println();
				}
			}
		
			System.out.println();
			System.out.println("_________________________________________________________");
			System.out.println();
			System.out.println();
		   EthernetHeader eh=new EthernetHeader();
		   eh.headerRead(data, (int)file.length(), count);
		   
		   
		
		}catch (FileNotFoundException e) {
				System.out.println("Error occured while locating file  --> Cursor in outer catch block");
				e.getMessage();

			}
	}
		
	else {
		System.out.println(" Please Enter Command line argument (File Path) as per execution steps !!");
	}

}

}

/*
 * 
 * Class EthernetHeader is invoked after reading of data from file to process ethernet header.
 * 
 * to process Ethernet header it calls Read method and displays the data as per tags.
 *  
 * 
 * 	  @MethodName -     headerRead
 *    @param1 -         byte array to pass data 
 *    @param2 -			Length of byte array 
 *    @param3 -         size of header
 *    @returnType -     No return type
 * 
 * 
 */




class EthernetHeader{
	
	public void headerRead(byte[] byteData, int length, int size) {
		
		byte[] byteDataE = new byte[length];
		for (int i =0;i< byteDataE.length ;i++) {
			byteDataE[i]=byteData[i];
			
		}
		int sizeOfHeader=size;
		System.out.println();
		System.out.println();
		System.out.println(" ETHER: ----- Ether Header -----");
		System.out.println(" ETHER: ");
		System.out.println(" ETHER: Packet size = " + sizeOfHeader + " bytes");
		System.out.print(" ETHER: Destination = ");
		for (int j = 0; j < 6; j++)
			System.out.printf("%02X:", byteDataE[j]);
		System.out.println();
		System.out.print(" ETHER: Source      = ");
		for (int j = 6; j < 12; j++)
			System.out.printf("%02X:", byteDataE[j]);
		System.out.println();
		
	boolean flag=false;
		if(byteDataE[12]==8) {
			if(byteDataE[13]==0) {
				System.out.print(" ETHER: Ethertype   = ");
				for (int j = 12; j < 14; j++)
					System.out.printf("%02X", byteDataE[j]);
				System.out.print(" (IP)");
				System.out.println();
				flag=true;
			}
		}
		
		System.out.println(" ETHER: ");
		if(flag) {
			
			IPHeader ih=new IPHeader(); 
			ih.IPheaderRead(byteDataE, length, sizeOfHeader);
		}
		else {
			System.out.println(" No IP header ! ");
		}
		 
	}
	
}




/*
 * 
 * Class IPHeader is invoked as per protocol identified in Ethernet header.
 * 
 * to process IP header it calls Read method and displays the data as per tags.
 *  
 * 
 * 	  @MethodName -     IPheaderRead
 *    @param1 -         byte array to pass data 
 *    @param2 -			Length of byte array 
 *    @param3 -         size of header
 *    @returnType -     No return type
 * 
 * 
 */

class IPHeader{
	
	public void IPheaderRead(byte[] byteData, int length, int size) {
		
		byte[] byteDataE = new byte[length];
		for (int i =0;i< byteDataE.length ;i++) {
			byteDataE[i]=byteData[i];
			
		}
		int sizeOfHeader=size;
		System.out.println(" IP: ----- IP Header -----");
		System.out.println(" IP:");
		System.out.print(" IP: Version         = ");
		System.out.printf(" %01X", byteDataE[14]/16);
		System.out.println();
		System.out.println(" IP: Header length   = "+((byteDataE[14]/16)*(byteDataE[14]%16)));
		System.out.print(" IP: Type of service = ");
		System.out.printf(" %02X", byteDataE[15]);
		System.out.println();
		System.out.println(" IP: Total length    =  "+(byteDataE.length-14));
		System.out.print(" IP: Identification  =  ");
		String identification="";
		int j1=0;
		for (int j = 18; j < 20; j++)			
			System.out.printf("%02X",byteDataE[j]);		

		System.out.println();
		System.out.print(" IP: Flags =  ");
		System.out.printf(" %02X", byteDataE[21]);
		System.out.println();
		System.out.print(" IP: Fragment offset = ");
		System.out.printf(" %02X", byteDataE[21]);
		System.out.println();
		System.out.print(" IP: Time to live    = ");

		System.out.print( (byteDataE[22]&0xFF)+" seconds/hop");
		System.out.println();
		boolean flagUdp=false;
		boolean flagicmp=false;
		boolean flagtcp=false;
		
		if(byteDataE[23]== 17) {
			System.out.print(" IP: Protocol        =");
			System.out.printf(" %02X", byteDataE[23]);
			System.out.print(" (UDP)");
			System.out.println();
		flagUdp=true;
		
		}
		
		else {
			if(byteDataE[23]==6) {
				System.out.print(" IP: Protocol        =");
				System.out.printf(" %02X", byteDataE[23]);
				System.out.print(" (TCP)");
				System.out.println();
				flagtcp=true;
				
			}
			
			else {
				
				if(byteDataE[23]==1) {
					System.out.print(" IP: Protocol        =");
					System.out.printf(" %02X", byteDataE[23]);
					System.out.print(" (ICMP)");
					System.out.println();
					flagicmp=true;
					
			}
		}
		}

		System.out.print(" IP: Header checksum = ");
		for (int j = 24; j < 26; j++)
			System.out.printf("%02X", byteDataE[j]);
		System.out.println();
		System.out.print(" IP: Source address  = ");
		for (int j = 26; j < 30; j++)
			
		{	System.out.print((byteDataE[j]&0xFF)+":");
		}
		
		System.out.println();
		System.out.print(" IP: Destination address = ");
		for (int j = 30; j < 34; j++)
			{
			
			System.out.print((byteDataE[j]&0xFF)+":");
			}
		System.out.println();
		System.out.println(" IP: No options");
		System.out.println(" IP: ");
		
		if(flagUdp) {

			UDPFile uf=new UDPFile();
			uf.UDPheaderRead(byteDataE, length, sizeOfHeader);
		}
		
		if(flagicmp) {
			
			ICMPfile ifile=new ICMPfile();
			ifile.ICMPheaderRead(byteDataE, length, sizeOfHeader);

		}
		
		if(flagtcp) {


			TCPFile tf=new TCPFile();
			tf.TCPheaderRead(byteDataE, length, sizeOfHeader);
		}
	}
}




/*
 * 
 * Class TCPFile is invoked as per protocol identified in IP header.
 * 
 * to process TCP header it calls Read method and displays the data as per tags.
 *  
 * 
 * 	  @MethodName -     TCPheaderRead
 *    @param1 -         byte array to pass data from IP to TCP header processing
 *    @param2 -			Length of byte array 
 *    @param3 -         size of header
 *    @returnType -     No return type
 * 
 * 
 */

class TCPFile{
	
public void TCPheaderRead(byte[] byteData, int length, int size) {
		
		byte[] byteDataE = new byte[length];
		for (int i =0;i< byteDataE.length ;i++) {
			byteDataE[i]=byteData[i];
			
		}
		int sizeOfHeader=size;
		System.out.println(" TCP: ----- TCP Header -----");
		System.out.println(" TCP:");
		System.out.print(" TCP: Source port = ");
		for (int j = 34; j < 36; j++)
			System.out.printf("%02X:", byteDataE[j]);
		System.out.println();
		System.out.print(" TCP: Destination port = ");
		for (int j = 36; j < 38; j++)
			System.out.printf("%02X:", byteDataE[j]);
		System.out.println();
		System.out.print(" TCP: Sequence number = ");
		for (int j = 38; j < 42; j++)
			System.out.printf("%02X:", byteDataE[j]);
		System.out.println();
		System.out.print(" TCP: Acknowledgement number = ");
		for (int j = 42; j < 46; j++)
			System.out.printf("%02X:", byteDataE[j]);
		System.out.println();
		System.out.print(" TCP: Data offset = ");
		
		System.out.printf("%02X", byteDataE[46]/16);
		System.out.println();
		System.out.print(" TCP: Flags = ");
		System.out.printf("%02X", byteDataE[47]);
		System.out.println();
		System.out.print(" TCP: Window = ");
		for (int j = 48; j < 50; j++)
			System.out.printf("%02X", byteDataE[j]);
		System.out.println();
		System.out.print(" TCP: Checksum = ");
		for (int j = 50; j < 52; j++)
			System.out.printf("%02X", byteDataE[j]);
		System.out.println();
		System.out.print(" TCP: Urgent pointer = ");
		for (int j = 52; j < 54; j++)
			System.out.printf("%02X", byteDataE[j]);
		System.out.println();
		System.out.println(" TCP: No options ");
		System.out.println(" TCP:");
		System.out.print(" TCP: Data: (first 64 bytes)");
		System.out.println();
		System.out.println();
		int flagcounter=0;
		for (int j = 34; j <(34+64); j++) {
			System.out.printf(" %02X ", byteDataE[j]);
			flagcounter++;
			if(flagcounter%16==0) {
				
				System.out.println();
			}
			
		}
		System.out.println();
}
	
}




/*
 * 
 * Class UDPfile is invoked as per protocol identified in IP header.
 * 
 * to process UDP header it calls Read method and displays the data as per tags.
 *  
 * 
 * 	  @MethodName -     UDPheaderRead
 *    @param1 -         byte array to pass data from IP to UDP header processing
 *    @param2 -			Length of byte array 
 *    @param3 -         size of header
 *    @returnType -     No return type
 * 
 * 
 */

class UDPFile{
	
public void UDPheaderRead(byte[] byteData, int length, int size) {
		
		byte[] byteDataE = new byte[length];
		for (int i =0;i< byteDataE.length ;i++) {
			byteDataE[i]=byteData[i];
			
		}
		int sizeOfHeader=size;
		System.out.println(" UDP: ----- UDP Header -----");
		System.out.println(" UDP:");
		System.out.print(" UDP: Source port = ");
		for (int j = 34; j < 36; j++)
			System.out.printf("%02X", byteDataE[j]);
		System.out.println();
		System.out.print(" UDP: Destination port =  ");
		for (int j = 36; j < 38; j++)
			System.out.print(byteDataE[j]);
			
		System.out.println();
		System.out.println(" UDP: Length = "+(byteDataE.length-14-20));
		
		System.out.print(" UDP: Checksum = ");
		for (int j = 40; j < 42; j++)
			System.out.printf("%02X", byteDataE[j]);
		System.out.println();
		System.out.println(" UDP:");
		System.out.println(" UDP: Data: (first 64 bytes)");
		System.out.println();
		int flagcounter=0;
		for (int j = 34; j <byteDataE.length; j++) {
			System.out.printf(" %02X ", byteDataE[j]);
			flagcounter++;
			if(flagcounter%16==0) {
				
				System.out.println();
			}
			
		}
		System.out.println();
}
}




/*
 * 
 * Class ICMPfile is invoked as per protocol identified in IP header.
 * 
 * to process ICMP header it calls Read method and displays the data as per tags.
 *  
 * 
 * 	  @MethodName -     ICMPheaderRead
 *    @param1 -         byte array to pass data from IP to ICMP header processing
 *    @param2 -			Length of byte array 
 *    @param3 -         size of header
 *    @returnType -     No return type
 * 
 * 
 */

class ICMPfile{
	
public void ICMPheaderRead(byte[] byteData, int length, int size) {
		
		byte[] byteDataE = new byte[length];
		for (int i =0;i< byteDataE.length ;i++) {
			byteDataE[i]=byteData[i];
			
		}
		int sizeOfHeader=size;
		
		System.out.println(" ICMP: ----- ICMP Header -----");
		System.out.println(" ICMP:");
		
		System.out.print(" ICMP: Type = ");
		System.out.printf("  %02X", byteDataE[34]);
		System.out.println();
		System.out.print(" ICMP: Code = ");
		System.out.printf("  %02X", byteDataE[35]);
		System.out.println();
		System.out.print(" ICMP: Checksum =");
		System.out.printf(" %02X", byteDataE[36]);
		System.out.printf("%02X", byteDataE[37]);
		System.out.println();
		System.out.println(" ICMP:");
		
		
}
	
}



