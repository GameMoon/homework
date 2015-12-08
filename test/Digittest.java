package client;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Digittest {
	String a;
	String b;
	@Before
	public void init(){
		a="1234";
		b="1234a";
	}
	@Test
	public void test() {
		Assert.assertEquals(true, Raise.digit(a));
		Assert.assertEquals(false,Raise.digit(b));
	}

}
