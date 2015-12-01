package client;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.Test;

public class Mdtest {
	// a94a8fe5ccb19ba61c4c0873d391e987982fbbd3 online generierte Sh1 hash von "test"
	@Test
	public void mdtest() throws UnsupportedEncodingException, NoSuchAlgorithmException {
		Assert.assertEquals("a94a8fe5ccb19ba61c4c0873d391e987982fbbd3", MD.Mdhash("test"));
	}

}
