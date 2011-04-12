/*
 * SNMP Package
 *
 * Copyright (C) 2002, Jonathan Sevy <jsevy@mcs.drexel.edu>
 *
 * This is free software. Redistribution and use in source and binary forms, with
 * or without modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice, this
 *     list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation 
 *     and/or other materials provided with the distribution.
 *  3. The name of the author may not be used to endorse or promote products 
 *     derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED 
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */


package snmp;

import java.io.*;
import java.math.*;
import java.net.*;
import java.util.*;


/**
*	The class SNMPv1TrapListenerInterface implements a server which listens for trap messages sent from 
*	remote SNMP entities. The approach is that from version 1 of SNMP, using no encryption of data. 
*	Communication occurs via UDP, using port 162, the standard SNMP trap port.
*
*	Applications utilize this class with classes which implement the SNMPTrapListener interface. These
*	must provide a processTrap() method, and are registered/deregistered with this class through its
*	addTrapListener() and removeTrapListener methods.
*/

public class SNMPv1TrapListenerInterface
						implements Runnable
{
	public static final int SNMP_TRAP_PORT = 162;
	
	// largest size for datagram packet payload; based on
	// RFC 1157, need to handle messages of at least 484 bytes
	public static final int MAXSIZE = 512;
	
	private DatagramSocket dSocket;
	private Thread receiveThread;
	
	private Vector listenerVector;
	
	
	/**
	*	Construct a new trap receiver object to receive traps from remote SNMP hosts.
	*	This version will accept messages from all hosts using any community name.
	*/
	
	public SNMPv1TrapListenerInterface()
		throws SocketException
	{
		dSocket = new DatagramSocket(SNMP_TRAP_PORT);
		
		listenerVector = new Vector();
		
		receiveThread = new Thread(this);
		
	}
	
	
	
	public void addTrapListener(SNMPTrapListener listener)
	{
		// see if listener already added; if so, ignore
		for (int i = 0; i < listenerVector.size(); i++)
		{
			if (listener == listenerVector.elementAt(i))
			{
				return;
			}
		}
		
		// if got here, it's not in the list; add it
		listenerVector.add(listener);
	}
	
	
	
	public void removeTrapListener(SNMPTrapListener listener)
	{
		// see if listener in list; if so, remove, if not, ignore
		for (int i = 0; i < listenerVector.size(); i++)
		{
			if (listener == listenerVector.elementAt(i))
			{
				listenerVector.removeElementAt(i);
				break;
			}
		}
		
	}

	
	
	/**
	*	Start listening for trap messages.
	*/
	
	public void startReceiving()
	{
		// if receiveThread not already running, start it
		if (!receiveThread.isAlive())
		{
			receiveThread = new Thread(this);
			receiveThread.start();
		}
	}
	

	
	
	/**
	*	Stop listening for trap messages.
	*/
	
	public void stopReceiving()
		throws SocketException
	{
		// interrupt receive thread so it will die a natural death
		receiveThread.interrupt();
	}

	
	
	
	
	/**
	*	The run() method for the trap interface's listener. Just waits for trap messages to
	*	come in on port 162 (or the port supplied in the constructor), then dispatches the retrieved 
	*   SNMPTrapPDU to each of the registered SNMPTrapListeners by calling their processTrap() methods.
	*/
	
	public void run()
	{
		
		int errorStatus = 0;
		int errorIndex = 0;
		
		
		try
		{
		
			while (!receiveThread.isInterrupted())
			{
				
				DatagramPacket inPacket = new DatagramPacket(new byte[MAXSIZE], MAXSIZE);
		
				dSocket.receive(inPacket);
				
				byte[] encodedMessage = inPacket.getData();
				
				
				/*
				System.out.println("Message bytes length (in): " + inPacket.getLength());
				
				System.out.println("Message bytes (in):");
				for (int i = 0; i < encodedMessage.length; ++i)
				{
					System.out.print(hexByte(encodedMessage[i]) + " ");
				}
				System.out.println("\n");
				*/
				
				
				SNMPMessage receivedMessage = new SNMPMessage(SNMPBERCodec.extractNextTLV(encodedMessage,0).value);
				
				SNMPTrapPDU receivedPDU = receivedMessage.getTrapPDU();
				
				// pass the received trap PDU to the processTrap method of any listeners
				for (int i = 0; i < listenerVector.size(); i++)
				{
					SNMPTrapListener listener = (SNMPTrapListener)listenerVector.elementAt(i);
					
					listener.processTrap(receivedPDU);
				}
				
			}
		
		}
		catch (IOException e)
		{
			// do nothing for now...
		}
		catch (SNMPBadValueException e)
		{
			// do nothing for now...
		}
				
	}
	
	
	
	private String hexByte(byte b)
	{
		int pos = b;
		if (pos < 0)
			pos += 256;
		String returnString = new String();
		returnString += Integer.toHexString(pos/16);
		returnString += Integer.toHexString(pos%16);
		return returnString;
	}
	
	
	
	
	
	
	private String getHex(byte theByte)
	{
		int b = theByte;
		
		if (b < 0)
			b += 256;
		
		String returnString = new String(Integer.toHexString(b));
		
		// add leading 0 if needed
		if (returnString.length()%2 == 1)
			returnString = "0" + returnString;
			
		return returnString;
	}
	
	
	
}