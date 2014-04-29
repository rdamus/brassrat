package com.brassratdev;

import org.apache.commons.digester3.Digester;

public class XMLDigester {
	protected Digester digester;
	
	public XMLDigester(){
		digester = new Digester();
		digester.setValidating( false );
	}
	
}
