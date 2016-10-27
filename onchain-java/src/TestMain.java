import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import OnChain.Core.Transaction;
import OnChain.IO.BinaryWriter;
import OnChain.IO.Serializable;


public class TestMain {

	public static void main(String[] args) {
		Transaction[] transactions = null;
		 try (BinaryWriter writer = new BinaryWriter(new ByteArrayOutputStream()))
	        {
	            writer.writeByte((byte)1); 
	            Serializable[] ss = Arrays.stream(transactions).map(p -> p.hash()).toArray(Serializable[]::new);
	            writer.writeSerializableArray(ss);
	            writer.writeSerializableArray(Arrays.stream(transactions).map(p -> p.hash()).toArray(Serializable[]::new));
	           
	        } catch (Exception e) {
	        	
	        }
		 
	}
}
